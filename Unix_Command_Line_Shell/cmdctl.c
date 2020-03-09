#include "commando.h"

void cmdctl_print(cmdctl_t *ctl) { //Format and print data for each job
  printf("%-4s #%-8s %4s %10s %4s %s\n", "JOB", "PID", "STAT",
        "STR_STAT", "OUTB", "COMMAND");
  int i,j;
  for (i=0; i<ctl->size; i++) {
    cmd_t *cmd = ctl->cmd[i];
    printf("%-4d #%-8d %4d %10s %4d ", i, cmd->pid, cmd->status,
          cmd->str_status, cmd->output_size);
    for (j=0; cmd->argv[j] != NULL; j++) {
      printf("%s ", cmd->argv[j]);
    }
    printf("\n");
  }
}

void cmdctl_add(cmdctl_t *ctl, cmd_t *cmd) {
  if (ctl->size >= MAX_CHILD) {return;}
  ctl->cmd[ctl->size] = cmd; //Add new child to end of ctl array
  ctl->size += 1; //Increment size counter for ctl
}

void cmdctl_update_state(cmdctl_t *ctl, int nohang) {
  int i;
  for (i=0; i<ctl->size; i++) { //Update state of each cmd in ctl
    cmd_t *cmd = ctl->cmd[i];
    cmd_update_state(cmd, nohang);
  }
}

void cmdctl_freeall(cmdctl_t *ctl) {
  int i;
  for (i=0; i<ctl->size; i++) { //Deallocate each cmd in ctl
    cmd_free(ctl->cmd[i]);
  }
  free(ctl); //Deallocate ctl
}
