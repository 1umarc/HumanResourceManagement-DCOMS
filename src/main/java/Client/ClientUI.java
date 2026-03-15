/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

// Utility Imports
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

// RMI Imports
import java.rmi.RemoteException;
import Shared_Interfaces.ServerToClient.*;
import Shared_Interfaces.Item;

/**
 * Main Client UI for BHEL HRM System
 * @author LUVEN
 */
public class ClientUI 
{
    // Remote service interfaces (acts as if local due to RMI)
    private final AuthInterface authService;
    private final ProfileInterface profileService;
    private final LeaveInterface leaveService;

    // Session state
    private String currentUser;

    // Scanner for user input
    private final Scanner scanner = new Scanner(System.in);

    // Constructor
    public ClientUI(AuthInterface authService, ProfileInterface profileService, LeaveInterface leaveService) 
    {
        this.authService    = authService;
        this.profileService = profileService;
        this.leaveService   = leaveService;
    }


    // --------------- UI Helper Functions ----------------
    // 1. Display all fields of an Item vertically
    private void displayItemDetail(Item item)
    {
        String[] fields  = item.getFields();
        String[] details = item.getDetails();

        for (int i = 0; (i < fields.length) && (i < details.length); i++)
        {
            System.out.printf("  %-26s: %s%n", fields[i], details[i]); // -26 spaces
        }
    }

    // 2. Get a value from a list by index
    private String getData(List<String> list, int index)
    {
        if (list != null && index < list.size()) return list.get(index);
        return "-";
    }

    // 3. Cut a string to max length for table, . = has been cut
    private String cutString(String s, int max)
    {
        if (s == null) // No string
        {
            return "-";
        }
        if (s.length() <= max) // No need to cut
        {
            return s;
        }
        return s.substring(0, max - 1) + ".";  // substring(start, end), max - 1 as last char is .
    }

    // 4. Hide password with asterisks (SECURE RMI)
    private String hidePassword(String password)
    {
        if (password == null || password.isEmpty()) // No password
        {
            return "-";
        } 
        return "*".repeat(password.length()); // Repeat * for password length
    }

    // 5. Standard divider
    private void divider()
    {
        System.out.println("  ----------------------------------------------------------");
    }

    // 6. Wait for user to press ENTER
    private void waitInput()
    {
        System.out.print("\n  Press ENTER to continue...");
        scanner.nextLine();
    }

    // 7. Slow loading bar for startup
    private void loadingBar()
    {
        try {
            System.out.print(" [");
            
            for (int i = 0; i < 60; i++) 
            {
                Thread.sleep(30);
                System.out.print("=");
            }

            System.out.println("] OK");
            Thread.sleep(300);
        } 
        catch (InterruptedException e) 
        { 
            Thread.currentThread().interrupt(); 
        }
    }

    // 8. Slow character print
    private void slowPrint(String message)
    {
        try 
        {
            for (char c : message.toCharArray()) 
            {
                System.out.print(c);
                Thread.sleep(15);
            }
            System.out.println();
        } 
        catch (InterruptedException e) 
        { 
            Thread.currentThread().interrupt(); 
        }
    }

    // 9. Clear the console
    private void clearConsole()
    {
        System.out.print("\033[H\033[2J"); // \033[H = Move cursor to top, \033[2J = Clear screen
        System.out.flush(); // Flush the output
    }

    // 10. Check integer input
    private int checkIndex(String input)
    {
        try 
        { 
            return Integer.parseInt(input);  // Returns -1 if not a number
        }
        catch (NumberFormatException e) 
        { 
            return -1; 
        }
    }


    // --------------- UI Display Functions ----------------
    // 1. Display a profile returned by viewUserProfile
    private void displayProfileDetail(List<String> profile)
    {
        System.out.printf(" %-14s: %s%n", "User ID",    getData(profile, 0));  // -14s = 14 spaces, n = new line
        System.out.printf(" %-14s: %s%n", "First Name", getData(profile, 1));
        System.out.printf(" %-14s: %s%n", "Last Name",  getData(profile, 2));
        System.out.printf(" %-14s: %s%n", "Role",       getData(profile, 3));
        System.out.printf(" %-14s: %s%n", "Password", hidePassword(getData(profile, 4)));
    }

    // 2. Display leave balance returned by viewUserLeaves
    private void displayLeaveBalance(List<String> leaves)
    {
        System.out.println("  [~~~~ LEAVE BALANCE ~~~~]");
        System.out.printf(" %-20s | Used : %-4s | Remaining : %s%n", "Annual Leave (AL)",  getData(leaves, 0), getData(leaves, 1));
        System.out.printf(" %-20s | Used : %-4s | Remaining : %s%n", "Medical Leave (ML)", getData(leaves, 2), getData(leaves, 3));
    }

