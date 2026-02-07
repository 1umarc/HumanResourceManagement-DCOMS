
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Shared_Interfaces.ServerToClient;

import java.rmi.Remote; // Brings in the Remote interface
import java.rmi.RemoteException; // RMI methods can fail due to network issues, so they must throw 
import Shared_Interfaces.Item;

// Step 1: Define the remote interface
public interface AuthInterface extends Remote { // ALL Methods Implementing this should be synchronized?
    public Item login(String username, String password) throws RemoteException;
    public void logout(String sessionToken) throws RemoteException; // Luven's Logout Session Tracking Thing?
}

