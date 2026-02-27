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
    private String[] fileTypes = {"User","LeaveApplication"};// Step 2: Constructor must throw RemoteException as UnicastRemoteObject constructor does
    
    public Meekail(int port) throws RemoteException {
        super();
        this.port = port;
    }

    //Tested and passed
    //Number of leave used and remaining for a specific employee [ALUsed;ALRemaining;MLUsed;MLRemaining] [0;30;0;30]
    public synchronized List<String> viewUserLeaves (String employeeID) throws RemoteException {
        ItemCollection items = ItemCollectionFactory.createItemCollection(fileTypes[0],port);

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

    //Tested and passed
    //all the pending leaves of a specific employee in a list of items format
    @Override
    public synchronized List<Item> viewUserPendingLA(String employeeID) throws RemoteException {
        
        ItemCollection allLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
        
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

    //Tested and passed
    //Creats an Item list of a all the leave application for a specific employee
    @Override   
    public synchronized List<Item> viewUserLA(String employeeID) throws RemoteException {
            
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
        
        List<Item> ApprovedLeaveEmployee = AllLeaves.filter("UserID", employeeID);

        if (ApprovedLeaveEmployee.isEmpty()) {
            return null; //there is no leave applications for this employee, or the employeeID is invalid
        }

        return ApprovedLeaveEmployee;
    }

    //Tested and passed
    @Override
    public synchronized List<Item> viewLA() throws RemoteException {
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
        
        if (AllLeaves.getAll().isEmpty()) {
            return null; //there is no leave applications
        }

        return AllLeaves.getAll();
    }


    //Tested and passed
    @Override
    public synchronized List<Item> viewPendingLA() throws RemoteException {
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
        
        List<Item> ApprovedLeaveEmployee = AllLeaves.filter("Status", "Pending");
        
        if (ApprovedLeaveEmployee.isEmpty()) {
            return null; //there is no pending leave applications
        }   
        
        return ApprovedLeaveEmployee;
    }
    
    //Tested and passed
    //the details list shoul contain the following details in this order: [UserID, LeaveType, ApplicaitonDate, StartDate, NumberOfDays,ReasonForLeave]
    @Override
    public synchronized int applyLA(List<String> Details) throws RemoteException {
        
        ItemCollection UserItems = ItemCollectionFactory.createItemCollection(fileTypes[0],port);//Lsit of Iteams
        Item user = UserItems.getItem(Details.get(0));
        
        if (user == null) {
            return 0; //Invalid userID
        }
        else if (!Details.get(1).equals("ML") && !Details.get(1).equals("AL")){
            return -3;
        }
        else if ((Details.get(1).equals("ML")) && (user.getFieldValue("MLRemaining").equals("0") || Integer.parseInt(user.getFieldValue("MLRemaining")) < Integer.parseInt(Details.get(4)))){    
                return -1; //Not enough remaining ML
        }
        else if ((Details.get(1).equals("AL")) && (user.getFieldValue("ALRemaining").equals("0") || Integer.parseInt(user.getFieldValue("ALRemaining")) < Integer.parseInt(Details.get(4)))){    
                return -2; //Not enough remaining AL
        }
        else if (Integer.parseInt(Details.get(4)) <= 0){
                return -4; //Invalid Number of Days
        }
        else if (Details.size() != 6){
                return -5; //Invalid number of details
        }
        else if (!Details.get(2).matches("\\d{2}/\\d{2}/\\d{4}")){
                return -6; //Invalid Application Date
        }
        else if (!Details.get(3).matches("\\d{2}/\\d{2}/\\d{4}")){
                return -7; //Invalid Start Date
        }
        else if (!isStartDateAfterApplicationDate(Details.get(2), Details.get(3))){
                    return -8; //Start Date has to be after Application Date
        }
        else{
            ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
            
            //get the last ID and increment it by 1 to get the new ID
            List<String> IDColumn = AllLeaves.getColumn("LAID");
            String newID = "A01";
            if (!IDColumn.isEmpty()) {
                String lastID = IDColumn.get(IDColumn.size() - 1);
                String postfix = lastID.substring(1, lastID.length()); // Get the prefix (e.g., "A")
                int numericPart = Integer.parseInt(postfix); // Get the numeric part of the last ID
                numericPart++; // Increment the numeric part by 1
                newID = String.format("A%02d", numericPart); // Combine the prefix with the new numeric part to form the new ID
                //System.out.println(newID);
            }
            
            Details.add(0, newID); // Add the new ID at the beginning of the list
            Details.add("Pending"); // Add the status at the end of the list
            Details.add("-"); // Add an empty string for the reason (to be filled in if the leave is rejected)
            //System.out.println(Details);
            //System.out.println(Details.size());

            String[] DetailsArray = Details.toArray(new String[0]);

            AllLeaves.createItem(DetailsArray);

            return 1; //Leave application successfully submitted
        }   

    }

    //Test and passed
        @Override
    public synchronized int deleteLA(String LAID) throws RemoteException {
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
        Item item = AllLeaves.getItem(LAID);
        if (item == null) {
            return 0; //LAID is invalid
        }
        else if(!item.getFieldValue("Status").equals("Pending")){
            return -1; //Only pending leave applications can be deleted
        }
        else{
            AllLeaves.removeItem(LAID);
            return 1 ;
        }
    }

    //Tested and passed
    // List<String> = [LeaveType; StartDate; NumberOfDays; ReasonForLeave]
    @Override
    public synchronized int updateLA(String LAID, String field, String Value) throws RemoteException {
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
        Item item = AllLeaves.getItem(LAID);
        if (item == null){
            return 0;
        }  
        
        if (!item.getFieldValue("Status").equals("Pending")){
            return -1;
        } 

        // 2. Format Validation (Check this BEFORE logic)
        if (field.equals("ApplicationDate") || field.equals("StartDate")) {
            if (!Value.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return field.equals("ApplicationDate") ? -4 : -5;
            }
        }

        // 3. Specific Field Logic
        if (field.equals("LeaveType")) {
            if (!Value.equals("ML") && !Value.equals("AL")) return -2;
        } 
        else if (field.equals("NumberOfDays")) {
            try {
                if (Integer.parseInt(Value) <= 0) return -3;
            } catch (NumberFormatException e) {
                return -3; // Not a number
            }
        } 
        else if (field.equals("StartDate")) {
            // Now it is safe to run this because we know the format is XX/XX/XXXX
            String appDate = item.getFieldValue("ApplicationDate");
            if (!isStartDateAfterApplicationDate(appDate, Value)) {
                //System.out.println("Logic failed: Start date is before application date");
                return -6; 
            }
        }
            item.getFieldValue("ApplicationDate");
            //System.out.println("Before Update: " + item.getFieldValue(field));
            //System.out.println(Value);
            //System.out.println(isStartDateAfterApplicationDate(item.getFieldValue("ApplicationDate"), Value));
            item.setFieldValue(field, Value);
            AllLeaves.UpdateFile();
            return 1;
    }

    //Tested and Passed
    @Override
    public synchronized Boolean editRemainingLeaves(String userID, String LeaveType, String LeaveCount) throws RemoteException {
        
        ItemCollection AllProfiles = ItemCollectionFactory.createItemCollection(fileTypes[0],port);
        
        Item item = AllProfiles.getItem(userID);
        
        if (item == null) {
            return false;
        }
        
        item.setFieldValue(LeaveType, LeaveCount);
        //System.out.println(item);
        AllProfiles.UpdateFile();
        return true;
    }
    
    //Tested and passed
    @Override
    public synchronized Boolean approveLA(String LAID) throws RemoteException {
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
        ItemCollection AllProfiles = ItemCollectionFactory.createItemCollection(fileTypes[0],port);

        Item leaveApplication = AllLeaves.getItem(LAID);
        Item userProfile = AllProfiles.getItem(leaveApplication.getFieldValue("UserID"));
        
        if (leaveApplication == null || userProfile == null) {
            return false;
        }

        leaveApplication.setFieldValue("Status", "Approved");
        leaveApplication.setFieldValue("ReasonForRejection", "na");
        AllLeaves.UpdateFile();


        String leaveType = leaveApplication.getFieldValue("LeaveType");
        if (leaveType.equals("AL")) {
            int remainingAL = Integer.parseInt(userProfile.getFieldValue("ALRemaining"));
            int newRemainingAL = remainingAL - Integer.parseInt(leaveApplication.getFieldValue("NumberOfDays"));
            userProfile.setFieldValue("ALRemaining", String.valueOf(newRemainingAL));
            int usedAL = Integer.parseInt(userProfile.getFieldValue("ALUsed"));
            int newUsedAL = usedAL + Integer.parseInt(leaveApplication.getFieldValue("NumberOfDays"));
            userProfile.setFieldValue("ALUsed", String.valueOf(newUsedAL));
            //System.out.println(userProfile);
            AllProfiles.UpdateFile();
        } 
        else {
            int remainingML = Integer.parseInt(userProfile.getFieldValue("MLRemaining"));
            int newRemainingML = remainingML - Integer.parseInt(leaveApplication.getFieldValue("NumberOfDays"));
            userProfile.setFieldValue("MLRemaining", String.valueOf(newRemainingML));
            int usedML = Integer.parseInt(userProfile.getFieldValue("MLUsed"));
            int newUsedML = usedML + Integer.parseInt(leaveApplication.getFieldValue("NumberOfDays"));
            userProfile.setFieldValue("MLUsed", String.valueOf(newUsedML));
            //System.out.println(userProfile);
            AllProfiles.UpdateFile();
        }

        //System.out.println(leaveApplication);
            
        return true;
    }

    //tested and passed
    @Override
    public synchronized Boolean rejectLA(String LAID, String Reason) throws RemoteException {
        
        ItemCollection AllLeaves = ItemCollectionFactory.createItemCollection(fileTypes[1],port);
        Item leaveApplication = AllLeaves.getItem(LAID);
        
        if (leaveApplication == null) {
            return false;
        }
        
        leaveApplication.setFieldValue("Status", "Rejected");
        leaveApplication.setFieldValue("ReasonForRejection", Reason);
        
        AllLeaves.UpdateFile();
        return true;
    }

    //Tested and passed
    @Override
    public synchronized List<Item> GenerateReport() throws RemoteException {
        return null;
    }
     
    private Boolean isStartDateAfterApplicationDate(String applicationDate, String startDate) {
        String[] appDateParts = applicationDate.split("/");
        String[] startDateParts = startDate.split("/");

        int appDay = Integer.parseInt(appDateParts[0]);
        int appMonth = Integer.parseInt(appDateParts[1]);
        int appYear = Integer.parseInt(appDateParts[2]);

        int startDay = Integer.parseInt(startDateParts[0]);
        int startMonth = Integer.parseInt(startDateParts[1]);
        int startYear = Integer.parseInt(startDateParts[2]);

        if (startYear > appYear) {
            return true;
        } 
        else if (startYear == appYear) {
            if (startMonth > appMonth) {
                return true;
            } 
            else if (startMonth == appMonth) {
                return startDay > appDay;
            }
        }
        return false;
    }

}
