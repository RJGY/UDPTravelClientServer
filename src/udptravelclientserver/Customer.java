/**
 *
 * Author: Reese Gunardi
 * File Name: Customer.java
 * Date: 15/3/2021
 * Purpose :
 * Customer class to be used in UDPServer.java and WriteToFile
 */
package udptravelclientserver;

import java.io.Serializable;

/**
 *
 * @author Alerz
 */

public class Customer implements Serializable {
    private String clientID;
    private int pinNumber;
    private Boolean status;
    private int numberOfTravels;
    private double totalCost;
    private static final int NUMBER_OF_FREE_TRAVELS = 5;
    
    public Customer() {
        this("", 0, null, 0);
    }
    
    public Customer(String clientID, int pinNumber, Boolean status, int numberOfTravels) {
        this.clientID = clientID;
        this.pinNumber = pinNumber;
        this.status = status;
        this.numberOfTravels = numberOfTravels;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public int getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(int pinNumber) {
        this.pinNumber = pinNumber;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public int getNumberOfTravels() {
        return numberOfTravels;
    }

    public void setNumberOfTravels(int numberOfTravels) {
        this.numberOfTravels = numberOfTravels;
    }
    
    public void calculateCost() {
        totalCost = Math.max((numberOfTravels - NUMBER_OF_FREE_TRAVELS) * 3, 0);
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    public void increaseTravels() {
        this.numberOfTravels++;
    }
    
    @Override
    public String toString() {
        return String.format("Customer [ \n\tCustomer Client ID: %s\n\tCustomer Pin Number: %d\n\tCustomer is currently travelling: %s\n\tNumber of Travels: %d\n\tTotal Cost: $%.2f\n]\n", 
                getClientID(), getPinNumber(), getStatus(), getNumberOfTravels(), getTotalCost());
    }
}
