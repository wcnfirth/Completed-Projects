## Overview
This is a simple, Unix-based quasi-command line shell called 'commando'. It is less funtional in many ways from standard shells like bash, but has some properties that distinguish it - such as the ability to recall output for any child process. Unlike bash or other standard Linux shells, the 'jobs' (child processes) that commando starts do not show any output by default and run concurrently with the main process, which immediately returns the command prompt '>@' to the user. It will however print an update if there is a change of state for any jobs running in the background. Whereas most shells are of a waiting structure, commando is non-waiting. It also implements some custom commands, consisting of the following:
..* help                : show list of commando commands
..* exit                : exit the program
..* list                : list all 'jobs' (child processes) that have been started giving information on each
..* pause nanos secs    : pause for the given number of nanoseconds and seconds
..* output-for int      : print the output for the given 'job' number
..* ouput-all           : print output for all jobs
..* wait-for int        : wait until the given job number finishes
..* wait-all            : wait for all jobs to finish
..* command arg1 ...    : non-built-in is run as a job

### How to run
There is a Makefile included, so the instructions are as follows:
..* navigate to the Unix_Command_Line_Shell directory
..* run the command 'make'
..* run the command './commando'
Run 'make clean' afterwards to remove build files.

#Note: This is a Unix-based program. It has been tested and will work on macOS, Ubuntu, and WSL: Ubuntu. It has not been tested on any other Operating Systems, and will not work on Windows unless using WSL.

### Example output
This is an example of what running commando will look like.

~~~#
~~~# ./commando
@> help
COMMANDO COMMANDS
help               : show this message
exit               : exit the program
list               : list all jobs that have been started giving information on each
pause nanos secs   : pause for the given number of nanseconds and seconds
output-for int     : print the output for given job number
output-all         : print output for all jobs
wait-for int       : wait until the given job number finishes
wait-all           : wait for all jobs to finish
command arg1 ...   : non-built-in is run as a job
@> ls
@> list
JOB  #PID      STAT   STR_STAT OUTB COMMAND
0    #1995       -1        RUN   -1 ls 
@!!! ls[#1995]: EXIT(0)
@> output-for 0
@<<< Output for ls[#1995] (357 bytes):
----------------------------------------
3K.txt
GROUP-MEMBERS.txt
Makefile
README.md
actual.std
actual.txt
binary_tests
binary_tests.c
cmd.c
cmd.o
cmdctl.c
cmdctl.o
commando
commando.c
commando.h
commando.o
expect.std
gettysburg.txt
p1-tests
quote.txt
shell_tests.sh
shell_tests_data.sh
sleep_print.c
standardize
stuff
table.sh
test_args
test_args.c
test_utils.c
tests.h
util.c
util.o
valgrind.txt
----------------------------------------
@> cat gettysburg.txt
@> wait-all
@!!! cat[#1996]: EXIT(0)
@> output-for 1
@<<< Output for cat[#1996] (1511 bytes):
----------------------------------------
Four score and seven years ago our fathers brought forth on this
continent, a new nation, conceived in Liberty, and dedicated to the
proposition that all men are created equal.

Now we are engaged in a great civil war, testing whether that nation,
or any nation so conceived and so dedicated, can long endure. We are
met on a great battle-field of that war. We have come to dedicate a
portion of that field, as a final resting place for those who here
gave their lives that that nation might live. It is altogether fitting
and proper that we should do this.

But, in a larger sense, we can not dedicate -- we can not consecrate
-- we can not hallow -- this ground. The brave men, living and dead,
who struggled here, have consecrated it, far above our poor power to
add or detract. The world will little note, nor long remember what we
say here, but it can never forget what they did here. It is for us the
living, rather, to be dedicated here to the unfinished work which they
who fought here have thus far so nobly advanced. It is rather for us
to be here dedicated to the great task remaining before us -- that
from these honored dead we take increased devotion to that cause for
which they gave the last full measure of devotion -- that we here
highly resolve that these dead shall not have died in vain -- that
this nation, under God, shall have a new birth of freedom -- and that
government of the people, by the people, for the people, shall not
perish from the earth.

Abraham Lincoln
November 19, 1863
----------------------------------------
@> list
JOB  #PID      STAT   STR_STAT OUTB COMMAND
0    #1995        0    EXIT(0)  357 ls 
1    #1996        0    EXIT(0) 1511 cat gettysburg.txt 
@> exit
~~~#
