### HANDLING CLIENT REQUESTS AND SENDING HTTP RESPONSES



Create Java objects to contain:
Banking users with usernames and unique identifiers
3 different types of accounts that can be added to the banking user’s object/profile (keeping in mind that a user could have more than one of the same type of account, like 2 savings accounts) that can hold funds as dollars and cents. 
Methods to (at a minimum)
View account history
View account balances
Sum all balances
Transfer funds between accounts (also not allowing a user to transfer more from an account than they have in the account)
Add accounts (must have a type and a name)
Remove accounts (must have no funds in them at the time)
Logging to show transaction state (could be used for account history)
Think about how you want to store logging data, could be per user or could be mapped using a java.util Class to hold in a RandomAccessFile, or a logfile per user.
Maintaining state in a Java File
Storing objects in a Java File
Once a transaction is complete, write new object to file
Used on startup to bring records into memory


## HOW TO RUN
1. Edit ./conf/httpd.conf to contain correct configuration data
2. Run "make" (Makefile may need to be edited based on user OS)
3. Run ./server
4. Navigate to the URI localhost:[port] from a web browser


#Note: All development and testing was done on macOS, and WSL: Ubuntu