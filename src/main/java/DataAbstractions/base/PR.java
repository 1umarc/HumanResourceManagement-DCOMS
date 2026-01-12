/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAbstractions.base;

import DataAbstractions.Item;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author JONATHAN
 */
public class PR extends Item {

    public PR(List<String> Details, String Type, DataReader reader, DataWriter writer) {
        super(Details, Type, reader, writer);
    }

    @Override
    public List<Item> getUpwardsRelatedItems(String Type) {
        switch (Type) {
            case "PO" -> {
                return this.getPORelatedToPR();
            }
            case "Payment" -> {
                List<Item> POList = this.getUpwardsRelatedItems("PO");
                
                List<Item> PaymentList = new ArrayList<>();
                
                for (Item currentPO : POList) {
                    List<Item> PaymentRelatedToPO = currentPO.getUpwardsRelatedItems("Payment");
                    
                    PaymentList.addAll(PaymentRelatedToPO);
                }
                
                return PaymentList;
            }

            default -> {
                return null;
            }
        }
    }

    @Override
    public List<Item> getDownwardsRelatedItems(String Type) {
        switch (Type) {
            case "Product" -> {
                return this.getProductsRelatedToPR();
            }
            default -> {
                return null;
            }
        }
    }
    
    @Override
    public Boolean addRelatedItem(Item newItem) {
        if (newItem == null || !newItem.getType().equals("Product")) {
            return false;
        }
        
//        A PR can only contain Products of the same Supplier
        if (!this.getFieldValue("SupplierID").equals(newItem.getFieldValue("SupplierID"))) {
            return false;
        }
        
        this.reader.setFileName("ProductToPR");
        
        List<String> FileDataFormat = new ArrayList<>();
        
        FileDataFormat.add(this.getID());
        FileDataFormat.add(newItem.getID());
        FileDataFormat.add(newItem.getFieldValue("Quantity")); // Quantity of Item
        
//        Checks if the Relationship Already Exists
        if (this.reader.getCompositeRow(FileDataFormat) != null) {
            return false;
        }
        
        this.writer.setFileName("ProductToPR");
        
        this.writer.appendData(FileDataFormat);
        
        return true;
    }

    @Override
    public Boolean deleteRelatedItem(Item newItem) {
        if (newItem == null || !newItem.getType().equals("Product")) {
            return false;
        }
        
        this.writer.setFileName("ProductToPR");
        
        List<String> FileDataFormat = new ArrayList<>();
        
        FileDataFormat.add(this.getID());
        FileDataFormat.add(newItem.getID());
        FileDataFormat.add(newItem.getDetails()[0]); // Quantity of Item
        
        this.writer.deleteCompositeData(FileDataFormat);
        
        return true;
    }
    
    private List<Item> getPORelatedToPR() {
        String ID = this.getID();
        
//        Reading and Writing To The Relationship File
        this.reader.setFileName("PRToPO");
        
//        Get All Product/PR/PO Rows Related To The PR/PO/Payment
        List<List<String>> Relationship = this.reader.FitlerData("PRID", ID);
        
        if (Relationship.isEmpty()) {
            return null;
        }
        
        List<String> RelatedItemIDList = new ArrayList<>();
        
        for (List<String> r : Relationship) {
            RelatedItemIDList.add(r.get(0)); // get ID of All Related Items 
        }

//        Read Product File And Get All Related Products To PR/Sales
        this.reader.setFileName("PO");

        List<List<String>> ItemDetailList = this.reader.FitlerData("POID", RelatedItemIDList);

        List<Item> ItemList = new ArrayList<>();

        for (int i = 0; i < ItemDetailList.size(); i++) {
            List<String> RowData = ItemDetailList.get(i);

            ItemFactory Factory = new ItemFactory();

            Item newItem = Factory.createItem(RowData, "PO");
            ItemList.add(newItem);
        }

        return ItemList;
    }
    
    private List<Item> getProductsRelatedToPR() {
        String ID = this.getID();
        
//        Reading and Writing To The Relationship File
        this.reader.setFileName("ProductToPR");
        
//        Get All Product/PR/PO Rows Related To The PR/PO/Payment
        List<List<String>> Relationship = this.reader.FitlerData("PRID", ID);
        
        if (Relationship.isEmpty()) {
            return null;
        }
        
        List<String> RelatedItemIDList = new ArrayList<>();
        
        for (List<String> r : Relationship) {
            RelatedItemIDList.add(r.get(1)); // get ID of All Related Items 
        }
//        Get The Quantity Of Product Sold/Bought
        List<String> ItemQuantities = this.reader.getColumn("Quantity");

//        Read Product File And Get All Related Products To PR/Sales
        this.reader.setFileName("Product");
        
        int WantedFieldIndex = this.reader.getFieldName().indexOf("Quantity");

        List<List<String>> ItemDetailList = this.reader.FitlerData("ProductID", RelatedItemIDList);

        List<Item> ItemList = new ArrayList<>();

        for (int i = 0; i < ItemDetailList.size(); i++) {
            List<String> RowData = ItemDetailList.get(i);

//            Replace The Quantity Of The Product In Inventory To The Quantity Sold/Bought
            RowData.remove(WantedFieldIndex);
            RowData.add(WantedFieldIndex, ItemQuantities.get(i));
            
            ItemFactory Factory = new ItemFactory();

            Item newItem = Factory.createItem(RowData, "Product");
            ItemList.add(newItem);
        }

        return ItemList;
    }

    @Override
    public Boolean CanBeDeleted() {
        List<String> UpwardsRelations = Arrays.asList("PO");
        
        for (String Relations : UpwardsRelations) {
            List<Item> RelatedItems = this.getUpwardsRelatedItems(Relations);
            
            if (RelatedItems == null) {
                continue;
            }
            
            if (!RelatedItems.isEmpty()) {
                return false; 
            }
        }
        
        return true;
    }

}
