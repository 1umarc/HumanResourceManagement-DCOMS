/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.DataLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import Shared_Interfaces.DatabaseToServer.DatabaseInterface;
import java.rmi.RemoteException;
import Shared_Interfaces.Item;

/**
 *
 * @author JONATHAN
 */
public class ItemCollection {
    private final List<String> FieldNames;
    private final List<Item> ItemList;
    private final DatabaseInterface Database;
    private final String Type;

    public ItemCollection(List<Item> newItemList, DatabaseInterface newDatabase, String Type) throws RemoteException {
        this.ItemList = newItemList;
        this.Database = newDatabase;
        this.FieldNames = this.Database.getFieldName();
        this.Type = Type;
    }
    
    @Override
    public String toString() {
        String Output = "";
        
        int count = 1;
        for (Item CurrentItem : this.ItemList) {
            Output += String.format("%d\n%s\n", count++, CurrentItem);
        } 
        return Output;
    }
    
    public void UpdateFile() throws RemoteException {
        List<List<String>> Data = new ArrayList<>(); // Turn Item Into Writable Format
        
        this.ItemList.forEach((Item currentItem) -> {
            Data.add(Arrays.asList(currentItem.getDetails()));
        });
        
        this.Database.setFileName(this.Type);
        
        this.Database.writeData(Data);
    }
    
    public int getCollectionSize() {
        return this.ItemList.size();
    }
    
    public Boolean CheckItemInCollection(Item ItemInstance) {
        for (Item CurrentItem : this.ItemList) {
            Boolean SameDetails = Arrays.equals(CurrentItem.getDetails(), ItemInstance.getDetails());
            
            if (SameDetails) {
                return true;
            }
        }
        
        return false;
    }
    
    public Boolean CheckItemInCollection(String[] Details) {
        for (Item CurrentItem : this.ItemList) {
            Boolean SameDetails = Arrays.equals(CurrentItem.getDetails(), Details);
            
            if (SameDetails) {
                return true;
            }
        }
        
        return false;
    }
    
    private int getItemIndex(Item ItemInstance) {
        String ItemInstanceID = ItemInstance.getID(); // Get Item ID
        
        for (int i = 0; i < this.ItemList.size(); i++) {
            String CurrentItemID = this.ItemList.get(i).getID(); // get Item ID
            
            Boolean SameID = CurrentItemID.equals(ItemInstanceID);
            
            if (SameID) { // Matches IDs
                return i;
            }
        }
        
        return -1;
    }
    
    private int getItemIndex(String ID) {
        for (int i = 0; i < this.ItemList.size(); i++) {
            String CurrentItemID = this.ItemList.get(i).getID();
            
            Boolean SameID = CurrentItemID.equals(ID);
            
            if (SameID) {
                return i;
            }
        }
        
        return -1;
    }
    
    public Item createItem(String[] Details) throws RemoteException {
//        Check If Data Size Matches

        Boolean SameSize = Details.length == this.FieldNames.size();
        Boolean ItemFound = this.getItemIndex(Details[0]) != -1;

        if (!SameSize || ItemFound) {
            return null;
        }
        
        List<String> MutableListOfDetails = new ArrayList<>(Arrays.asList(Details));
        
        Item newItem = new Item(MutableListOfDetails, this.FieldNames, this.Type);
        
        this.ItemList.add(newItem);
        this.UpdateFile();
        
        return newItem;
    }
    
    public Boolean removeItem(Item ItemInstance) throws RemoteException {
        int index = this.getItemIndex(ItemInstance);

        if (index == -1) {
            return false;
        }
        
        Item currentItem = this.ItemList.get(index);
        
//        If Item Has Any Relationship or Item has Certain Status Then No Delete
        if (!this.CanBeDeleted(currentItem.getID())) { 
            return false;
        }
        
        this.ItemList.remove(index);
        this.UpdateFile();

        return true;
    }
    
