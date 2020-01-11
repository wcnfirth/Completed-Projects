# The following Makefile has been tested and used exclusively on WSL: Ubuntu
CC     = gcc
CFLAGS = -Wall -c -g -ggdb -pthread
LDFLAGS = -Wall -g -ggdb -pthread

server: server.o httpResponses.o
	$(CC) $(LDFLAGS) -o server server.o httpResponses.o

server.o: src/server.c src/server.h
	$(CC) $(CFLAGS) -c src/server.c

httpResponses.o: src/httpResponses.c src/server.h
	$(CC) $(CFLAGS) -c src/httpResponses.c

clean:
	rm -f *.o server


# THE FOLLOWING HAS BEEN TESTED AND WORKS ON MAC OSX
#
# CFLAGS = -Wall -g
# CC     = gcc $(CFLAGS)
#
# server: server.o httpResponses.o
# 	$(CC) -g -o server server.o httpResponses.o
#
# server.o: src/server.c src/server.h
# 	$(CC) -c src/server.c
#
# httpResponses.o: src/httpResponses.c src/server.h
# 	$(CC) -c src/httpResponses.c
#
# clean:
# 	rm -f *.o server