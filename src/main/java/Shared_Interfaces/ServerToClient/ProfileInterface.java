
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Shared_Interfaces.ServerToClient;

import java.rmi.Remote; // Brings in the Remote interface
import java.rmi.RemoteException; // RMI methods can fail due to network issues, so they must throw 
import Shared_Interfaces.Item;
import java.util.List;

// Step 1: Define the remote interface
public interface ProfileInterface extends Remote { // ALL Methods Implementing this should be synchronized?
    // Used By Everyone
    public List<Item> viewUserProfile(String employeeID) throws RemoteException;
    public Boolean editProfile(Item User) throws RemoteException;

    // Used By HR Staff
    public List<Item> viewAllProfiles() throws RemoteException;
    public Boolean deleteProfile(Item User) throws RemoteException;
}

