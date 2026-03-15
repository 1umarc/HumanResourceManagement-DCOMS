
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Shared_Interfaces.DatabaseToServer;

import java.rmi.Remote; // Brings in the Remote interface
import java.rmi.RemoteException; // RMI methods can fail due to network issues, so they must throw 
import java.util.List;

public interface DatabaseInterface extends Remote { 
    public Boolean setFileName(String filePath) throws RemoteException;
    public String getFileName() throws RemoteException;
    public Boolean writeData(List<List<String>> Items) throws RemoteException;
    public List<List<String>> getData() throws RemoteException;
    public List<String> getFieldName() throws RemoteException;
}

