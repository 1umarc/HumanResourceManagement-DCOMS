/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nodes;

// MAIN.JAVA FOR SERVER
/**
 *
 * @author luven
 */
import Shared_Interfaces.RMIBind;
import Shared_Interfaces.ServerToClient.AuthInterface;
import Shared_Interfaces.ServerToClient.LeaveInterface;
import Shared_Interfaces.ServerToClient.ProfileInterface;

import Server.Chunkit;
import Server.Meekail;


public class ServerNode {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);

            LeaveInterface leaveInterface = new Meekail(port);
            AuthInterface authInterface = new Chunkit(port);
            ProfileInterface profileInterface = new Chunkit(port);

            RMIBind.BindService(leaveInterface, "LeaveInterface", port);
            RMIBind.BindService(authInterface, "AuthInterface", port);
            RMIBind.BindService(profileInterface, "ProfileInterface", port);
            
            while(true){
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