    // 3. Display all profiles
    private void displayProfilesTable(List<Item> profiles)
    {
        System.out.println(" +----+------------+--------------------+------------+--------+--------+");
        System.out.printf(" | %-2s | %-10s | %-18s | %-10s | %-6s | %-6s |" + "%n", "#", "UserID", "Name", "Role", "AL Rem", "ML Rem");
        System.out.println(" +----+------------+--------------------+------------+--------+--------+");

        // Loop through all profiles
        for (int i = 0; i < profiles.size(); i++)
        {
            Item p    = profiles.get(i); // list of profiles
            String name = cutString(p.getFieldValue("FirstName") + " " + p.getFieldValue("LastName"), 18);

            System.out.printf(" | %-2d | %-10s | %-18s | %-10s | %-6s | %-6s |" + "%n",
                i + 1,
                cutString(p.getFieldValue("UserID"), 10),
                name,
                cutString(p.getFieldValue("Role"), 10),
                p.getFieldValue("ALRemaining"),
                p.getFieldValue("MLRemaining")
            );
        }
        System.out.println(" +----+------------+--------------------+------------+--------+--------+");
    }

    // 4. Display leave applications
    private void displayLATable(List<Item> leaves)
    {
        System.out.println(" +----+------+------------+------+------------+------+----------+");
        System.out.printf(" | %-2s | %-4s | %-10s | %-4s | %-10s | %-4s | %-8s |" + "%n", "#", "ID", "UserID", "Type", "Start Date", "Days", "Status");
        System.out.println(" +----+------+------------+------+------------+------+----------+");

        // Loop through all leave applications
        for (int i = 0; i < leaves.size(); i++)
        {
            Item l = leaves.get(i);
            System.out.printf(" | %-2d | %-4s | %-10s | %-4s | %-10s | %-4s | %-8s |" + "%n",
                i + 1,
                cutString(l.getFieldValue("LAID"), 4),
                cutString(l.getFieldValue("UserID"), 10),
                cutString(l.getFieldValue("LeaveType"), 4),
                l.getFieldValue("StartDate"),
                l.getFieldValue("NumberOfDays"),
                cutString(l.getFieldValue("Status"), 8)
            );
        }
        System.out.println(" +----+------+------------+------+------------+------+----------+");
    }


    // --------------- Main UI Functions ----------------
    public void launchUI()
    {   
        clearConsole();

        System.out.println
        (
             " +==========================================================+\n"
            + " |                                                          |\n"
            + " |       BHEL HUMAN RESOURCE MANAGEMENT (HRM) SYSTEM        |\n"
            + " |                                                          |\n"
            + " |        Enterprise Distributed RMI Architecture           |\n"
            + " |                                                          |\n"
            + " +==========================================================+\n"
        );

        slowPrint(" Starting client session...");
        loadingBar();

        System.out.println("\n BHEL HRM SYSTEM STATUS : ONLINE");

        boolean running = true;
        while (running)
        {
            running = loginMenu();
        }

        System.out.println(" BHEL HRM SYSTEM STATUS : OFFLINE");
        scanner.close();
    }

    
    // Menu 1: Login
    private boolean loginMenu()
    {
        System.out.println
        (
             "\n +==========================================================+\n"
            + " |           Welcome to BHEL's HRM System!                  |\n"
            + " +==========================================================+"
        );

        System.out.print(" |  Username  : ");
        String username = scanner.nextLine().trim();

        System.out.print(" |  Password  : ");
        String password = scanner.nextLine().trim();

        try
        {
            int result = authService.login(username, password); // RMI Call #1

            switch (result)
            {
                case 1:
                    currentUser = username;
                    System.out.println(" |  \u2705 Login successful. Welcome Staff, " + username + "!\n");
                    HRStaffMenu();
                    return true;

                case 2:
                    currentUser = username;
                    System.out.println(" |  \u2705 Login successful. Welcome Employee, " + username + "!\n");
                    EmployeeMenu();
                    return true;

                case 0:
                    System.out.println(" |  \u274C Login failed. User not found!");
                    break;

                case -1:
                    System.out.println(" |  \u274C Login failed. Incorrect password!");
                    break;

                case -2:
                    System.out.println(" |  \u274C Login failed. Incorrect username!");
                    break;

                default:
                    System.out.println(" |  \u274C Login failed. Unknown error (" + result + ")");
            }
        }
        catch (RemoteException e)
        {
            System.out.println(" |  Client Connection Error: " + e.getMessage());
        }

        System.out.print("\n |  Re-login again? (Y/N) : ");
        String retry = scanner.nextLine().trim();

        System.out.println(" +==========================================================+\n");
        return retry.equalsIgnoreCase("y"); // ignore case
    }    


    // --------------- HR Staff Menus ----------------
    // Menu 2: HR Staff
    private void HRStaffMenu()
    {
        boolean menu = true;
        while (menu)
        {
            System.out.println
            (
                 "\n +==========================================================+\n"
                + " |                    BHEL HR STAFF MENU                    |\n"
                + " +==========================================================+\n"
                + " |  1  |  Employee Profiles                                 |\n"
                + " |  2  |  Leave Applications                                |\n"
                + " |  3  |  Generate Leave Report                             |\n"
                + " |  4  |  Logout                                            |\n"
                + " +==========================================================+"
            );

            System.out.print(" |  Select option > ");
            String choice = scanner.nextLine().trim();

            try
            {
                switch (choice)
                {
                    case "1":
                        employeeProfilesMenu();
                        break;
                    case "2":
                        leaveApplicationsMenu();
                        break;
                    case "3":
                        generateReportMenu();
                        break;
                    case "4":
                        menu = false;
                        break;
                    default:
                        System.out.println("\n |  \u274C Invalid selection. Please try again!");
                }
            }
            catch (RemoteException e)
            {
                System.out.println(" |  Client Connection Error: " + e.getMessage());
            }
        }
    }


