/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;


/**
 *
 * @author luven
 */
import java.util.ArrayList;
import java.util.List;
import Shared_Interfaces.ServerToClient.LeaveInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject; // required functionality to receive remote calls
import Server.DataLogic.ItemCollection;
import Server.DataLogic.ItemCollectionFactory;
import Shared_Interfaces.Item;

// Step 1: Implement the remote interface 
public class Meekail extends UnicastRemoteObject implements LeaveInterface{ // extends for remote object, implements its interface
    private final int port;
    // Step 2: Constructor must throw RemoteException as UnicastRemoteObject constructor does
    
    public Meekail(int port) throws RemoteException {
        super();
        this.port = port;
    }

    //Number of leave used and remaining for a specific employee [ALUsed;ALRemaining;MLUsed;MLRemaining] [0;30;0;30]
    public List<String> viewUserLeaves (String employeeID) throws RemoteException {
        
        return null;
    }

    //all the pending leaves of a specific employee in a list of items format
    @Override
    public List<Item> viewUserPendingLA(String employeeID) throws RemoteException {
        
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection("LeaveApplications",port);
        
        List<String> fields = new ArrayList<>();
        fields.add("UserID");
        fields.add("Status");
        
        List<String> values = new ArrayList<>();
        values.add(employeeID);
        values.add("Pending");
        
        List<Item> ApprovedLeaveEmployee = AllLeaves.filter(fields, values);

        return ApprovedLeaveEmployee;
    }

    //Creats an Item list of a all the leave application for a specific employee
    @Override   
    public List<Item> viewUserLA(String employeeID) throws RemoteException {
        
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection("LeaveApplications",port);
        
        
        List<Item> ApprovedLeaveEmployee = AllLeaves.filter("UserID", employeeID);

        return ApprovedLeaveEmployee;
    }

    @Override

    public Boolean applyLA(List<String> Details) throws RemoteException {
        
        return null;
    }

    public Boolean updateLA(String LAID, List<String> Details) throws RemoteException {
        return null;
    }

    @Override
    public Boolean deleteLA(String LAID) throws RemoteException {
        return null;
    }

    @Override
    public List<Item> viewLA() throws RemoteException {
        return null;
    }

    @Override
    public List<Item> viewPendingLA() throws RemoteException {
        return null;
    }

    @Override
    public Boolean editRemainingLeaves(String LAID, String LeaveType, int LeaveCount) throws RemoteException {
        return null;
    }
    
    @Override
    public Boolean approveLA(String LAID, String Reason) throws RemoteException {
        return null;
    }

    @Override
    public Boolean rejectLA(String LAID, String Reason) throws RemoteException {
        return null;
    }

    @Override
    public String GenerateReport() throws RemoteException {
        return null;
    }


}
