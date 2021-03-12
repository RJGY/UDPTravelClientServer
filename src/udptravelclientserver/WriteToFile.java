/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udptravelclientserver;

import java.io.*;
import java.util.*;
/**
 *
 * @author Alerz
 */
public class WriteToFile extends TimerTask {

    private PrintWriter printer = null;
    private static final String BINARY_FILE_NAME = "Member.dat";
    private ArrayList<Customer> customerList;
    public static final int INTERVAL = 10000; // Print to file every 2 minutes 120000 milliseconds.
    public static final int START_INTERVAL = 1000; // Start after 1 second.
    private Timer tm = new Timer(); // Using timer from util package
    
    public WriteToFile(ArrayList<Customer> customerList) {
        this.customerList = customerList;
    }
    
    // This method is called automatically when the task is scheduled
    @Override
    public void run() {
        try {
            // assign PrintWriter instance to a file names Member.dat to be opened in write mode
            FileOutputStream fos = new FileOutputStream(new File(BINARY_FILE_NAME));
            printer = new PrintWriter(fos, false); /* append = false */
            ObjectOutputStream out;
            out = new ObjectOutputStream(fos);
            for (Customer customer : customerList) {
                out.writeObject(customer);
                out.flush();
            }
            //close the file
            printer.close();
        } // end of try block
        catch(IOException ex) { //exception handling for file handling
            ex.printStackTrace();
        }
    }
}
