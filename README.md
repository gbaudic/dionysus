# Dionysus  [![Build Status](https://travis-ci.org/gbaudic/dionysus.svg?branch=master)](https://travis-ci.org/gbaudic/dionysus)
A student pub management software in Java

This is a Java software I started writing during the summer of 2011, and which received minor bugfixes and improvements since then. 
It was originally hosted on Sourceforge, now I only use Sourceforge for the binary releases. 
The initial aim was to produce a management software to make the daily operation of the student pub more convenient. 

Current version is 0.1.5, it corrects some bugs from the 0.1 original version.

To use the software, simply unpack the zip file in a directory with write permissions,
and launch the jar file (double-clicking on Windows, or `java -jar dionysus-0.1.5.jar` on Unices).
Databases are accounts.dat for users, articles.dat for articles and log.dat for transactions.
They are all initially empty.
Tickets are printed as raw text files in the tickets/ subdirectory.

The GUI is "password-protected", the password and logins can be found in the file `logins.txt`. It is clear text.

The software requires Java 8 or higher. It works with the Oracle JDK and OpenJDK.  

For further documentation, please see the wiki. You may also find the source code useful. 

## Features

- User list, useful to keep track of members 
- Stock management and notifications when an item is almost sold out
- Transaction log
- Multiple payment methods support
- Multiple vendors support

## Intended audience

Dionysus is for you if:
- your shop is limited in size and does not receive many customers
- you do not have complex offers, rebates
- you use a single computer for sales
- you want a lightweight piece of software which works out-of-the-box with very limited dependencies (only the JRE is required)

Dionysus is NOT (yet!) for you if:
- you need specific hardware support, such as USB barcode scanners
- your shop experiences heavy customer traffic
- you have several POS terminals
- you need a loyalty program for your customers
- you have several hundreds articles in store
- you need a stable, bug-free, supported system
- you need interoperability with other software

**Special note for users in France**: since January 1, 2018, all PoS software in use needs to have been approved by the fiscal administration. As you can imagine, this one is not approved, and is unlikely to be so, at the very least because it just does not support VAT. 

## Modifications

If you find bugs, think about new possible features, or any improvements, please feel free to open an issue or make a pull request. 
