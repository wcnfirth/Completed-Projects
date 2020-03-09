## Overview
This is a Java servlet banking application, that must be run using a Java web server. It consists of a set of custom classes that are used to store the data for all of the Users that have created an account with the 'bank', and Java Servlets that perform different user transactions and serve the corresponding HTML to the browser.

Once the user has created an account, they can do any of the following:
..* Create bank accounts (checking, savings, or brokerage).
..* Remove bank accounts.
..* View bank account histories.
..* View bank account balances.
..* Transfer funds between bank accounts.

State is maintained by storing all user account data and transaction data in a Java File, which is then used to bring all records into memory upon startup.

## How to run
1. Install and set up a Java web server, if not already done. All development and testing was performed using [Apache Tomcat 9](https://tomcat.apache.org/download-90.cgi).
2. Download JDK 11 or a newer version, and make sure the enviornment variable JAVA_HOME is set to its filepath. Development and testing was performed using [OpenJDK 11](https://access.redhat.com/documentation/en-us/openjdk/11/html/openjdk_11_for_windows_getting_started_guide/index).
3. Copy and paste JavaServletBankingApp.war into the Java web server. If using Tomcat 9, it will go into the '/webapps' directory.
4. Start the Java web server.
5. Open a web browser and navigate to localhost:[port], where 'port' is the port the Java web server is running through.
6. Navigate to localhost:[port]/JavaServletBankingApp

Note: JavaServletBankingApp.war can also be imported and run in [Eclipse](https://www.eclipse.org/downloads/).