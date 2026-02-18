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
        ItemCollection items = ItemCollectionFactory.createItemCollection("User",port);

        Item user = items.getItem(employeeID);

        if (user == null) {
            return null;//employeeID is invalid
        }
        else{
            List<String> details = new ArrayList<>();
            details.add(user.getFieldValue("ALUsed")); // ALUsed
            details.add(user.getFieldValue("ALRemaining")); // ALRemaining  
            details.add(user.getFieldValue("MLUsed")); // MLUsed
            details.add(user.getFieldValue("MLRemaining")); // MLRemaining
            return details;
        }        
    }

    //all the pending leaves of a specific employee in a list of items format
    @Override
    public List<Item> viewUserPendingLA(String employeeID) throws RemoteException {
        
        ItemCollection allLeaves = ItemCollectionFactory.createItemCollection("LeaveApplications",port);
        
        List<String> fields = new ArrayList<>();
        fields.add("UserID");
        fields.add("Status");
        
        List<String> values = new ArrayList<>();
        values.add(employeeID);
        values.add("Pending");
        
        List<Item> ApprovedLeaveEmployee = allLeaves.filter(fields, values);

        if (ApprovedLeaveEmployee.isEmpty()) {
            return null; //there is no pending leave applications for this employee, or the employeeID is invalid
        }

        return ApprovedLeaveEmployee;
    }

    //Creats an Item list of a all the leave application for a specific employee
    @Override   
    public List<Item> viewUserLA(String employeeID) throws RemoteException {
            
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection("LeaveApplications",port);
        
        List<Item> ApprovedLeaveEmployee = AllLeaves.filter("UserID", employeeID);

        if (ApprovedLeaveEmployee.isEmpty()) {
            return null; //there is no leave applications for this employee, or the employeeID is invalid
        }

        return ApprovedLeaveEmployee;
    }

    @Override
    public List<Item> viewLA() throws RemoteException {
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection("LeaveApplications",port);
        
        return AllLeaves.getAll();
    }

    @Override
    public List<Item> viewPendingLA() throws RemoteException {
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection("LeaveApplications",port);
        
        List<Item> ApprovedLeaveEmployee = AllLeaves.filter("Status", "Pending");
        
        if (ApprovedLeaveEmployee.isEmpty()) {
            return null; //there is no pending leave applications
        }   
        
        return ApprovedLeaveEmployee;
    }

    @Override
    public int applyLA(List<String> Details) throws RemoteException {
        
        ItemCollection UserItems = ItemCollectionFactory.createItemCollection("User",port);
        Item user = UserItems.getItem(Details.get(0));
        
        if (user == null) {
            return 0; //Invalid userID
        }
        else if ((Details.get(1).equals("Emergency")) && (user.getFieldValue("MLRemaining").equals("0") || Integer.parseInt(user.getFieldValue("MLRemaining")) < Integer.parseInt(Details.get(4)))){    
                return -1; //Not enough remaining ML
        }
        else if ((Details.get(1).equals("Annual")) && (user.getFieldValue("ALRemaining").equals("0") || Integer.parseInt(user.getFieldValue("ALRemaining")) < Integer.parseInt(Details.get(4)))){    
                return -2; //Not enough remaining AL
        }
        else if (!Details.get(1).equals("Emergency") && !Details.get(1).equals("Annual")){
                return -3; //Invalid Leave Type
        }
        else if (Integer.parseInt(Details.get(4)) <= 0){
                return -4; //Invalid Number of Days
        }
        
        else{
            ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection("LeaveApplications",port);
            //get the last ID and increment it by 1 to get the new ID
            List<String> IDColumn = AllLeaves.getColumn("ID");
            String newID = "001";
            if (!IDColumn.isEmpty()) {
                int lastID = Integer.parseInt(IDColumn.get(IDColumn.size() - 1));
                newID = String.format("%03d", lastID + 1);
            }
            Details.add(0, newID); // Add the new ID at the beginning of the list
            Details.add("Pending"); // Add the status at the end of the list
            Details.add(" "); // Add an empty string for the reason (to be filled in if the leave is rejected)
            
            String[] DetailsArray = Details.toArray(new String[0]);

            AllLeaves.createItem(DetailsArray);

            return 1; //Leave application successfully submitted
        }   

    }

    @Override
    public Boolean deleteLA(String LAID) throws RemoteException {
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection("LeaveApplications",port);
        Item item = AllLeaves.getItem(LAID);
        
        if (item == null) {
            return false; //LAID is invalid
        }else if(!item.getFieldValue("Status").equals("Pending")){
            return false; //Only pending leave applications can be deleted
        }

        return AllLeaves.removeItem(LAID);
    }

     public Boolean updateLA(String LAID, List<String> Details) throws RemoteException {
    
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
