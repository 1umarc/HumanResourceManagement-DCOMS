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

    // Used By Everyone
    public List<String> viewUserLeaves(String employeeID) throws RemoteException; // Returns Number of leave used and remaining for a specific employee [ALUsed;ALRemaining;MLUsed;MLRemaining] [0;30;0;30]
    public List<Item> viewUserPendingLA(String employeeID) throws RemoteException;
    public List<Item> viewUserLA(String employeeID) throws RemoteException;
    public int applyLA(List<String> Details) throws RemoteException;
    public Boolean updateLA(String LAID, List<String> Details) throws RemoteException;
    public Boolean deleteLA(String LAID) throws RemoteException;

    // Used By HR Staff
    public List<Item> viewLA() throws RemoteException;
    public List<Item> viewPendingLA() throws RemoteException;
    public Boolean editRemainingLeaves(String LAID, String LeaveType, int LeaveCount) throws RemoteException; // Get User from LeaveApplication then Subtract
    public Boolean approveLA(String LAID, String Reason) throws RemoteException;
    public Boolean rejectLA(String LAID, String Reason) throws RemoteException;
    public String GenerateReport() throws RemoteException; // Already Pretty Formated
}


