/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;


/**
 *
 * @author luven
 */
import Shared_Interfaces.ServerToClient.ProfileInterface;
import Shared_Interfaces.ServerToClient.AuthInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject; // required functionality to receive remote calls
import Server.DataLogic.ItemCollection;
import Server.DataLogic.ItemCollectionFactory;
import Shared_Interfaces.Item;

// Step 1: Implement the remote interface 
public class Chunkit extends UnicastRemoteObject implements AuthInterface { // extends for remote object, implements its interface
     private final int port;


    // Step 2: Constructor must throw RemoteException as UnicastRemoteObject constructor does
    public Chunkit(int port) throws RemoteException {
        super();
        this.port = port;
    }

    @Override
    public Item login(String username, String password) throws RemoteException {
        ItemCollection Items = ItemCollectionFactory.createItemCollection("User", this.port);

        System.out.println(Items);

        return null;
    }

    @Override
    public void logout(String sessionToken) throws RemoteException {

    } // Luven's Logout Session Tracking Thing?

}
