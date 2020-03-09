#include "commando.h"

cmd_t *cmd_new(char *argv[]) {
  cmd_t *cmd = malloc(sizeof(*cmd)); //Allocate space for a new cmd
  int i;
  for (i=0; argv[i] != NULL; i++) { //Copy token strings to cmd->argv
    char *str_copy = strdup(argv[i]);
    cmd->argv[i] = str_copy;
  }
  for (i=i; i<ARG_MAX+1; i++) { //Initialize all unused arguments in argv to NULL
    cmd->argv[i] = NULL;
  }
  /*Initialize name field to first argument of argv,
    and all other fields to default values*/
  strcpy(cmd->name, argv[0]);
  cmd->finished = 0;
  snprintf(cmd->str_status, STATUS_LEN, "INIT");
  cmd->pid = -1;
  cmd->status = -1;
  cmd->output = (void *)NULL;
  cmd->output_size = -1;
  return cmd;
}

void cmd_free(cmd_t *cmd) {
  int i;
  for (i=0; cmd->argv[i] != NULL; i++) { //Deallocate all individual arguments
    free(cmd->argv[i]);
  }
  if (cmd->output != NULL) { //Deallocate output (if there is output)
    free(cmd->output);
  }
  free(cmd); //Deallocate cmd
}

void cmd_start(cmd_t *cmd) {
  pipe(cmd->out_pipe); //Create the pipe
  pid_t child_pid = fork(); //Fork the process
  snprintf(cmd->str_status, STATUS_LEN, "RUN"); //Change status to running
  if (child_pid == 0) { //Make sure fork worked correctly
    int stdout_bak = dup(STDOUT_FILENO);
    dup2(cmd->out_pipe[PWRITE], STDOUT_FILENO); //Child output now prints to pipe
    execvp(cmd->name, cmd->argv); //Execute child command
    dup2(stdout_bak, STDOUT_FILENO); //Restore standard out
    close(cmd->out_pipe[PREAD]); //Close non-used end of pipe
  }
  else { //Parent process
    cmd->pid = child_pid;
    close(cmd->out_pipe[PWRITE]);
  }
}

void cmd_fetch_output(cmd_t *cmd) {
  if (cmd->finished == 0) { //Process already terminated
    printf("%s[#%d] not finished yet\n", cmd->name, cmd->pid);
    return;
  }
  int output_size;
  int fd = cmd->out_pipe[PREAD];
  cmd->output = read_all(fd, &output_size); //Read & store output of child process from pipe
  cmd->output_size = output_size;
  close(cmd->out_pipe[PREAD]); //Close the pipe -- done with it
}

void cmd_print_output(cmd_t *cmd) {
  if (cmd->output == NULL) {
    printf("%s[#%d] has no output yet\n", cmd->name, cmd->pid);
  } else { //Print output of finished child cmd
    write(STDOUT_FILENO, cmd->output, cmd->output_size);
  }
}

void cmd_update_state(cmd_t *cmd, int nohang) {
  if (cmd->finished == 1) {return;} //Do nothing if already finished
  int status;
  pid_t pid = waitpid(cmd->pid, &status, nohang); //Either blocking or non-blocking wait
  if (pid == cmd->pid) {
    if (WIFEXITED(status)) { //Process has finished
      cmd->finished = 1;
      cmd->status = WEXITSTATUS(status); //Exit status of process
      snprintf(cmd->str_status, STATUS_LEN, "EXIT(%d)", cmd->status); //Update status
      printf("@!!! %s[#%d]: EXIT(%d)\n", cmd->name, cmd->pid, cmd->status); //Print alert
      cmd_fetch_output(cmd); //cmd is finished, so output is final--fetch it
    }
  }
}

char *read_all(int fd, int *nread) {
  int cur_bufsize = BUFSIZE;
  char *buf = malloc(cur_bufsize+1);
  int bytes = read(fd, buf, cur_bufsize); //Read from pipe into buffer
  int bytes_read = bytes;
  if (bytes >= cur_bufsize) { //If buffer couldn't store all data in fd...
    while (bytes > 0) {
      char *temp_buf = malloc(cur_bufsize+1); //Temp buffer for extra data
      cur_bufsize *= 2;
      bytes = read(fd, temp_buf, cur_bufsize/2); //Read unread data
      if (bytes > 0) { //Make sure data was read
        *(buf+bytes_read) = '\0'; //Null terminate buffers
        *(temp_buf+bytes) = '\0';
        buf = realloc(buf, cur_bufsize+1); //Reallocate buf to be double the size
        bytes_read += bytes;
        strcat(buf, temp_buf); //Concatenate temp_buf to buf
      }
      free(temp_buf); //Deallocate temporary buffer
    }
  }
  *nread = bytes_read; //Pointer to length of buf
  *(buf+bytes_read) = '\0'; //Null terminate buffer
  return buf;
}
