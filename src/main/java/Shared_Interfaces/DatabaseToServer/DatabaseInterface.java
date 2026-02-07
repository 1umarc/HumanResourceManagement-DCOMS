
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Shared_Interfaces.DatabaseToServer;

import java.rmi.Remote; // Brings in the Remote interface
import java.rmi.RemoteException; // RMI methods can fail due to network issues, so they must throw 
import java.util.List;

public interface DatabaseInterface extends Remote { // ALL Methods Implementing this should be synchronized?
    public Boolean setFileName(String filePath) throws RemoteException;
    public String getFileName() throws RemoteException;
    public Boolean writeData(List<List<String>> Data) throws RemoteException;
    public Boolean appendData(List<String> Data) throws RemoteException;
    public Boolean updateData(List<String> Data) throws RemoteException;
    public Boolean deleteData(String ID) throws RemoteException;
    public Boolean updateCompositeData(List<String> Data, List<String> Keys) throws RemoteException;
    public Boolean deleteCompositeData(List<String> Keys) throws RemoteException;
    public List<List<String>> getData() throws RemoteException;
    public List<String> getRow(String Key) throws RemoteException;
    public List<String> getCompositeRow(List<String> Keys) throws RemoteException;
    public List<String> getColumn(String Field) throws RemoteException;
    public List<String> getFieldName() throws RemoteException;
    public List<List<String>> FitlerData(List<String> Field, List<String> Value) throws RemoteException;
    public List<List<String>> FitlerData(String Field, List<String> Value) throws RemoteException;
    public List<List<String>> FitlerData(String Field, String Value) throws RemoteException;
}

