/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.main.java.nodes;

/**
 *
 * @author luven
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Naming; // alternative & more direct, dont need to locate registry with port (step 2), just locate directly with name

public class ServerNode {
    public static void main(String[] args) {
        try {
            // Step 1: Create an instance of the implementation
            HelloService helloService = new HelloServiceImpl(); // interface interface new implementation

            int port = Integer.parseInt(args[0]);

            // Step 2: Start (or get) the RMI registry on port 1099
            Registry registry = LocateRegistry.getRegistry("localhost", port); // default RMI port, later seperate rmi registry process is used

            // Step 3: Bind the remote object to a name in the registry
            registry.rebind("HelloService", helloService);  // name of object ref (client looks up), remote object
            
            //Naming.rebind("rmi://localhost/AddService", helloService); --> assumes RMI registry already running on the default port 1099

            System.out.println("RMI Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

