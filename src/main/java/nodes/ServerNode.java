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
import Server.Chunkit;


public class ServerNode {
    public static void main(String[] args) {
        try {
            Chunkit trial = new Chunkit();

            trial.login("a", "b");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

