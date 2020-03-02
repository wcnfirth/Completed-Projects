#include "commando.h"

int main(int argc, char *argv[]) {
  setvbuf(stdout, NULL, _IONBF, 0); // Turn off output buffering
  int echo = 0;
  if (argc > 1) { // Check if echoing is turned on
    if (strcmp(argv[1], "--echo") == 0  || getenv("COMMANDO_ECHO")) {
      echo = 1;
    }
  }
  //Create and allocate empty ctl
  cmdctl_t *ctl = malloc(sizeof(cmdctl_t));
  ctl->size = 0;
  while (1) { //Begin the main loop
    char *buf = malloc(MAX_LINE+1);
    printf("@> ");
    if (fgets(buf, MAX_LINE, stdin) == NULL) { //Read input, check for end of input
      printf("\nEnd of input\n");
      free(buf);
      break;
    }
    if (echo) {printf("%s", buf);} //Apply echo if echoing is turned on
    int ntok;
    char *tokens[ARG_MAX+1];
    parse_into_tokens(buf, tokens, &ntok);
    if (ntok > 0) { //Make sure there was input
      if (strcmp(tokens[0], "help") == 0) { //Start of else-if's for specific commands
        printf("COMMANDO COMMANDS\n"
        "%-18s : show this message\n"
        "%-18s : exit the program\n"
        "%-18s : list all jobs that have been started giving information on each\n"
        "%-18s : pause for the given number of nanseconds and seconds\n"
        "%-18s : print the output for given job number\n"
        "%-18s : print output for all jobs\n"
        "%-18s : wait until the given job number finishes\n"
        "%-18s : wait for all jobs to finish\n"
        "%-18s : non-built-in is run as a job\n", "help", "exit", "list",\
        "pause nanos secs", "output-for int", "output-all", "wait-for int",\
        "wait-all", "command arg1 ...");
      } else if (strcmp(tokens[0], "exit") == 0) {
        free(buf); //Deallocate buffer before exiting the loop
        break;
      } else if (strcmp(tokens[0], "list") == 0) {
        cmdctl_print(ctl);
      } else if (strcmp(tokens[0], "pause") == 0 && ntok == 3) {
        int nanos = atoi(tokens[1]);
        int secs = atoi(tokens[2]);
        pause_for(nanos, secs);
        cmdctl_update_state(ctl, NOBLOCK); //Update state of ctl after pause
      } else if (strcmp(tokens[0], "output-for") == 0 && ntok == 2) {
        int job = atoi(tokens[1]);
        cmd_t *cmd = ctl->cmd[job]; //Format and print output for specific 'job'
        printf("@<<< Output for %s[#%d] (%d bytes):\n", cmd->name, cmd->pid, cmd->output_size);
        printf("----------------------------------------\n");
        cmd_print_output(cmd);
        printf("----------------------------------------\n");
      } else if (strcmp(tokens[0], "output-all") == 0) {
        int i;
        cmd_t *cmd;
        for (i=0; i<ctl->size; i++) { //Format and print output for all jobs
          cmd = ctl->cmd[i];
          printf("@<<< Output for %s[#%d] (%d bytes):\n", cmd->name, cmd->pid, cmd->output_size);
          printf("----------------------------------------\n");
          cmd_print_output(cmd);
          printf("----------------------------------------\n");
        }
      } else if (strcmp(tokens[0], "wait-for") == 0 && ntok == 2) {
        int job = atoi(tokens[1]);
        if (job >= 0 && job < ctl->size) { //Wait for specific job to finish
          cmd_update_state(ctl->cmd[job], DOBLOCK);
        }
      } else if (strcmp(tokens[0], "wait-all") == 0) {
        cmdctl_update_state(ctl, DOBLOCK); //Wait for all jobs to finish
      } else { //Assume input is a command, create & start a cmd then add it to ctl
        cmdctl_add(ctl, cmd_new(tokens));
        cmd_start(ctl->cmd[ctl->size-1]);
      }
    }
    free(buf); //Deallocate buffer
    cmdctl_update_state(ctl, NOBLOCK); //Update all states before next loop
  }
  cmdctl_freeall(ctl); //Deallocate everthing in ctl before exiting
  return 0;
}