    // Menu 2.1: Employee Profiles
    private void employeeProfilesMenu() throws RemoteException
    {
        boolean menu = true;
        while (menu)
        {
            System.out.println
            (
                 "\n +==========================================================+\n"
                + " |                   EMPLOYEE PROFILES                      |\n"
                + " +==========================================================+\n"
                + " |  1  |  View All Profiles                                 |\n"
                + " |  2  |  Manage an Employee  (Edit / Delete / Leave Bal)   |\n"
                + " |  3  |  Create New Employee                               |\n"
                + " |  4  |  Back                                              |\n"
                + " +==========================================================+"
            );

            System.out.print(" |  Select option > ");
            String choice = scanner.nextLine().trim();

            switch (choice)
            {
                case "1":
                    viewAllProfilesFlow();
                    break;
                case "2":
                    manageProfileFlow();
                    break;
                case "3":
                    createEmployeeFlow();
                    break;
                case "4":
                    menu = false;
                    break;
                default:
                    System.out.println("\n |  \u274C Invalid selection. Please try again!");
            }
        }
    }

    // Menu 2.1.1: View All Profiles
    private void viewAllProfilesFlow() throws RemoteException
    {
        List<Item> profiles = profileService.viewAllProfiles(); // RMI Call #2

        System.out.println("\n  [~~~~ VIEW ALL EMPLOYEE PROFILES ~~~~]");

        if (profiles == null || profiles.isEmpty()) // No profiles
        {
            System.out.println("  No employee profiles found!");
            waitInput();
            return;
        }

        displayProfilesTable(profiles);
        System.out.println("  Total : " + profiles.size() + " employee(s)");
        waitInput(); // Wait for user input to continue
    }

    // Menu 2.1.2: Manage an Employee
    private void manageProfileFlow() throws RemoteException
    {
        // Fetch profiles list, refreshes after every action
        List<Item> profiles = profileService.viewAllProfiles(); // RMI Call #3

        if (profiles == null || profiles.isEmpty())
        {
            System.out.println("\n  No employee profiles found!");
            waitInput();
            return;
        }

        // Select employee, outer loop
        boolean select = true;
        while (select)
        {
            System.out.println("\n  [~~~~ MANAGE EMPLOYEE - Select a Profile ~~~~]");

            displayProfilesTable(profiles);
            System.out.println("  Total : " + profiles.size() + " employee(s)");
            System.out.print("\n  Enter profile number to manage (0 to cancel) > ");

            int index = checkIndex(scanner.nextLine().trim());

            if (index == -1)
            { 
                System.out.println("  \u274C Invalid input. Enter a number!"); 
                continue; 
            }
            else if (index == 0)
            { 
                select = false; 
                break;
            }
            else if (index < 1 || index > profiles.size())
            { 
                System.out.println("  \u274C Number out of range. Try again!");
                continue; 
            }


            // Profile detail, inner loop
            String employeeID = profiles.get(index - 1).getFieldValue("UserID"); // -1 because index starts at 1
            boolean manage = true;

            while (manage)
            {
                // Loop with details catching so that edits are reflected
                List<String> profileDetails = profileService.viewUserProfile(employeeID); // RMI Call #4
                List<String> leaveDetails = leaveService.viewUserLeaves(employeeID);      // RMI Call #5

                System.out.println
                (
                     "\n +==========================================================+\n"
                    + " |                   EMPLOYEE DETAILS                       |\n"
                    + " +==========================================================+"
                );
                if (profileDetails != null)
                {
                    displayProfileDetail(profileDetails);
                }
                divider();
                if (leaveDetails != null)
                {
                    displayLeaveBalance(leaveDetails);
                }

                System.out.println
                (
                     "\n +==========================================================+\n"
                    + " |  1  |  Edit Employee Fields                             |\n"
                    + " |  2  |  Delete Employee                                  |\n"
                    + " |  3  |  Edit Leave Balance                               |\n"
                    + " |  4  |  Back                                             |\n"
                    + " +==========================================================+"
                );
                System.out.print(" |  Select option > ");
                String action = scanner.nextLine().trim();

                switch (action)
                {
                    case "1":
                        editProfileFlow(employeeID);
                        break;

                    case "2":
                        boolean deleted = deleteProfileFlow(employeeID);
                        if (deleted)
                        {
                            // Refresh list and exit inner manage loop
                            profiles  = profileService.viewAllProfiles(); // RMI Call #6
                            manage  = false;

                            if (profiles == null || profiles.isEmpty())
                            {
                                select = false; // Exit outer select loop
                            }
                        }
                        break;

                    case "3":
                        editLeaveBalanceFlow(employeeID);
                        break;

                    case "4":
                        manage = false; // Exit inner manage loop
                        break;

                    default:
                        System.out.println("\n |  \u274C Invalid selection. Please try again!");
                }
            }
        }
    }

