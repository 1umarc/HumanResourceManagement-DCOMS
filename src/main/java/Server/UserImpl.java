/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;


import java.util.ArrayList;
/**
 *
 * @author CHONG
 */
import java.util.List;

import Shared_Interfaces.ServerToClient.ProfileInterface;
import Shared_Interfaces.ServerToClient.AuthInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject; // required functionality to receive remote calls
import Server.DataLogic.ItemCollection;
import Server.DataLogic.ItemCollectionFactory;
import Shared_Interfaces.Item;


// Step 1: Implement the remote interface 
public class UserImpl extends UnicastRemoteObject implements AuthInterface, ProfileInterface  { // extends for remote object, implements its interface
     private final int port;


    // Step 2: Constructor must throw RemoteException as UnicastRemoteObject constructor does
    public UserImpl(int port) throws RemoteException {
        super();
        this.port = port;
    }


    @Override
    public synchronized int login(String username, String password) throws RemoteException {

        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);

        Item user = Users.getItem(username);
        // System.out.println(user);
        if (user == null) {
            return 0;
        }
        else if (user.getFieldValue("UserID").equals(username) && 
                !user.getFieldValue("Password").equals(password)) {
            return -1;
        }
        else if (user.getFieldValue("Password").equals(password) && 
                !user.getFieldValue("UserID").equals(username) ) {
            return -2;
        }

        String role = user.getFieldValue("Role");

        switch (role) {
            case "HRStaff":
                return 1;

            case "Intern":
            case "Engineer":
            case "CEO":
                return 2;

            default:
                return 0;
        }
    }

    @Override
    public synchronized List<String> viewUserProfile(String employeeID) throws RemoteException {

        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);

        Item user = Users.getItem(employeeID);

        if (user == null) {
            return null; // employeeID invalid
        }
        else {
            List<String> details = new ArrayList<>();
            details.add(user.getFieldValue("UserID"));
            details.add(user.getFieldValue("FirstName"));
            details.add(user.getFieldValue("LastName"));
            details.add(user.getFieldValue("Role"));
            details.add(user.getFieldValue("Password"));

            return details;
        }
    }

    @Override
    public synchronized Boolean editProfile(String Username, String Fieldname, String Value) throws RemoteException {

        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);

        if (Username == null || Fieldname == null || Value == null) {
            return false;
        }

        Item user = Users.getItem(Username);

        if (user == null) {
            return false;
        }

        // Validate field exists
        if (!Users.getFieldNames().contains(Fieldname)) {
            return false;
        }

        // Update field value
        Boolean updated = user.setFieldValue(Fieldname, Value);

        if (updated) {
            Users.UpdateFile(); // SAVE TO FILE
            return true;
        }

        return false;

    }

    @Override
    public synchronized List<Item> viewAllProfiles() throws RemoteException {
        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);

        return Users.getAll();
    }

    @Override
    public synchronized Boolean deleteProfile(String employeeID) throws RemoteException {    
        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);
        return Users.removeItem(employeeID);
    }   


    //Intern - 4, 4; HRstaff - 15,15; Engineer - 12,12; CEO - 30,30;  
    @Override
    public Boolean createNewEmployee(List<String> details) throws RemoteException {
        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", port);
        List<String> userId = Users.getColumn("UserID");

        for (String id : userId) {
            if (id.equals(details.get(0))) {
                return false;// user already exists
            }
        }
        String ALremaining;
        String MLremaining;

        String role = details.get(5);  // store once (cleaner)

        
        // Use .equals() for String comparison
        if ("HRStaff".equals(role)) {
            ALremaining = "15";
            MLremaining = "15";
        }
        else if ("Engineer".equals(role)) {
            ALremaining = "12";
            MLremaining = "12";
        }
        else if ("Intern".equals(role)) {
            ALremaining = "4";
            MLremaining = "4";
        }
        else {
            ALremaining = "30";
            MLremaining = "30";
        }

        List<String> newDetails = new ArrayList<>();

        for (String detail : details) {
            newDetails.add(detail);
        }
        // Add leave balances
        newDetails.add("0");          // AL taken
        newDetails.add(ALremaining);  // AL remaining
        newDetails.add("0");          // ML taken
        newDetails.add(MLremaining);  // ML remaining

        String[] newDetailsArray = newDetails.toArray(new String[0]);

        Users.createItem(newDetailsArray);

        for (int i = 0; i < newDetailsArray.length; i++) {
            System.out.println(newDetailsArray[i]);
        }
        //System.out.println(newDetailsArray);

        return true;
    }
}   

