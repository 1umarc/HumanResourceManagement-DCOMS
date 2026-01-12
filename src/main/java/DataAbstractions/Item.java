/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAbstractions;

//import DataAbstractions.base.DataReader;
//import DataAbstractions.base.DataWriter;
//import DataAbstractions.base.FileHandler;
//import java.util.ArrayList;
import DataAbstractions.base.DataWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import DataAbstractions.base.DataReader;

/**
 *
 * @author JONATHAN
 */
public abstract class Item {
    protected List<String> Fields;
    protected List<String> Details;
    protected String Type;
    protected DataWriter writer;
    protected DataReader reader;
    
    public Item (List<String> Details, String Type, DataReader reader, DataWriter writer) {
        this.Type = Type;
        this.reader = reader;
        this.writer = writer;
        this.Fields = this.reader.getFieldName();
        this.Details = Details;
    }
    
    @Override
    public String toString() {
        String Buffer = "";
        
        for (int i = 0; i < this.Fields.size() && i < this.Details.size(); i++) {
            Buffer += String.format("%s: %s\n", this.Fields.get(i), this.Details.get(i));
        }
        
        return Buffer;
    }
    
    public String getID() {
        if (this.Details.isEmpty()) {
            return null;
        }
        
        return this.Details.get(0);
    }
    
    public String getType() {
        return this.Type;
    }

    public String[] getFields() {
        return this.Fields.toArray(String[]::new);
    }
    
    public String[] getDetails() {
        return this.Details.toArray(String[]::new);
    }
    
    public Boolean setDetails(String[] newDetails) {
        Boolean SameLength = newDetails.length == this.Fields.size();
        Boolean SameID = this.getID().equals(newDetails[0]);
        
        if (!SameLength || !SameID) {
            return false;
        }
        
        this.Details = new ArrayList<>(Arrays.asList(newDetails)); // Creates a Mutable ArrayList With the Details
        return true;
    }
    
    public String getFieldValue(String FieldName) {
        int index = this.Fields.indexOf(FieldName);
        
        if (index == -1) { // Field Name Not Found
            return null;
        }
        
        return this.Details.get(index);
    }
    
    public Boolean setFieldValue(String FieldName, String Value) {
        int index = this.Fields.indexOf(FieldName);
        
        if (index == -1) { // Field Name Not Found
            return false;
        }
        
        this.Details.remove(index);
        this.Details.add(index, Value);
        return true;
    }
    
    public abstract List<Item> getUpwardsRelatedItems(String Type); // Get which PO the PR is in
    
    public abstract List<Item> getDownwardsRelatedItems(String Type); // Get List of PR of a PO

    public abstract Boolean addRelatedItem(Item newItem);

    public abstract Boolean deleteRelatedItem(Item newItem);
    
    public abstract Boolean CanBeDeleted();
    
}
