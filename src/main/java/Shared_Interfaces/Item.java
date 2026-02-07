/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Shared_Interfaces;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;

/**
 *
 * @author JONATHAN
 */
public class Item implements Serializable {
    protected List<String> Fields;
    protected List<String> Details;
    protected String Type;
    
    public Item (List<String> Details, List<String> Fields, String Type) {
        this.Type = Type;
        this.Fields = Fields;
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
}
