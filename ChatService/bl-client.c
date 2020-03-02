#include "blather.h"

/* Global Data Structures */
simpio_t simpio_actual;
simpio_t* simpio = &simpio_actual;

client_t client_actual;
client_t* client = &client_actual;

join_t join_actual;
join_t* join = &join_actual;

pthread_t user_thread;
pthread_t server_thread;

/* Global Variables */
char server_name_actual[MAXPATH], username_actual[MAXPATH];
char to_client_fname_actual[MAXPATH], to_server_fname_actual[MAXPATH];
char* server_name = &(server_name_actual[0]); // server name
char* username = &(username_actual[0]); // client name
char* to_client_fname = &(to_client_fname_actual[0]); // server-to-client FIFO name
char* to_server_fname = &(to_server_fname_actual[0]); // client-to-server FIFO name
char* serv = "to_server_"; // used for naming client-to-server FIFO
char* usr = "to_user_"; // used for naming server-to-client FIFO

/* user thread -- monitor input and send messages to server */
void* user_to_server(void *arg) {
   while (!simpio->end_of_input) {
      simpio_reset(simpio); // reset simpio
      iprintf(simpio, ""); // new prompt
      /* keep track of client input */
      while (!simpio->line_ready && !simpio->end_of_input) {
         simpio_get_char(simpio);
      }
      /* translate client input into mesg_t, send to server through FIFO */
      if (simpio->line_ready) {
         mesg_kind_t norm = BL_MESG;
         mesg_t norm_mesg_actual;
         mesg_t* norm_mesg = &norm_mesg_actual;
         memset(norm_mesg, 0, sizeof(mesg_t));
         norm_mesg->kind = norm;
         strncpy(norm_mesg->name, username, MAXNAME);
         strncpy(norm_mesg->body, simpio->buf, MAXLINE);
         write(client->to_server_fd, norm_mesg, sizeof(mesg_t));
      }
   }
   /* write DEPARTED mesg_t to the server */
   mesg_kind_t departed = BL_DEPARTED;
   mesg_t depart_mesg_actual;
   mesg_t* depart_mesg = &depart_mesg_actual;
   memset(depart_mesg, 0, sizeof(mesg_t));
   depart_mesg->kind = departed;
   strncpy(depart_mesg->name, username, MAXNAME);
   write(client->to_server_fd, depart_mesg, sizeof(mesg_t));
   /* close client-to-server FIFO and cancel the server thread */
   pthread_cancel(server_thread);
   close(client->to_server_fd);
   return NULL;
}

/* server thread -- detect messages from the server and act accordingly */
void* server_to_user(void *arg) {
   while (1) {
      mesg_t mesg_actual;
      mesg_t* mesg = &mesg_actual;
      memset(mesg, 0, sizeof(mesg_t));
      read(client->to_client_fd, mesg, sizeof(mesg_t)); // read message from server
      mesg_kind_t kind = mesg->kind;
      if (kind == BL_MESG) { // normal message
         iprintf(simpio, "[%s] : %s\n", mesg->name, mesg->body);
      } else if (kind == BL_JOINED) { // new client joined the chat
         iprintf(simpio, "-- %s JOINED --\n", mesg->name);
      } else if (kind == BL_DEPARTED) { // client left the chat
         iprintf(simpio, "-- %s DEPARTED --\n", mesg->name);
      } else if (kind == BL_SHUTDOWN) { // server is shutting down
         iprintf(simpio, "!!! server is shutting down !!!\n");
         printf("\n");
         break;
      }
   }
   /* close server-to-client FIFO and cancel the server thread */
   pthread_cancel(user_thread);
   close(client->to_client_fd);
   return NULL;
}

/* main function */
int main(int argc, char *argv[]) {
   if (argc != 3) { // check for proper input
      printf("inproper input: enter <server> <username>\n");
      return 0;
   }
   int pid = getpid();
   char cpid[16];
   sprintf(cpid, "%d.fifo", pid); // cast pid to char array
   /* store applicable data in corresponding variables */
   strcpy(to_client_fname, usr);
   strcpy(to_server_fname, serv);
   strcat(to_client_fname, cpid);
   strcat(to_server_fname, cpid);
   strcpy(server_name, argv[1]);
   strcpy(username, argv[2]);
   /* make fifo's for communication to and from server */
   mkfifo(to_client_fname, DEFAULT_PERMS);
   mkfifo(to_server_fname, DEFAULT_PERMS);
   /* create join_t instance with fifo names and client name */
   strcpy(join->name, username);
   strcpy(join->to_client_fname, to_client_fname);
   strcpy(join->to_server_fname, to_server_fname);
   /* open server's join_fd fifo and write join request to it */
   int join_fd = open(server_name, O_WRONLY);
   if (join_fd == -1) { // check that server exists
      printf("error: server '%s' does not exist\n", server_name);
      return 0;
   }
   write(join_fd, join, sizeof(join_t));
   /* initialize client data */
   strcpy(client->name, username);
   client->to_client_fd = open(to_client_fname, O_RDONLY);
   client->to_server_fd = open(to_server_fname, O_WRONLY);
   strcpy(client->to_client_fname, to_client_fname);
   strcpy(client->to_server_fname, to_server_fname);
   client->data_ready = 0;
   /* initialize simplified terminal I/O */
   char prompt[MAXNAME];
   snprintf(prompt, MAXNAME, "%s>> ", username); // create a prompt string
   simpio_set_prompt(simpio, prompt);         // set the prompt
   simpio_reset(simpio);                      // initialize io
   simpio_noncanonical_terminal_mode();       // set the terminal into a compatible mode
   /* start user & server threads */
   pthread_create(&user_thread, NULL, user_to_server, NULL);
   pthread_create(&server_thread, NULL, server_to_user, NULL);
   /* wait for threads to return */
   pthread_join(user_thread, NULL);
   pthread_join(server_thread, NULL);
   /* restore standard terminal output */
   simpio_reset_terminal_mode();
   return 0;
}
