# UDPTravelClientServer
Two small java applications which uses a UDP connection to communicate with each other.


# Description
This project uses basic java.net packages to create a UDP connection between the server and client. 
The client sends data, converted to bytes to the server. This data is converted back 
into a string and parsed into a Customer object. This object is then used to toggle the customer's
state in the system, logging them in if they are logged out and logging them in if they were logged out.
This is then sent back to the client, telling them if their login/logout was successful.Starter data
is loaded through text files and is stored in a binary file through the use of ObjectOutputStream and FileOutputStream.

# Usage
1. Run the server.jar first then the client.jar second
2. Follow prompts.


# TODO:
 - Add releases to github.
 - Add word file with annotations.
 - Flesh out description, add a how to use section
 - Build jars.