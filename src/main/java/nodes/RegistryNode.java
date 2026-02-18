/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nodes;

// MAIN.JAVA FOR REGISTRY
/**
 *
 * @author luven
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming; // alternative & more direct, dont need to locate registry with port (step 2), just locate directly with name

public class RegistryNode {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Registry registry = LocateRegistry.createRegistry(port); // default RMI port, later seperate rmi registry process is used
            System.out.println("RMI Server is ready.");

            while(true){
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
