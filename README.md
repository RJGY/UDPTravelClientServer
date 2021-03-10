# UDPTravelClientServer
 Small Java app which uses UDP to communicate with servers and clients.

# Description
Uses basic java.net packages to create a UDP connection between the server and client. 
The client sends data, converted to bytes to the server. This data is converted back 
into a string and parsed into an object. This object is then used to toggle the customer's
state in the system. This is then sent back to the client, telling them if their 
login/logout was successful. Starter data is loaded through text files and is stored 
in a binary file through the use of Scanner and FileOutputStream.


