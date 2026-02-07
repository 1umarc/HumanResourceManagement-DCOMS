/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;


/**
 *
 * @author luven
 */
import Shared_Interfaces.ServerToClient.LeaveInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject; // required functionality to receive remote calls

// Step 1: Implement the remote interface 
public class Meekail extends UnicastRemoteObject { // extends for remote object, implements its interface

    // Step 2: Constructor must throw RemoteException as UnicastRemoteObject constructor does
    protected Meekail() throws RemoteException {
        super();
    }

}
