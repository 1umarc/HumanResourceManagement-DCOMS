
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
    public List<Item> viewProfile(String employeeID) throws RemoteException;
    public List<Item> viewProfiles() throws RemoteException;
    public boolean editProfile(Item User) throws RemoteException;
    public boolean deleteProfile(Item User) throws RemoteException;
}

