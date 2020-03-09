## Overview
This is a local server and client system that utilizes FIFOs and multithreading in order to allow multiple clients to connect to a single server, and send group messages to all users connected to that same server. When a server instance is created, the user must give the server a name as an argument. When a new client instance is created, the user must give two arguments - a name for the client, and the name of the server that the user wishes to connect to. If a server instance of the given name exists, the client will be connected and will then be able to send messages to all clients that are connected to that same server.

## How to run
A Makefile is included to make compilation simple.
..* navigate to the Unix_Chat_Service directory
..* run 'make'
..* run ./bl-server <server_name> to create a server instance called 'server_name'
..* run ./bl-client <server_name> <client_name> to create a client called 'client-name' and connect it to server 'server_name'
..* messages sent by the client will now be sent to all other clients connected to 'server_name'
Run 'make clean' afterwards to remove build files.