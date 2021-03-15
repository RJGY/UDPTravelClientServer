/**
 *
 * Author: Reese Gunardi
 * File Name: UDPServer.java
 * Date: 15/3/2021
 * Purpose:
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
            byte[] buffer = new byte[1000];         // Byte array
            
            // infinite loop to listen to clients
            System.out.println("UDP Server running...");
            System.out.printf("%s\t%s\t%s\n", "Customer ID", "No. Travels", "Total Cost");
            while(true) {
                //packet prepared to receive
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                //receive
                aSocket.receive(request);
                // Store message.
                String message = new String(request.getData()).trim();
                // Error handling for incorrect input regarding too many colons.
                if(message.split(":").length != 3) {
                    System.out.println("Error - Received incorrect input.");
                    continue;
                } // end if
                
                String replyString = "";
                if(server.customerLogin(message)) {
                    if(message.split(":")[2].trim().equalsIgnoreCase("in")) {
                        if(server.customerGetOn(message)) {
                            // successfully boarded customer
                            replyString = "Success - Welcome";
                        } else {
                            // customer already on board
                            replyString = "Error - Customer is already signed in.";
                        } // end if
                    }
                    else if(message.split(":")[2].trim().equalsIgnoreCase("out")) {
                        if(server.customerGetOff(message)) {
                            // successfully unloaded customer
                            replyString = "Success - Have a good day";
                        } else {
                            // customer was not on board.
                            replyString = "Error - Customer is already signed out.";
                        }
                    }
                } else {
                    // customerLogin returned false.
                    // Need to tell if there is no customer or if incorrect pin.
                    if (server.checkCustomerID(message)) {
                        // Customer id exists.
                        replyString = "Error - Incorrect Pin Number.";
                    } else {
                        replyString = "Error - Incorrect Customer ID.";
                    }
                } // end if
                
                byte[] m = replyString.getBytes();
                //packet prepared to transmit
                DatagramPacket replyPacket = new DatagramPacket(m, m.length, request.getAddress(), request.getPort());
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
    
    // Function that loads customer from text file.
    public void loadCustomers() {
        try {
            File myObj = new File(MEMBER_TEXT_FILE_NAME);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                Customer newCustomer = new Customer(data.split(" ")[0], Integer.parseInt(data.split(" ")[1]), false, 0);
                customerList.add(newCustomer);
            } // End while
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    } // end of function
    
    // Function returns a boolean from the id and pin. If the customer id cant be found,
    // return null so because there is no corresponding customer.
    // If the customer id is found but an incorrect pin is received, 
    public Boolean customerLogin(String message) {
        for(Customer customer : customerList) { 
            if(customer.getClientID().equalsIgnoreCase(message.split(":")[0])) {
                try {
                    return customer.getPinNumber() == Integer.parseInt(message.split(":")[1]);
                } catch (java.lang.NumberFormatException e) {
                    return false;
                }
            }
        }
        // Cannot find customer, returning false.
        return false;
    }
    
    public boolean checkCustomerID(String message) {
        // Code below can do the whole function in one line.
        // return customerList.stream().anyMatch((customer) -> (customer.getClientID().equalsIgnoreCase(message.split(":")[0])));
        for(Customer customer : customerList) {
            if(customer.getClientID().equalsIgnoreCase(message.split(":")[0])) { 
                return true;
            }
        }
        return false;
    } 
    
    // Function which sets that the customer is getting on.
    // returns true if successful
    // returns false if not
    public Boolean customerGetOn(String message) {
        for(int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            if(customer.getClientID().equalsIgnoreCase(message.split(":")[0])) {
                if(!customer.getStatus()) {
                    // Customer can board.
                    customer.setStatus(true);
                    return true;
                } else {
                    // Customer cannot board.
                    // Error handling for unable to board.
                    return false;
                }
            }
        }
        // Cannot find customer.
        return null;
    } // End of function.
    
    
    // Function which sets that the customer is getting on.
    // returns true if successful
    // returns false if not
    public Boolean customerGetOff(String message) {
        for(int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            if(customer.getClientID().equalsIgnoreCase(message.split(":")[0])) {
                if(customer.getStatus()) {
                    // Customer can get off. 
                    // Customer board status toggled off.
                    customer.setStatus(false);
                    // Add one to customers total trips.
                    customer.increaseTravels();
                    // Calculate total cost.
                    customer.calculateCost();
                    // Print to server.
                    System.out.printf("%s\t\t%d\t\t$%.2f\n", customer.getClientID(), customer.getNumberOfTravels(), customer.getTotalCost());
                    return true;
                } else {
                    // Customer cannot board.
                    // Return false.
                    return false;
                }
            }
        }
        // Cannot find customer. Return null
        return null;
    } // end of function.
} // end of class

