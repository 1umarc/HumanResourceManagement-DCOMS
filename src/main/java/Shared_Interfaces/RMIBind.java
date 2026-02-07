/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Shared_Interfaces;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry; 
import java.rmi.Remote;
import java.rmi.RemoteException; 

public class RMIBind {
    private static Registry getRegistry(int port) throws RemoteException {
        try {
            return LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            return LocateRegistry.getRegistry(port);
        }
    }

    public static Boolean BindService(Remote Service, String ServiceName, int port) {
        
        try {
            Registry registry = getRegistry(port);

            if (registry == null) {
                return false;
            }

            registry.rebind(ServiceName, Service);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <Service extends Remote> Service getService(String ServiceName, int port) {
        try {
            Registry registry = getRegistry(port);

            if (registry == null) {
                return null;
            }

            return (Service) registry.lookup(ServiceName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