    public Boolean removeItem(String ID) throws RemoteException {
        int index = this.getItemIndex(ID);

        if (index == -1) {
            return false;
        }
        
        Item currentItem = this.ItemList.get(index);
        
//        If Item Has Any Relationship or Item has Certain Status Then No Delete
        if (!this.CanBeDeleted(currentItem.getID())) { 
            return false;
        }
        
        this.ItemList.remove(index);
        this.UpdateFile();

        return true;
    }
    
    public List<String> getColumn(String Field) {
        List<String> ColumnData = new ArrayList<>();
        
        for (Item CurrentItem : this.ItemList) {
            String CurrentItemFieldValue = CurrentItem.getFieldValue(Field);
            ColumnData.add(CurrentItemFieldValue);
        }
        
        return ColumnData;
    }
    
    public List<String> getFieldNames() {
        return new ArrayList<>(this.FieldNames);
    }
    
    public List<Item> getAll() {
        return new ArrayList<>(this.ItemList);
    }

    public Item getItem(String ID) {
        int index = this.getItemIndex(ID);
        
        if (index == -1) {
            return null;
        }
        
        return this.ItemList.get(index);
    }

    private Boolean CanBeDeleted(String ID) throws RemoteException {
        switch (this.Type) {
            case "User" -> {
                List<Item> ItemList = new ArrayList<>();
                        
                this.Database.setFileName("LeaveApplication");
                
                List<List<String>> ItemDetailList = this.Database.FitlerData("UserID", ID);
                
                return ItemDetailList.isEmpty();
            }
            case "LeaveApplication" -> {
                return true;
            }
            default -> {
                return true;
            }
        }
    }
    
    public List<Item> filter(List<String> Fields, List<String> Values) { // Field and Values Match 1 to 1 -> index 0 with index 0, etc
        List<Integer> indexes = new ArrayList<>();
        
//        Creates a List of n 1s where n is the size of the collection
        indexes.addAll(Collections.nCopies(this.getCollectionSize(), 1));
        
//        If Value in the Corresponding Field and Row is Different, Then Remove
        for (int i = 0; i < Fields.size() && i < Values.size(); i++) {
            List<String> ColumnData = this.getColumn(Fields.get(i));
            
            String FieldValue = Values.get(i);
            
            for (int j = 0; j < ColumnData.size(); j++) {
                if (indexes.get(j) == 0) { // If Index is Already Invalid then continue
                    continue;
                }
                
//                Checks if The value of that row in the Corresponding Field is Equal to the Field Value
                if (!ColumnData.get(j).equals(FieldValue)) { 
                    indexes.remove(j);
                    indexes.add(j, 0);
                }
            } 
        }
        
        List<Item> FilteredItemList = new ArrayList<>();
        
        for (int i = 0; i < indexes.size(); i++) {
            if (indexes.get(i) == 1) {
                FilteredItemList.add(this.ItemList.get(i));
            }
        }
        
        return FilteredItemList;
    }   
    
    public List<Item> filter(String Field, List<String> Values) {
        List<Item> FilteredItemList = new ArrayList<>();
        
        List<String> ColumnData = this.getColumn(Field);
        
        
        for (int i = 0; i < ColumnData.size(); i++) {
            if (Values.contains(ColumnData.get(i))) {
                FilteredItemList.add(this.ItemList.get(i));
            }
        } 

        return FilteredItemList;
    }
    
    
    public List<Item> filter(String Field, String Value) {
        List<Item> FilteredItemList = new ArrayList<>();
        
        List<String> ColumnData = this.getColumn(Field);
        
        for (int i = 0; i < ColumnData.size(); i++) {
            if (Value.equals(ColumnData.get(i))) {
                FilteredItemList.add(this.ItemList.get(i));
            }
        } 

        return FilteredItemList;
    }
    
    public List<Item> filter(Predicate<Item> lambda) {
        List<Item> SortedList = new ArrayList<>(this.ItemList);
        SortedList.stream().filter(lambda).collect(Collectors.toList());
        return SortedList;
    }
    
    public List<Item> getSortedItems(Comparator<Item> lambda) {
        List<Item> SortedList = new ArrayList<>(this.ItemList);
        SortedList.sort(lambda);
        return SortedList;
    }
    
    public Boolean isEmpty() {
        return this.ItemList.isEmpty();
    }
    
}
