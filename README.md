# Dionysus
A student pub management software in Java

This is a Java software I started writing during the summer of 2011, and which received minor bugfixes and improvements since then. 
It was originally hosted on Sourceforge. 

Current version is 0.1.5, it corrects some bugs from the 0.1 original version.

To use the software, simply unpack the zip file in a directory with write permissions,
and launch the jar file (double-clicking on Windows, or java -jar dionysus-0.1.5.jar on Unices).
Databases are accounts.dat for users, articles.dat for articles and log.dat for transactions.
They are all initially empty.
Tickets are printed as raw text files in the tickets/ subdirectory.

The GUI is "password-protected", the password can be found in the file passe.txt. It is clear text.

The software requires Java 7 or higher. I have only tested it with the Oracle JDK, 
but it should work fine with OpenJDK as well.

For further documentation, please see the wiki. You may also find the source code useful. 

## Modifications

If you find bugs, think about new possible features, or any improvements, please feel free to open an issue or make a pull request. 
