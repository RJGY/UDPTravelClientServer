/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udptravelclientserver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;
/**
 *
 * @author Alerz
 */
public class WriteToFile extends TimerTask {

    private PrintWriter printer = null;
    private String binaryFileName;
    private ArrayList<Customer> customerList;
    private ObjectOutputStream out;
    private static final int INTERVAL = 10000; // Print to file every 2 minutes 120000 milliseconds.
    private static final int START_INTERVAL = 1000; // Start after 1 second.
    private Timer tm = new Timer(); // Using timer from util package
    
    public WriteToFile(ArrayList<Customer> customerList) {
        this.customerList = customerList;
        
        // Schedule timer to write to file after start interval and repeat every interval
        tm.schedule(new WriteToFile(customerList), START_INTERVAL, INTERVAL);
    }
    
    //this method is called automatically when the task is scheduled
    @Override
    public void run() {
        try {
            // assign PrintWriter instance to a file names SystemRecord.txt to be opened in append mode
            printer = new PrintWriter(new FileOutputStream(new File("SystemRecord.txt"), true )); /* append = true */
            //Read system date
            Date date = new Date();
            //Date formatter
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            //foramtt date to the given mask
            String dateString = formatter.format(date);
            // Display on the screen
            System.out.println( "Current time of the day using Date - 12 hour format: " + dateString);
            //Write the same contents to the file
            printer.println("Current time of the day using Date - 12 hour format: " + dateString);
            //close the file
            printer.close();
            // Assign printer to null for garbage collection.
            printer = null;
        } // end of try block
        catch(IOException ex) { //exception handling for file handling
            ex.printStackTrace();
        }
    }
}
