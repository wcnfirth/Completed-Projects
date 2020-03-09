CFLAGS = -Wall -g
CC     = gcc $(CFLAGS)

all: bl-client bl-server

bl-client: bl-client.o simpio.o util.o
	$(CC) -g -pthread -o bl-client bl-client.o simpio.o util.o

bl-server: bl-server.o server.o util.o
	$(CC) -g -o bl-server bl-server.o server.o util.o

server.o: server.c blather.h
	$(CC) -c server.c

bl-client.o: bl-client.c blather.h
	$(CC) -c bl-client.c

bl-server.o: bl-server.c blather.h
	$(CC) -c bl-server.c

util.o: util.c blather.h
	$(CC) -c util.c

simpio.o: simpio.c blather.h
	$(CC) -c simpio.c

shell-tests: shell_tests.sh shell_tests_data.sh cat-sig.sh clean-tests
	chmod u+rx shell_tests.sh shell_tests_data.sh normalize.awk filter-semopen-bug.awk \
	cat-sig.sh ./shell_tests.sh

clean-all: clean clean-tests

clean:
	rm -f *.o bl-client bl-server

clean-tests:
	rm -f test-*.log test-*.out test-*.expect test-*.diff test-*.valgrindout
