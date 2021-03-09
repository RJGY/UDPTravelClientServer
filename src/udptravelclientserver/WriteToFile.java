/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udptravelclientserver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import java.util.Timer;
/**
 *
 * @author Alerz
 */
public class WriteToFile extends TimerTask {

    private PrintWriter prn = null;

    public static void main(String args[])
    {
        int interval = 2000; // Print to file every 2 minutes 120000

        Timer tm = new Timer(); // using timer from util package

        //schedule timer to write to file after interval and repeat every interval
        tm.schedule(new WriteToFile(), interval, interval);
    }
    
    //this method is called automatically when the task is scheduled
    @Override
    public void run() {
        try {
            // assign PrintWriter instance to a file names SystemRecord.txt to be opened in append mode
            prn = new PrintWriter(new FileOutputStream(new File("SystemRecord.txt"), true )); /* append = true */
            //Read system date
            Date date = new Date();
            //Date formatter
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            //foramtt date to the given mask
            String dateString = formatter.format(date);
            // Display on the screen
            System.out.println( "Current time of the day using Date - 12 hour format: " + dateString);
            //Write the same contents to the file
            prn.println("Current time of the day using Date - 12 hour format: " + dateString);
            //close the file
            prn.close();
            // Assign printer to null for garbage collection.
            prn = null;
        } // end of try block
        catch(IOException ex) { //exception handling for file handling
            ex.printStackTrace();
        }
    }
}
