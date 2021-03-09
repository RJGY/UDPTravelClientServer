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
    public static void main(String args[])
    {

        DatagramSocket aSocket = null;          // DatagramSocket declared and initalised.
        try {
            aSocket = new DatagramSocket();     //DatagramSocket assigned.
            Scanner sa=new Scanner(System.in);  // Scanner instance assigned.
            //host address designated to localhost
            InetAddress aHost = InetAddress.getByName("localhost");
            // byte array used to send and receive a maximum of 1000 characters.
            byte [] m = new byte[1000];
            // port number for the process to listen at.
            int serverPort = 6789;
            String input = ""; //variable to hold user input.
            //keep chatting until terminiated with exit from the user
            while(!input.equalsIgnoreCase("exit")) {
                System.out.println("Enter a message:");
                input = sa.nextLine();
                //convert string to bytes
                m = input.getBytes();
                //packet prepared to transmit
                DatagramPacket request = new DatagramPacket(m,m.length, aHost, serverPort);
                aSocket.send(request);
                //packet prepared to receive
                DatagramPacket reply = new DatagramPacket(m, m.length);aSocket.receive(reply);
                // remove trailling empty spaces from the message of 1000 characters
                System.out.println("Server Response: " + new String(reply.getData()).trim());
            }//end of while
        }//end of try block
        catch (SocketException e) { // Socket exception handling.
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if(aSocket != null) aSocket.close();
        }
    } // end of main
} //  end of class
