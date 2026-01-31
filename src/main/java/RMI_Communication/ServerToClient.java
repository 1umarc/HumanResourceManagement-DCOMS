/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package RMI_Communication;

/**
 *
 * @author luven
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject; // required functionality to receive remote calls

// Step 1: Implement the remote interface 
public class ServerToClient extends UnicastRemoteObject implements Meekail { // extends for remote object, implements its interface

    // Step 2: Constructor must throw RemoteException as UnicastRemoteObject constructor does
    protected ServerToClient() throws RemoteException {
        super();
    }
    
//    protected AddImplementation() throws RemoteException {
//        super();
//    }


    // Step 3: Implement the remote method
    @Override
    public String sayHello(String name) throws RemoteException {
        return "Hello, " + name + "! This is an RMI server.";
        
//    @Override
//    public int add(int a, int b) throws RemoteException {
//        return a + b;
     
    }
}
