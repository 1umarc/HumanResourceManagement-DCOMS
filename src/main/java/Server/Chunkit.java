/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;


import java.util.ArrayList;
/**
 *
 * @author luven
 */
import java.util.List;
import Shared_Interfaces.ServerToClient.ProfileInterface;
import Shared_Interfaces.ServerToClient.AuthInterface;
import Shared_Interfaces.ServerToClient.LeaveInterface;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject; // required functionality to receive remote calls
import Server.DataLogic.ItemCollection;
import Server.DataLogic.ItemCollectionFactory;
import Shared_Interfaces.Item;


// Step 1: Implement the remote interface 
public class Chunkit extends UnicastRemoteObject implements AuthInterface, ProfileInterface, LeaveInterface { // extends for remote object, implements its interface
     private final int port;


    // Step 2: Constructor must throw RemoteException as UnicastRemoteObject constructor does
    public Chunkit(int port) throws RemoteException {
        super();
        this.port = port;
    }

    @Override
    public int login(String username, String password) throws RemoteException {

        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);

        Item user = Users.getItem(username);

        if (user == null || 
            !user.getFieldValue("Password").equals(password)) {
            return 0;
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

    @Override // for serialization, the last userid that logout will be stored 
    public void logout(String sessionToken) throws RemoteException {
        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);
        
        
    } // Luven's Logout Session Tracking Thing?

    @Override
    public List<String> viewUserProfile(String employeeID) throws RemoteException {

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
    public Boolean editProfile(Item User, String Role) throws RemoteException {

        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);

        if (User == null) {
            return false;
        }

        // Get original user from database
        Item originalUser = Users.getItem(User.getID());

        if (originalUser == null) {
            return false;
        }

        // If HRStaff → allow full edit
        if (Role.equals("HRStaff")) {

            Users.removeItem(originalUser.getID());
            Users.createItem(User.getDetails());
            return true;
        }

        // If Intern or Engineer → restrict editing
        if (Role.equals("Intern") || Role.equals("Engineer")) {

            // Keep restricted fields from original user
            List<String> newDetails = new ArrayList<>();

            newDetails.add(User.getFieldValue("UserID"));
            newDetails.add(User.getFieldValue("FirstName"));
            newDetails.add(User.getFieldValue("LastName"));
            newDetails.add(User.getFieldValue("Password"));

            // Preserve original restricted values
            newDetails.add(originalUser.getFieldValue("Role"));
            newDetails.add(originalUser.getFieldValue("ALUsed"));
            newDetails.add(originalUser.getFieldValue("ALRemaining"));
            newDetails.add(originalUser.getFieldValue("MLUsed"));
            newDetails.add(originalUser.getFieldValue("MLRemaining"));

            Users.removeItem(originalUser.getID());
            Users.createItem(newDetails.toArray(new String[0]));

            return true;
        }

        return false;
    }

    @Override
    public List<Item> viewAllProfiles() throws RemoteException {
        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);

        return Users.getAll();
    }

    @Override
    public Boolean deleteProfile(Item User) throws RemoteException {    
        ItemCollection Users = ItemCollectionFactory.createItemCollection("User", this.port);

        return Users.removeItem(User.getID());
    }   

    @Override
    public List<Item> GenerateReport() throws RemoteException {

        ItemCollection users = ItemCollectionFactory.createItemCollection("User", this.port);
        ItemCollection leaves = ItemCollectionFactory.createItemCollection("LeaveApplication", this.port);

        List<Item> allUsers = users.getAll();
        List<Item> allLeaves = leaves.getAll();

        List<Item> reportList = new ArrayList<>();

        // Define report field names
        List<String> reportFields = new ArrayList<>();
        reportFields.add("UserID");
        reportFields.add("FirstName");
        reportFields.add("LastName");
        reportFields.add("Role");
        reportFields.add("TotalApplications");
        reportFields.add("Approved");
        reportFields.add("Rejected");
        reportFields.add("Pending");
        reportFields.add("TotalApprovedDays");
        reportFields.add("ApprovalRate");

        for (Item user : allUsers) {

            String userID = user.getFieldValue("UserID");

            int total = 0;
            int approved = 0;
            int rejected = 0;
            int pending = 0;
            int approvedDays = 0;

            for (Item leave : allLeaves) {

                if (!leave.getFieldValue("UserID").equals(userID))
                    continue;

                total++;

                String status = leave.getFieldValue("Status");

                switch (status) {
                    case "Approved":
                        approved++;
                        approvedDays += Integer.parseInt(leave.getFieldValue("NumberOfDays"));
                        break;

                    case "Rejected":
                        rejected++;
                        break;

                    case "Pending":
                        pending++;
                        break;
                }
            }

            double approvalRate = total == 0 ? 0 : ((double) approved / total) * 100;

            List<String> reportDetails = new ArrayList<>();
            reportDetails.add(userID);
            reportDetails.add(user.getFieldValue("FirstName"));
            reportDetails.add(user.getFieldValue("LastName"));
            reportDetails.add(user.getFieldValue("Role"));
            reportDetails.add(String.valueOf(total));
            reportDetails.add(String.valueOf(approved));
            reportDetails.add(String.valueOf(rejected));
            reportDetails.add(String.valueOf(pending));
            reportDetails.add(String.valueOf(approvedDays));
            reportDetails.add(String.format("%.2f", approvalRate));

            Item reportItem = new Item(reportDetails, reportFields, "Report");

            reportList.add(reportItem);
        }
        return reportList;
    }

    /////////////////////
    @Override
    public List<String> viewUserLeaves(String employeeID) throws RemoteException {
        return null;
    }

    @Override
    public List<Item> viewUserPendingLA(String employeeID) throws RemoteException {
        return null;
    }

    @Override
    public List<Item> viewUserLA(String employeeID) throws RemoteException {
        return null;
    }

    @Override
    public int applyLA(List<String> Details) throws RemoteException {
        return 0;
    }

    @Override
    public Boolean deleteLA(String LAID) throws RemoteException {
        return null;
    }

    @Override
    public Boolean updateLA(String LAID, List<String> Details) throws RemoteException {
        return null;
    }

///////////////////

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
}
