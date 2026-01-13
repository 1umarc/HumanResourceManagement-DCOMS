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
import java.rmi.Naming; // to skip step 1

public class DatabaseNode {
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);

            // Step 1: Get reference to the RMI registry on the server
            // If client runs on the same machine as server, use "localhost" --> 1 tier
            Registry registry = LocateRegistry.getRegistry("localhost", port); // variable = "localhost" or IP or hostname


            // Step 2: Look up the remote object by the same name used in server
            HelloService helloService = (HelloService) registry.lookup("HelloService"); // interface interface = (interface)--> () typecasting - interface treated as object
                        // helloService --> STUB, implementing the interface
            // Step 3: Invoke remote method as if local object
            String response = helloService.sayHello("Umapathy"); // networking...
            
            // Step 4: Use/print the result
            System.out.println("Response from server: " + response);
            
//            AddInterface obj = (AddInterface) Naming.lookup("rmi://localhost/AddService"); --> RMI registry running in a separate process, common production practice
//            int result = obj.add(10, 20);
//            System.out.println("Result of addition: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


