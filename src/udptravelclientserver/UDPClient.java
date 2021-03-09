/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udptravelclientserver;

/*
 *
 * @author Alerz
 *
 * Author: Reese Gunardi
 * File Name: UDPClient.java
 * Date: 20/02/2020
 * Purpose:
 * Java UDP sockets provide a connectionless communication between client and server.(similar to offline text messaging)
 * The client and server uses send and receive methods encapsulated by the socket to communicate with each other.
 * Each datagrampacket is sent and received independently.
 * The client ends the coversation by typing exit.
 * Multiple clients can communicate with the server.
 * Run the server program first before running the client program.
 * ******************************************************
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class UDPClient
{
    // port number for the process to listen at.
    private static final int SERVER_PORT = 6789;
    
    // host address designated to localhost
    private static final String SERVER_ADDRESS = "localhost";
    
    public static void main(String args[])
    {
        DatagramSocket aSocket = null;          // DatagramSocket declared and initalised.
        try {
            aSocket = new DatagramSocket();     //DatagramSocket assigned.
            Scanner scanner = new Scanner(System.in);  // Scanner instance assigned.
            
            InetAddress aHost = InetAddress.getByName(SERVER_ADDRESS);
            // byte array used to send and receive a maximum of 1000 characters.
            byte [] m = new byte[1000];
            // variable to hold user input.
            String input = ""; 
            // keep chatting until terminiated with 3 from the user
            while(true) {
                System.out.println("****Travel Kiosk****\n\t1:IN\n\t2:OUT\n\t3:EXIT\nEnter: ");
                input = scanner.nextLine();
                // Check if user breaking out of loop
                if(input.equalsIgnoreCase("3")) {
                    break;
                }
                // Variable to mutate and hold multiple inputs from user.
                StringBuilder multiInput = new StringBuilder();
                // Purge any extra characters in the scanner.
                input = scanner.nextLine(); 
                // Prompt user.
                System.out.println("Customer Client ID:");
                input = scanner.nextLine(); 
                // Mutates the string, removing all white space around the input,
                // then adding our own space to easily seperate the inputs.
                multiInput.append(input.trim()).append(" ");
                // Purge any extra characters in the scanner.
                input = scanner.nextLine(); 
                // Prompt user.
                System.out.println("Customer Pin Number:");
                input = scanner.nextLine(); 
                // Appends the pin to the end of the 
                multiInput.append(input.trim());
                // Convert string to bytes
                m = multiInput.toString().getBytes();
                // Packet prepared to transmit
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, SERVER_PORT);
                // Transmit packet.
                aSocket.send(request);
                // Prepare packet to receive
                DatagramPacket reply = new DatagramPacket(m, m.length);
                // Recieve reply.
                aSocket.receive(reply);
                // remove trailling empty spaces from the message of 1000 characters
                System.out.println("Server Response: " + new String(reply.getData()).trim());
            }//end of while
        }//end of try block
        catch (SocketException e) { // Socket exception handling.
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            // Close connection to server.
            if(aSocket != null) aSocket.close();
            System.out.println("Exitting System, Goodbye!");
        }
    } // end of main
} //  end of class
