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
public interface LeaveInterface extends Remote { // ALL Methods Implementing this should be synchronized?
    public List<Item> viewLeaves(String employeeID) throws RemoteException;
    public Item applyLeave(Item employeeID, List<String> Details) throws RemoteException;
    public boolean editLeave(Item LeaveApplication) throws RemoteException;
    public boolean deleteLeave(Item LeaveApplication) throws RemoteException;
}