    // 2.1.2.1 Edit Employee Fields
    private void editProfileFlow(String employeeID) throws RemoteException
    {
        // Editable field index mapping
        String[] editableFields = { "FirstName", "LastName", "Econtact", "Role", "Password" };

        System.out.println("\n  [~~~~ EDIT PROFILE : " + employeeID + " ~~~~]");
        divider();
        System.out.println("  Select field to edit:");
        System.out.println("    1  |  First Name");
        System.out.println("    2  |  Last Name");
        System.out.println("    3  |  Contact Number");
        System.out.println("    4  |  Role");
        System.out.println("    5  |  Password");
        divider();
        System.out.print("  Select field > ");

        int fieldIndex = checkIndex(scanner.nextLine().trim());

        if (fieldIndex < 1 || fieldIndex > editableFields.length)
        {
            System.out.println("\n  \u274C Invalid field selection!");
            waitInput();
            return;
        }

        String field = editableFields[fieldIndex - 1]; // Map index to field name

        System.out.print("  New Value for [" + field + "] : ");
        String value = scanner.nextLine().trim();

        boolean result = profileService.editProfile(employeeID, field, value); // RMI Call #7

        if (result)
            System.out.println("\n  \u2713 Profile updated successfully.");
        else
            System.out.println("\n  \u274C Update failed.  and try again!");

        waitInput();
    }

    // 2.1.2.2 Delete Employee
    private boolean deleteProfileFlow(String employeeID) throws RemoteException // true = successfully deleted
    {
        System.out.println("\n  [~~~~ DELETE PROFILE : " + employeeID + " ~~~~~]");
        System.out.print("  Confirm deletion of '" + employeeID + "'? (Y/N) > ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y"))
        {
            System.out.println("  Deletion cancelled!");
            waitInput();
            return false;
        }

        boolean result = profileService.deleteProfile(employeeID); // RMI Call #8

