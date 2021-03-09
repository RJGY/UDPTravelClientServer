/**
 *
 * Author :Reese Gunardi
 * File Name:UDPServer.java
 * Date :20/02/2020
 * Purpose :
 * Java UDP sockets provide a connectionless communication between client and server.(similar to offline text messaging)
 * The server sits in an infinite loop receiving messages from single/multiple clients and replies to them.
 * The client and server uses send and receive methods encapsulated by the socket to communicate with each other.
 * Each datagrampacket is sent and received independently.
 * Run the server program first before running the client program.
 * ******************************************************
 */
package udptravelclientserver;

/**
 *
 * @author Alerz
 */

import java.net.*;
import java.io.*;
import java.util.*;
public class UDPServer
{
    private static final int SERVER_PORT = 6789;
    private static final String MEMBER_TEXT_FILE_NAME = "Member.txt";
    private ArrayList<Customer> customerList = new ArrayList<>();
    
    public static void main(String args[])
    {
        UDPServer server = new UDPServer();
        server.loadCustomers();
    	DatagramSocket aSocket = null;              // DatagramSocket declared and initalised.
	try {
            aSocket = new DatagramSocket(SERVER_PORT);     //DatagramSocket assigned with port number.
            byte[] buffer = new byte[1000];         // byte array
            
            // infinite loop to listen to clients
            System.out.println("UDP Server running...");
            while(true) {
                //packet prepared to receive
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                //receive
                aSocket.receive(request);
                //remove trailling spaces
                System.out.println("Client Request: " + new String(request.getData()).trim());
                //packet prepared to transmit
                DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(),request.getAddress(), request.getPort());
                //send
                aSocket.send(reply);
                //clear buffer for next rquest
                Arrays.fill(buffer, (byte)0);
            }//end of while loop
        }//end of try block
        
        catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        finally {
            if(aSocket != null) aSocket.close();
        }
    } // end of main
    
    public void loadCustomers() {
        try {
            File myObj = new File(MEMBER_TEXT_FILE_NAME);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Customer newCustomer = new Customer(data.split(" ")[0], Integer.parseInt(data.split(" ")[1]), false, 0);
                customerList.add(newCustomer);
                System.out.println(newCustomer);
            } // End while
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    } // end of function
    
    // Function returns a customer from the id.
    public Customer searchCustomers(String customerID) {
        for(Customer customer : customerList) { 
            if(customer.getClientID().equals(customerID)) { 
                return customer;
            }
        }
        // Cannot find customer, returning null.
        return null;
    } //  end of function
    
    // Function returns a boolean from the id and pin. If the customer id cant be found,
    // return null so because there is no corresponding customer.
    // If the customer id is found but an incorrect pin is received, 
    public Boolean customerLogin(String customerID, int pinNumber) {
        for(Customer customer : customerList) { 
            if(customer.getClientID().equals(customerID)) { 
                return customer.getPinNumber() == pinNumber;
            }
        }
        // Cannot find customer, returning null.
        return null;
    }
} // end of class

