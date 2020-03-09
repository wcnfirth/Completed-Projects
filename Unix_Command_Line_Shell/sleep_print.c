#include <time.h>
#include <stdio.h>
#include <stdlib.h>

// usage: sleep_print secs message
int main(int argc, char *argv[]){

  int secs = atoi(argv[1]);

  struct timespec tm = {
    .tv_nsec = 0,
    .tv_sec  = secs,
  };
  nanosleep(&tm,NULL);
  int i;
  for(i=2; i<argc; i++){
    printf("%s ",argv[i]);
  }
  printf("\n");

  return secs;
}
