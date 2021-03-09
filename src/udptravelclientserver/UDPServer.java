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
        Timer tm = new Timer(); // Using timer from util package
        UDPServer server = new UDPServer();
        server.loadCustomers();
        // Schedule timer to write to file after start interval and repeat every interval
        tm.schedule(new WriteToFile(server.customerList), WriteToFile.START_INTERVAL, WriteToFile.INTERVAL);
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
                // Store message.
                String message = new String(request.getData());
                // Print received message.
                System.out.println("Client Request: " + message);
                
                // Error handling for incorrect input regarding too many colons.
                if(message.split(":").length != 3) {
                    System.out.println("Error: Received incorrect input.");
                    continue;
                }
                
                // TODO: FINISH REST OF HANDLING.
                String replyString = "";
                if(server.customerLogin(message)) {
                    
                } else if(server.customerLogin(message) == null) {
                    
                } else {
                    
                }
                
                //packet prepared to transmit
                DatagramPacket replyPacket = new DatagramPacket(replyString.getBytes(), replyString.getBytes().length, request.getAddress(), request.getPort());
                //send packet
                aSocket.send(replyPacket);
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
            System.out.println("Closing Server. Goodbye!");
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
    public Customer searchCustomer(String message) {
        for(Customer customer : customerList) { 
            if(customer.getClientID().equals(message.split(":")[0])) { 
                return customer;
            }
        }
        // Cannot find customer, returning null.
        return null;
    } //  end of function
    
    // Function returns a boolean from the id and pin. If the customer id cant be found,
    // return null so because there is no corresponding customer.
    // If the customer id is found but an incorrect pin is received, 
    public Boolean customerLogin(String message) {
        for(Customer customer : customerList) { 
            if(customer.getClientID().equals(message.split(":")[0])) { 
                return customer.getPinNumber() == Integer.parseInt(message.split(":")[1]);
            }
        }
        // Cannot find customer, returning null.
        return null;
    }
    
    // Function which sets that the customer is getting on.
    public Boolean customerGetOn(String message) {
        for(int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            if(customer.getClientID().equals(message.split(":")[0])) {
                if(!customer.getStatus()) {
                    // Customer can board.
                    customer.setStatus(true);
                    return true;
                } else {
                    // Customer cannot board.
                    // Error handling for unable to board.
                    System.out.println("Error: Customer is already signed in!");
                    return false;
                }
            }
        }
        // Cannot find customer.
        System.out.println("Customer not found.");
        return null;
    }
    
    
    // Function which sets that the customer is getting on.
    public Boolean customerGetOff(String message) {
        for(int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            if(customer.getClientID().equals(message.split(":")[0])) {
                if(customer.getStatus()) {
                    // Customer can board.
                    customer.setStatus(false);
                    // Add one to total trips.
                    customer.increaseTravels();
                    // and calculate their total cost.
                    customer.calculateCost();
                    return true;
                } else {
                    // Customer cannot board.
                    // Error handling for unable to board.
                    System.out.println("Error: Customer is not signed in!");
                    return false;
                }
            }
        }
        // Cannot find customer.
        System.out.println("Customer not found.");
        return null;
    }
} // end of class

