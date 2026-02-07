/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nodes;

// MAIN.JAVA FOR DATABASE
/**
 *
 * @author luven
 */
import Shared_Interfaces.RMIBind;
import Shared_Interfaces.DatabaseToServer.DatabaseInterface;
import Database.FileHandler;

public class DatabaseNode {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);

            DatabaseInterface Database = new FileHandler();

            RMIBind.BindService(Database, "DatabaseInterface", port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


