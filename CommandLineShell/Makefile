CFLAGS = -Wall -g
CC     = gcc $(CFLAGS)

commando: cmd.o cmdctl.o commando.o util.o
	$(CC) -g -o commando cmd.o cmdctl.o commando.o util.o

cmd.o: cmd.c commando.h
	$(CC) -c cmd.c

cmdctl.o: cmdctl.c commando.h
	$(CC) -c cmdctl.c

commando.o: commando.c commando.h
	$(CC) -c commando.c

util.o: util.c commando.h
	$(CC) -c util.c

binary_tests.o : binary_tests.c commando.h tests.h
	$(CC) -c $<

test_utils.o : test_utils.c tests.h
	$(CC) -c $<

binary_tests : binary_tests.o cmd.o cmdctl.o test_utils.o
	$(CC) -o $@ $^

test-binary : binary_tests
	valgrind ./binary_tests

test-shell : commando
	./shell_tests.sh

clean:
	rm -f *.o ./commando