        if (result)
        {
            System.out.println("  \u2713 Employee '" + employeeID + "' deleted successfully.");
            waitInput();
            return true;
        }
        else
        {
            System.out.println("  \u274C Deletion failed. Please try again!");
            waitInput();
            return false;
        }
    }

    // 2.1.2.3 Edit Leave Balance
    private void editLeaveBalanceFlow(String employeeID) throws RemoteException
    {
        // Editable leave field index mapping
        String[] leaveFields = { "ALRemaining", "ALUsed", "MLRemaining", "MLUsed" };

        System.out.println("\n  [~~~~ EDIT LEAVE BALANCE : " + employeeID + " ~~~~]");
        divider();
        System.out.println("  Select leave field to edit:");
        System.out.println("    1  |  AL Remaining");
        System.out.println("    2  |  AL Used");
        System.out.println("    3  |  ML Remaining");
        System.out.println("    4  |  ML Used");
        divider();
        System.out.print("  Select field > ");

        int fieldIndex = checkIndex(scanner.nextLine().trim());

        if (fieldIndex < 1 || fieldIndex > leaveFields.length)
        {
            System.out.println("\n  \u274C Invalid field selection!");
            waitInput();
            return;
        }

        String leaveField = leaveFields[fieldIndex - 1]; // Map index to field name

        System.out.print("  New Count for [" + leaveField + "] : ");
        String count = scanner.nextLine().trim();

        boolean result = leaveService.editRemainingLeaves(employeeID, leaveField, count); // RMI Call #9

        if (result)
        {
            System.out.println("\n  \u2713 Leave balance updated successfully.");
        }
        else
        {
            System.out.println("\n  \u274C Update failed. Enter a valid number and try again!");
        }
        waitInput();
    }

    // Menu 2.1.3: Create New Employee
    private void createEmployeeFlow() throws RemoteException
    {
        // Role index mapping
        String[] roles = { "HRStaff", "Engineer", "Intern", "CEO" };

        System.out.println("\n  [~~~~ CREATE NEW EMPLOYEE ~~~~]");
        divider();

        System.out.print("  User ID         : ");
        String userID = scanner.nextLine().trim();

        System.out.print("  First Name      : ");
        String firstName = scanner.nextLine().trim();

        System.out.print("  Last Name       : ");
        String lastName = scanner.nextLine().trim();

        System.out.print("  Contact Number  : ");
        String contact = scanner.nextLine().trim();

        System.out.println("  Select Role:");
        System.out.println("    1  |  HR Staff");
        System.out.println("    2  |  Engineer");
        System.out.println("    3  |  Intern");
        System.out.println("    4  |  CEO");
        System.out.print("  Select role > ");

        int roleIndex = checkIndex(scanner.nextLine().trim());

        if (roleIndex < 1 || roleIndex > roles.length)
        {
            System.out.println("\n  \u274C Invalid role selection!");
            waitInput();
            return;
        }

        String role = roles[roleIndex - 1]; // Map index to role name

        System.out.print("  Password        : ");
        String password = scanner.nextLine().trim();

        // Index 5 must be Role to match server-side createNewEmployee logic
        List<String> details = new ArrayList<>();
        details.add(userID);      
        details.add(firstName);     
        details.add(lastName);     
        details.add(contact);
        details.add(role);      
        details.add(password);     

        boolean result = profileService.createNewEmployee(details); // RMI Call #10

        if (result)
        {
          System.out.println("\n  \u2713 Employee '" + userID + "' created successfully!");  
        }
        else
        {
            System.out.println("\n  \u274C Failed, User ID may already exist!");
        }
        waitInput();
    }


    //  Menu 2.2: Leave Applications
    private void leaveApplicationsMenu() throws RemoteException
    {
        boolean menu = true;
        while (menu)
        {
            System.out.println
            (
                 "\n +==========================================================+\n"
                + " |                  LEAVE APPLICATIONS                      |\n"
                + " +==========================================================+\n"
                + " |  1  |  View All Leave Applications                       |\n"
                + " |  2  |  Manage Pending  (View / Approve / Reject)         |\n"
                + " |  3  |  Back                                              |\n"
                + " +==========================================================+"
            );

            System.out.print(" |  Select option > ");
            String choice = scanner.nextLine().trim();

            switch (choice)
            {
                case "1":
                    viewAllLAFlow();
                    break;
                case "2":
                    managePendingLAFlow();
                    break;
                case "3":
                    menu = false;
                    break;
                default:
                    System.out.println("\n |  \u274C Invalid selection. Please try again!");
            }
        }
    }

    // Menu 2.2.1: View All Leave Applications
    private void viewAllLAFlow() throws RemoteException
    {
        List<Item> leaves = leaveService.viewLA(); // RMI Call #11

        System.out.println("\n  [~~~~ ALL LEAVE APPLICATIONS ~~~~]");

        if (leaves == null || leaves.isEmpty())
        {
            System.out.println("  No leave applications recorded.");
            waitInput();
            return;
        }

        displayLATable(leaves);
        System.out.println("  Total : " + leaves.size() + " application(s)");
        waitInput();
    }
    
    // Menu 2.2.2: Manage Pending Leave Applications
    private void managePendingLAFlow() throws RemoteException
    {
        // Similar fetch to 2.1.2 that refreshes
        List<Item> pending = leaveService.viewPendingLA(); // RMI Call #12

        if (pending == null || pending.isEmpty())
        {
            System.out.println("\n  No pending leave applications found.");
            waitInput();
            return;
        }

        // Select application, outer loop
        boolean select = true;
        while (select)
        {
            System.out.println("\n  [~~~~ MANAGE PENDING APPLICATIONS — Select an Application ~~~~]");
            displayLATable(pending);
            System.out.println("  Total : " + pending.size() + " pending application(s)");
            System.out.print("\n  Enter application number to manage (0 to cancel) > ");

            int index = checkIndex(scanner.nextLine().trim());

            if (index == -1)
            { 
                System.out.println("  \u274C Invalid input. Enter a number!"); 
                continue; 
            }
            else if (index == 0)
            { 
                select = false; 
                break;
            }
            else if (index < 1 || index > pending.size())
            { 
                System.out.println("  \u274C Number out of range. Try again!");
                continue; 
            }

            // Application detail, inner loop
            Item   selected = pending.get(index - 1);
            String LAID = selected.getFieldValue("LAID");
            boolean manage = true;

            while (manage)
            {
                System.out.println(
                     "\n  +==========================================================+\n"
                    + "  |              LEAVE APPLICATION DETAILS                   |\n"
                    + "  +==========================================================+"
                );
                displayItemDetail(selected);
                System.out.println(
                     "\n  +==========================================================+\n"
                    + "  |  1  |  Approve Application                              |\n"
                    + "  |  2  |  Reject Application                               |\n"
                    + "  |  3  |  Back                                             |\n"
                    + "  +==========================================================+"
                );
                System.out.print(" |  Select option > ");
                String action = scanner.nextLine().trim();

                switch (action)
                {
                    case "1":
                        boolean approveResult = leaveService.approveLA(LAID); // RMI Call #13
                        if (approveResult)
                        {
                            System.out.println("  \u2713 Application '" + LAID + "' has been approved.");
                        }
                        else
                        {
                            System.out.println("  \u274C Approval failed. Please try again!");
                        }
                        waitInput();

                        // Refresh pending list, exit inner manage loop
                        pending  = leaveService.viewPendingLA(); // RMI Call #14
                        manage = false;

                        if(pending == null || pending.isEmpty())
                        {
                            select = false; // Exit outer select loop
                        }
                        break;

                    case "2":
                        System.out.print("  Reason for Rejection : ");
                        String reason = scanner.nextLine().trim();

                        Boolean rejectResult = leaveService.rejectLA(LAID, reason); // RMI Call #15
                        if (rejectResult)
                        {
                            System.out.println("  \u2713 Application '" + LAID + "' has been rejected.");
                        }
                        else
                        {
                            System.out.println("  \u274C Rejection failed. Please try again!");
                        }
                        waitInput();

                        // Refresh pending list, exit inner manage loop
                        pending  = leaveService.viewPendingLA(); // RMI Call #16
                        manage = false;

                        if(pending == null || pending.isEmpty())
                        {
                            select = false; // Exit outer select loop
                        }
                        break;

                    case "3":
                        manage = false;
                        break;

                    default:
                        System.out.println("\n |  \u274C Invalid selection. Please try again!");
                }
            }
        }
    }


    //  Menu 2.3: Generate Leave Report
    private void generateReportMenu() throws RemoteException 
    {
        // Fetch all profiles so user can select by index
        List<Item> profiles = profileService.viewAllProfiles(); // RMI Call #17

        // Select employee by index
        boolean select = true;
        while (select)
        {
            System.out.println("\n  [~~~~ GENERATE EMPLOYEE REPORT — Select an Employee ~~~~]");
            displayProfilesTable(profiles);
            System.out.println("  Total : " + profiles.size() + " employee(s)");
            System.out.print("\n  Enter employee number to generate report (0 to cancel) > ");

            // Same index check
            int index = checkIndex(scanner.nextLine().trim());
            if (index == -1)
            {
                System.out.println("  \u274C Invalid input. Enter a number!");
                continue;
            }
            else if (index == 0)
            {
                select = false;
                break;
            }
            else if (index < 1 || index > profiles.size())
            {
                System.out.println("  \u274C Number out of range. Try again!");
                continue;
            }

            // Get employee ID to generate report
            String employeeID = profiles.get(index - 1).getFieldValue("UserID");
            System.out.println("\n  [~~~~ GENERATING REPORT FOR : " + employeeID + " ~~~~]");
            loadingBar();

            List<Item> report = leaveService.GenerateReport(employeeID); // RMI Call #18

            // Index 0 is the profile + stats summary
            Item summary = report.get(0);

            System.out.println
            (
                "\n +==========================================================+\n"
                + " |              BHEL HRM - EMPLOYEE YEARLY REPORT           |\n"
                + " +~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+\n"
                + " |                  PROFILE & FAMILY DETAILS                |\n"
                + " +==========================================================+"
            );
            System.out.printf("  %-20s: %s%n", "Employee ID", summary.getFieldValue("UserID"));
            System.out.printf("  %-20s: %s %s%n", "Name", summary.getFieldValue("FirstName"), summary.getFieldValue("LastName"));
            System.out.printf("  %-20s: %s%n", "Role", summary.getFieldValue("Role"));
            System.out.printf("  %-20s: %s%n", "Emer Contact", summary.getFieldValue("E-contact"));
            // %-20s: %s%n = 20 spaces, %s = String, %n = new line

            System.out.println
            (
                "\n +==========================================================+\n"
                + " |                    LEAVE BALANCE                         |\n"
                + " +==========================================================+"
            );
            System.out.printf("  %-20s | Used : %-4s | Remaining : %s%n", "Annual Leave (AL)", summary.getFieldValue("ALUsed"), summary.getFieldValue("ALRemaining"));
            System.out.printf("  %-20s | Used : %-4s | Remaining : %s%n", "Medical Leave (ML)", summary.getFieldValue("MLUsed"), summary.getFieldValue("MLRemaining"));

            // Index 1+ are the leave history Items
            System.out.println
            (
                 "\n +==========================================================+\n"
                + " |                     LEAVE HISTORY                        |\n"
                + " +==========================================================+"
            );
            if (report.size() == 1) // Only the summary item, no leave records
            {
                System.out.println("  No leave applications recorded.");
            }
            else
            {
                // Build leave history list manually from index 1 onwards
                List<Item> leaveHistory = new ArrayList<>();
                for (int i = 1; i < report.size(); i++)
                {
                    leaveHistory.add(report.get(i)); // get = List function
                }
                displayLATable(leaveHistory);
                System.out.println("  Total : " + leaveHistory.size() + " application(s)");
            }

            System.out.println
            (
                "\n +==========================================================+\n"
                + " |                    LEAVE STATISTICS                      |\n"
                + " +==========================================================+"
            );
            System.out.printf("  %-20s: %s%n", "Total Applications", summary.getFieldValue("TotalApplications"));
            System.out.printf("  %-20s: %s%n", "Approved", summary.getFieldValue("Approved"));
            System.out.printf("  %-20s: %s%n", "Rejected", summary.getFieldValue("Rejected"));
            System.out.printf("  %-20s: %s%n", "Pending", summary.getFieldValue("Pending"));
            System.out.printf("  %-20s: %s day(s)%n", "Total Days Taken", summary.getFieldValue("TotalApprovedDays"));
            System.out.printf("  %-20s: %s%n", "Approval Rate", summary.getFieldValue("ApprovalRate"));
            System.out.println(" +==========================================================+");
            waitInput();
        }   
    }


    // --------------- Employee Menus ----------------
    // Menu 3: Employee Menu
    private void EmployeeMenu()
    {
        boolean active = true;
        while (active)
        {
            System.out.println
            (
                 "\n +==========================================================+\n"
                + " |                  BHEL EMPLOYEE MENU                      |\n"
                + " +==========================================================+\n"
                + " |  1  |  My Profile                                        |\n"
                + " |  2  |  My Leave Applications                             |\n"
                + " |  3  |  Logout                                            |\n"
                + " +==========================================================+"
            );

            System.out.print(" |  Select option > ");
            String choice = scanner.nextLine().trim();

            try
            {
                switch (choice)
                {
                    case "1":
                        viewMyProfileFlow();
                        break;
                    case "2":
                        myLeaveMenu();
                        break;
                    case "3":
                        active = false;
                        break;
                    default:
                        System.out.println("\n |  \u274C Invalid selection. Please try again!");
                }
            }
            catch (RemoteException e)
            {
                System.out.println(" |  Client Connection Error: " + e.getMessage());
            }
        }
    }


    // Menu 3.1: View My Profile
    private void viewMyProfileFlow() throws RemoteException
    {
        List<String> profileDetails = profileService.viewUserProfile(currentUser); // RMI Call #19
        List<String> leaveDetails = leaveService.viewUserLeaves(currentUser); // RMI Call #20

        System.out.println(
             "\n +==========================================================+\n"
            + " |                       MY PROFILE                         |\n"
            + " +==========================================================+"
        );

        displayProfileDetail(profileDetails);
        divider();

        if (leaveDetails != null)
        {
            displayLeaveBalance(leaveDetails);
        }

        System.out.println(" +==========================================================+");
        waitInput();
    }


    //  Menu 3.2: My Leave Applications
    private void myLeaveMenu() throws RemoteException
    {
        boolean menu = true;
        while (menu)
        {
            // Fetching every loop for updates
            List<String> leaveBalance = leaveService.viewUserLeaves(currentUser); // RMI Call #21
            List<Item>   allLeaves    = leaveService.viewUserLA(currentUser); // RMI Call #22

            System.out.println
            (
                 "\n +==========================================================+\n"
                + " |                  MY LEAVE APPLICATIONS                   |\n"
                + " +==========================================================+"
            );

            // Leave balance header
            if (leaveBalance != null)
            {
                displayLeaveBalance(leaveBalance);
            }
            divider();

            // Full leave applications
            if (allLeaves == null || allLeaves.isEmpty())
            {
                System.out.println("  No leave applications recorded.");
            }
            else
            {
                displayLATable(allLeaves);
                System.out.println("  Total : " + allLeaves.size() + " application(s)");
            }

            // Mini-menu
            System.out.println
            (
                 "\n +==========================================================+\n"
                + " |  1  |  Manage Pending  (Update / Delete)                 |\n"
                + " |  2  |  Apply for Leave                                   |\n"
                + " |  3  |  Back                                              |\n"
                + " +==========================================================+"
            );
            System.out.print(" |  Select option > ");
            String choice = scanner.nextLine().trim();

            switch (choice)
            {
                case "1":
                    editPendingLAFlow();
                    break;
                case "2":
                    applyLeaveFlow();
                    break;
                case "3":
                    menu = false;
                    break;
                default:
                    System.out.println("\n |  \u274C Invalid selection. Please try again!");
            }
        }
    }

    // Menu 3.2.1: Edit Pending Leave Applications
    private void editPendingLAFlow() throws RemoteException
    {
        List<Item> pending = leaveService.viewUserPendingLA(currentUser); // RMI Call #23

        if (pending == null || pending.isEmpty())
        {
            System.out.println("\n  You have no pending applications to manage.");
            waitInput();
            return;
        }

        // Same outer select loop
        boolean select = true;
        while (select)
        {
            System.out.println("\n  [~~~~ MANAGE MY PENDING APPLICATIONS — Select an Application ~~~~]");
            displayLATable(pending);
            System.out.println("  Total : " + pending.size() + " pending application(s)");
            System.out.print("\n  Enter application number to manage (0 to cancel) > ");

            int index = checkIndex(scanner.nextLine().trim());

            if (index == -1)
            { 
                System.out.println("  \u274C Invalid input. Enter a number!"); 
                continue; 
            }
            else if (index == 0)
            { 
                select = false; 
                break;
            }
            else if (index < 1 || index > pending.size())
            { 
                System.out.println("  \u274C Number out of range. Try again!");
                continue; 
            }

            // Same inner manage loop
            Item   selected = pending.get(index - 1);
            String LAID = selected.getFieldValue("LAID");
            boolean manage = true;
        
            while (manage)
            {
                System.out.println
                (
                     "\n +==========================================================+\n"
                    + " |              LEAVE APPLICATION DETAILS                   |\n"
                    + " +==========================================================+"
                );
                displayItemDetail(selected);
                System.out.println(
                     "\n +==========================================================+\n"
                    + " |  1  |  Update Application                               |\n"
                    + " |  2  |  Delete Application                               |\n"
                    + " |  3  |  Back                                             |\n"
                    + " +==========================================================+"
                );
                System.out.print(" |  Select option > ");
                String action = scanner.nextLine().trim();

                switch (action)
                {
                    case "1":
                        updateLAFlow(LAID);
 
                        pending  = leaveService.viewUserPendingLA(currentUser); // RMI Call #24
                        manage = false;

                        if (pending == null || pending.isEmpty())
                        {
                            select = false;
                        }
                        break;

                    case "2":
                        boolean deleted = deleteLAFlow(LAID);
                        if (deleted)
                        {
                            pending  = leaveService.viewUserPendingLA(currentUser); // RMI Call #25
                            manage = false;

                            if (pending == null || pending.isEmpty()) select = false;
                        }
                        break;

                    case "3":
                        manage = false;
                        break;

                    default:
                        System.out.println("\n |  \u274C Invalid selection. Please try again!");
                }
            }
        }
    }

    // Menu 3.2.1.1 Update a Leave Application
    private void updateLAFlow(String LAID) throws RemoteException
    {
        // Editable field index mapping
        String[] editableFields = { "LeaveType", "ApplicationDate", "StartDate", "NumberOfDays", "ReasonForLeave" };

        System.out.println("\n  [~~~~ UPDATE APPLICATION : " + LAID + " ~~~~]");
        divider();
        System.out.println("  Select field to update:");
        System.out.println("    1  |  Leave Type          (AL / ML)");
        System.out.println("    2  |  Application Date    (DD/MM/YYYY)");
        System.out.println("    3  |  Start Date          (DD/MM/YYYY)");
        System.out.println("    4  |  Number of Days");
        System.out.println("    5  |  Reason for Leave");
        divider();
        System.out.print("  Select field > ");

        int fieldIndex = checkIndex(scanner.nextLine().trim());

        if (fieldIndex < 1 || fieldIndex > editableFields.length)
        {
            System.out.println("\n  \u274C Invalid field selection!");
            waitInput();
            return;
        }

        String field = editableFields[fieldIndex - 1]; // Map index to field name

        System.out.print("  New Value for [" + field + "] : ");
        String value = scanner.nextLine().trim();

        int result = leaveService.updateLA(LAID, field, value); // RMI Call #26

        switch (result)
        {
            case 1:
            {
                System.out.println("\n  \u2713 Application updated successfully.");
                break;
            }

            case 0:
            {
                System.out.println("\n  \u274C Application failed. Invalid LAID!");
                break;
            }

            case -1:
            {
                System.out.println("\n  \u274C Application failed. Only pending applications can be updated!");
                break;
            }

            case -2:
            {
                System.out.println("\n  \u274C Application failed. Invalid leave type. Use AL or ML!");
                break;
            }

            case -3:
            {
                System.out.println("\n  \u274C Application failed. Number of days must be greater than 0!");
                break;
            }

            case -4:
            {
                System.out.println("\n  \u274C Application failed. Invalid application date. Use DD/MM/YYYY!");
                break;
            }

            case -5:
            {
                System.out.println("\n  \u274C Application failed. Invalid start date. Use DD/MM/YYYY!");
                break;
            }

            case -6:
            {
                System.out.println("\n  \u274C Application failed. Start date must be after application date!");
                break;
            }

            default:
            {
                System.out.println("\n  \u274C Application failed. Unknown error (" + result + ")");
                break;
            }
        }
        waitInput();
    }

    // Menu 3.2.1.2 Delete a Leave Application
    private boolean deleteLAFlow(String LAID) throws RemoteException
    {
        System.out.println(
             "\n  [~~~~ DELETE APPLICATION : " + LAID + " ~~~~]\n"
        );
        System.out.print("  Confirm deletion? (Y/N) > ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y"))
        {
            System.out.println("  Deletion cancelled!");
            waitInput();
            return false;
        }

        int result = leaveService.deleteLA(LAID); // RMI Call #27

        switch (result)
        {
            case 1:
            {
                System.out.println("  \u2713 Application '" + LAID + "' deleted successfully.");
                waitInput();
                return true;
            }

            case 0:
            {
                System.out.println("  \u274C Application failed. Invalid LAID!");
                break;
            }

            case -1:
            {
                System.out.println("  \u274C Application failed. Only pending applications can be deleted!");
                break;
            }

            default:
            {
                System.out.println("\n  \u274C Application failed. Unknown error (" + result + ")");
                break;
            }
        }
        waitInput();
        return false;
    }


    // Menu 3.2.2: Apply for Leave
    private void applyLeaveFlow() throws RemoteException
    {
        System.out.println
        (
             "\n  [~~~~ APPLY FOR LEAVE ~~~~]\n"
            + "  Leave Types : AL (Annual Leave)  |  ML (Medical Leave)\n"
            + "  Date Format : DD/MM/YYYY"
        );
        divider();

        System.out.print("  Leave Type        : ");
        String leaveType = scanner.nextLine().trim();

        System.out.print("  Application Date  : ");
        String appDate = scanner.nextLine().trim();

        System.out.print("  Start Date        : ");
        String startDate = scanner.nextLine().trim();

        System.out.print("  Number of Days    : ");
        String numDays = scanner.nextLine().trim();

        System.out.print("  Reason for Leave  : ");
        String reason = scanner.nextLine().trim();

        List<String> details = new ArrayList<>();
        details.add(currentUser);
        details.add(leaveType); 
        details.add(appDate); 
        details.add(startDate);  
        details.add(numDays);    
        details.add(reason);   

        int result = leaveService.applyLA(details); // RMI Call #28

        switch (result)
        {
            case 1:
            {
                System.out.println("\n  \u2713 Leave application submitted successfully.");
                break;
            }

            case 0:
            {
                System.out.println("\n  \u274C Application failed. Invalid User ID!");
                break;
            }

            case -1:
            {
                System.out.println("\n  \u274C Application failed. Insufficient Medical Leave balance!");
                break;
            }

            case -2:
            {
                System.out.println("\n  \u274C Application failed. Insufficient Annual Leave balance!");
                break;
            }

            case -3:
            {
                System.out.println("\n  \u274C Application failed. Invalid leave type. Use AL or ML!");
                break;
            }

            case -4:
            {
                System.out.println("\n  \u274C Application failed. Number of days must be greater than 0!");
                break;
            }

            case -5:
            {
                System.out.println("\n  \u274C Application failed. Invalid number of details provided!");
                break;
            }

            case -6:
            {
                System.out.println("\n  \u274C Application failed. Invalid application date. Use DD/MM/YYYY!");
                break;
            }

            case -7:
            {
                System.out.println("\n  \u274C Application failed. Invalid start date. Use DD/MM/YYYY!");
                break;
            }

            case -8:
            {
                System.out.println("\n  \u274C Application failed. Start date must be after application date!");
                break;
            }

            default:  
            {
                System.out.println("\n  \u274C Application failed. Unknown error (" + result + ")");
                break;
            }
        }
        waitInput();
    }
}