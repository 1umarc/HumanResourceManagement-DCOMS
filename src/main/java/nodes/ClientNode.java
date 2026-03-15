/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nodes;

import Client.ClientUI;
import Shared_Interfaces.ServerToClient.*;
import Shared_Interfaces.RMIBind;

/**
 * ClientNode - BHEL HRM Client Application
 * Retrieves remote interfaces to establishes secure RMI connection, launches the UI
 * @author LUVEN
 */
public class ClientNode
{
    public static void main(String[] args) 
    {
        System.out.println
        (
              "================================="
            + "| BHEL HRM System - Client Node |"
            + "=================================\n"
        );
         
        try 
        {
            // Retrieve command line arguments
            int port = Integer.parseInt(args[0]);  

            // Retrieve remote service interfaces using RMIBind
            System.out.println("* Connecting to RMI services at port: " + port);
            
            AuthInterface authInterface = RMIBind.getService("AuthInterface", port);
            ProfileInterface profileInterface = RMIBind.getService("ProfileInterface", port);
            LeaveInterface leaveInterface = RMIBind.getService("LeaveInterface", port);
            
            if (authInterface == null || profileInterface == null || leaveInterface == null) 
            {
                System.err.println("!! Failed to retrieve one or more services, make sure server is running.");
                System.exit(1); // Exit with 1 = failure
            }
            
            System.out.println("Successfully connected to all services\n");
            
            // Step 3: Launch the client UI
            ClientUI clientUI = new ClientUI(authInterface, profileInterface, leaveInterface);
            clientUI.launchUI();
        } 
        catch (Exception e) 
        {
            System.err.println("!! Unexpected error has occured:");
            e.printStackTrace();
        }
    }
}
