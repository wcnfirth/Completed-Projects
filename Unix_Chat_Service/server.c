#include "blather.h"

/* return pointer to specific client */
client_t *server_get_client(server_t *server, int idx) {
   return &(server->client[idx]);
}

/* initialize and start server of given name */
void server_start(server_t *server, char *server_name, int perms) {
   printf("server_start()\n");
   remove(server_name); // remove any other fifo with the same name
   int myfd = mkfifo(server_name, perms); // make a new join fifo
   printf("%d\n", errno);
   check_fail(myfd==-1, 1, "Couldn't make file %s", server_name);
   server->join_fd = open(server_name, S_IRUSR | O_NONBLOCK); // open fifo, store fd
   server->join_ready = 0;
   server->n_clients = 0;
   server->time_sec = 0;
   strncpy(server->server_name, server_name, MAXPATH);
   printf("server_start(): end\n");
}

/* shutdown a server */
void server_shutdown(server_t *server) {
   /* close and remove join fifo */
   close(server->join_fd);
   remove(server->server_name);
   /* remove clients from server */
   mesg_kind_t shutdown = BL_SHUTDOWN;
   mesg_t mesg;
   memset(&mesg, 0, sizeof(mesg_t));
   mesg.kind = shutdown;
   int i, err;
   for (i=0; i < server->n_clients; i++) { // send shutdown message to clients
      client_t* client = server_get_client(server, i);
      err = write(client->to_client_fd, &mesg, sizeof(mesg_t));
      check_fail(err==-1, 1, "Couldn't write to file %s", client->to_client_fname);
   }
   for (i=0; i < server->n_clients; i++) { // remove clients from server
      err = server_remove_client(server, i);
      check_fail(err==-1, 1, "Couldn't remove client %s", server->client[i].name);
   }
}

/* add client to a server */
int server_add_client(server_t *server, join_t *join) {
   printf("server_add_client(): %s %s %s\n", join->name, join->to_client_fname,
          join->to_server_fname);
   if (server->n_clients == MAXCLIENTS) { return -1; } // not enough space for client
   /* open to_client and to_server fifo's */
   int to_client_fd = open(join->to_client_fname, O_WRONLY);
   check_fail(to_client_fd==-1, 1, "Couldn't open file %s", join->to_client_fname);
   int to_server_fd = open(join->to_server_fname, O_RDONLY);
   check_fail(to_server_fd==-1, 1, "Couldn't open file %s", join->to_server_fname);
   /* initialize client data */
   client_t client;
   memset(&client, 0, sizeof(client_t));
   strncpy(client.name, join->name, MAXPATH);
   client.to_client_fd = to_client_fd;
   client.to_server_fd = to_server_fd;
   strncpy(client.to_client_fname, join->to_client_fname, MAXPATH);
   strncpy(client.to_server_fname, join->to_server_fname, MAXPATH);
   client.data_ready = 0;
   /* add client to server's client array */
   server->client[server->n_clients] = client;
   server->n_clients += 1;
   return 0;
}

/* remove client from a server */
int server_remove_client(server_t *server, int idx) {
   printf("server_remove_client(): %d\n", idx);
   /* close and remove to_client and to_server fifo's */
   client_t* client = server_get_client(server, idx);
   int tc_fd = close(client->to_client_fd);
   int ts_fd = close(client->to_server_fd);
   int tc_fname = remove(client->to_client_fname);
   int ts_fname = remove(client->to_server_fname);
   if (tc_fd != 0 || ts_fd != 0 || tc_fname != 0 || ts_fname != 0) { // failed to close & remove
      return -1;
   }
   /* shift remaining clients */
   if (idx < server->n_clients-1) {
      int i;
      for (i=idx+1; i < server->n_clients; i++) {
         server->client[i-1] = server->client[i];
      }
   }
   server->n_clients -= 1; // decrement client count
   return 0;
}

/* broadcast a message to all clients connected to the server */
int server_broadcast(server_t *server, mesg_t *mesg) {
   printf("server_broadcast(): %d from %s - %s\n", mesg->kind, mesg->name, mesg->body);
   int i, err;
   for (i=0; i < server->n_clients; i++) { // write message to all to_client fifo's
      client_t* client = server_get_client(server, i);
      err = write(client->to_client_fd, mesg, sizeof(mesg_t));
      check_fail(err==-1, 1, "Couldn't write to file %s", client->to_client_fname);
      if (err < 0) { return -1; } // write was not successful
   }
   return 0;
}

/* check if any clients have written to the server or want to join the server */
void server_check_sources(server_t *server) {
   fd_set client_set; // make fd set
   FD_ZERO(&client_set); // initialize client_set
   FD_SET(server->join_fd, &client_set); // add join_fd to client_set
   int maxfd = server->join_fd;
   /* add client to_server_fd's to client_set */
   int i, err;
   for (i=0; i < server->n_clients; i++) {
      client_t* client = server_get_client(server, i);
      FD_SET(client->to_server_fd, &client_set);
      if (maxfd < client->to_server_fd) { maxfd = client->to_server_fd; }
   }
   /* monitor client to_server fifo's for new data */
   err = select(maxfd+1, &client_set, NULL, NULL, NULL);
   check_fail(err==-1, 1, "Could not detect client data");
   /* set data_ready flags for clients with new data */
   if (FD_ISSET(server->join_fd, &client_set)) { // see if a client wants to join
      server->join_ready = 1; // set server's join_ready flag
   }
   for (i=0; i < server->n_clients; i++) {
      client_t* client = server_get_client(server, i);
      if (FD_ISSET(client->to_server_fd, &client_set)) { // new data
         client->data_ready = 1; // set client's data_ready flag
      }
   }
}

/* check if a new client wants to join the server */
int server_join_ready(server_t *server) {
   return server->join_ready;
}

/* read join request and add new client to the server */
int server_handle_join(server_t *server) {
   printf("server_handle_join()\n");
   join_t join;
   memset(&join, 0, sizeof(join_t));
   int err = read(server->join_fd, &join, sizeof(join_t)); // read join request
   check_fail(err==-1, 1, "Couldn't read join request");
   server_add_client(server, &join); // add client to server
   /* prepare and broadcast message that a client has joined */
   mesg_kind_t kind = BL_JOINED;
   mesg_t mesg;
   memset(&mesg, 0, sizeof(mesg_t));
   mesg.kind = kind;
   strncpy(mesg.name, join.name, MAXPATH);
   server_broadcast(server, &mesg);
   server->join_ready = 0; // reset flag
   return 0;
}

/* check if client has sent a message */
int server_client_ready(server_t *server, int idx) {
   client_t* client = server_get_client(server, idx);
   return client->data_ready;
}

/* read message from client and take action */
int server_handle_client(server_t *server, int idx) {
   mesg_t mesg_actual;
   mesg_t* mesg = &mesg_actual;
   memset(mesg, 0, sizeof(mesg_t));
   client_t* client = server_get_client(server, idx);
   int err = read(client->to_server_fd, mesg, sizeof(mesg_t)); // read message from client
   check_fail(err==-1, 1, "Couldn't read message from client %s", client->name);
   /* identify message type and act accordingly */
   mesg_kind_t kind = mesg->kind;
   if (kind == BL_MESG) { // client sending message to rest of clients
      printf("server: mesg received from client %d %s : %s\n", idx, mesg->name, mesg->body);
      server_broadcast(server, mesg);
   } else if (kind == BL_DEPARTED) { // client departed
      printf("server: departed client %d %s\n", idx, mesg->name);
      server_remove_client(server, idx);
      server_broadcast(server, mesg);
   }
   client->data_ready = 0;
   return 0;
}