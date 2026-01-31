/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package src.main.java.nodes;

import java.rmi.Remote; // Brings in the Remote interface
import java.rmi.RemoteException; // RMI methods can fail due to network issues, so they must throw 

// Step 1: Define the remote interface
public interface Luven extends Remote {
    // Step 2: Declare remote methods
    String sayHello(String name) throws RemoteException;
    
    //int add(int a, int b) throws RemoteException;
}

