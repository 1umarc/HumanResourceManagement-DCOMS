/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.DataLogic;

import Shared_Interfaces.DatabaseToServer.DatabaseInterface;
import Shared_Interfaces.RMIBind;
import java.util.ArrayList;
import java.util.List;
import java.rmi.RemoteException;
import Shared_Interfaces.Item;

/**
 *
 * @author JONATHAN
 */
public class ItemCollectionFactory {
    public static ItemCollection createItemCollection(String Type, int port) throws RemoteException {
        DatabaseInterface Database = RMIBind.getService("DatabaseInterface", port); // Call from RMI

        Database.setFileName(Type);
        
        List<List<String>> ItemDetailList = Database.getData();
        List<String> Fields = Database.getFieldName();
        
        List<Item> ItemList = new ArrayList<>();

        for (List<String> RowData : ItemDetailList) {
            Item newItem = new Item(RowData, Fields, Type);

            ItemList.add(newItem);
        }
        
        return new ItemCollection(ItemList, Database, Type);
    }
}
    
