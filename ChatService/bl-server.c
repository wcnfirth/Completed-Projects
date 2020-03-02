#include "blather.h"

server_t server_actual;
server_t* server = &server_actual;

void handle_signals(int signo) { // handles server shutdown
   server_shutdown(server);
   exit(0);
}

int main(int argc, char *argv[]) {
   if (argc != 2) {
      printf("error: enter a single server name\n");
      return 0;
   }
   struct sigaction my_sa = {};
   my_sa.sa_handler = handle_signals;
   sigemptyset(&my_sa.sa_mask); // don't block any other signals during handling
   my_sa.sa_flags = SA_RESTART; // always restart system calls on signals possible
   sigaction(SIGTERM, &my_sa, NULL); // register SIGTERM with given action
   sigaction(SIGINT,  &my_sa, NULL);
   char* server_name = argv[1];
   server_start(server, server_name, DEFAULT_PERMS); // initialize server

   int i;
   while (1) { // main loop
      server_check_sources(server); // update flags
      if (server_join_ready(server)) { // check if a client wants to join
         server_handle_join(server);

      }
      for (i=0; i < server->n_clients; i++) { // handle any messages from clients
         if (server_client_ready(server, i)) {
            server_handle_client(server, i);
         }
      }
   }
   return 0;
}
