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
public class PO extends Item {
    public PO(List<String> Details, String Type, DataReader reader, DataWriter writer) {
        super(Details, Type, reader, writer);
    }
    
    @Override
    public List<Item> getUpwardsRelatedItems(String Type) {
        switch (Type) {
            case "Payment" -> {
                return this.getPaymentRelatedToPO();
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public List<Item> getDownwardsRelatedItems(String Type) {
        switch (Type) {
            case "PR" -> {
                return this.getPRRelatedToPO();
            }
            case "Product" -> {
                List<Item> PRList = this.getDownwardsRelatedItems("PR");
                
                List<Item> ProductList = new ArrayList<>();
                
                for (Item currentPR : PRList) {
                    List<Item> ProductRelatedToPR = currentPR.getDownwardsRelatedItems("Product");
                    
                    ProductList.addAll(ProductRelatedToPR);
                }
                
                return ProductList;
            }
            default -> {
                return null;
            }
        }
    }
    
    @Override
    public Boolean addRelatedItem(Item newItem) {
        if (newItem == null || !newItem.getType().equals("PR")) {
            return false;
        }
        
//        A PO can only contain PRs of the same Supplier
        if (!this.getFieldValue("SupplierID").equals(newItem.getFieldValue("SupplierID"))) {
            return false;
        }
        
        
        this.reader.setFileName("PRToPO");
        
        List<String> FileDataFormat = new ArrayList<>();
        
        FileDataFormat.add(this.getID());
        FileDataFormat.add(newItem.getID());
        
//        Checks if the Relationship Already Exists
        if (this.reader.getCompositeRow(FileDataFormat) != null) {
            return false;
        }
         
        this.writer.setFileName("PRToPO");
        
        this.writer.appendData(FileDataFormat);
        
        return true;
    }

    @Override
    public Boolean deleteRelatedItem(Item newItem) {
        if (!newItem.getType().equals("PR")) {
            return false;
        }
        
        this.writer.setFileName("PRToPO");
        
        List<String> FileDataFormat = new ArrayList<>();
        
        FileDataFormat.add(this.getID());
        FileDataFormat.add(newItem.getID());
        
        this.writer.deleteCompositeData(FileDataFormat);
        
        return true;
    }
    
    private List<Item> getPaymentRelatedToPO() {
        String ID = this.getID();
        
//        Reading The Relationship File
        this.reader.setFileName("POToPayment");
        
//        Get All Product/PR/PO Rows Related To The PR/PO/Payment
        List<List<String>> Relationship = this.reader.FitlerData("POID", ID);
        
        if (Relationship.isEmpty()) {
            return null;
        }
        
        List<String> RelatedItemIDList = new ArrayList<>();
        
        for (List<String> r : Relationship) {
            RelatedItemIDList.add(r.get(0)); // get ID of All Related Items 
        }

//        Read Product File And Get All Related Products To PR/Sales
        this.reader.setFileName("Payment");

        List<List<String>> ItemDetailList = this.reader.FitlerData("PaymentID", RelatedItemIDList);

        List<Item> ItemList = new ArrayList<>();

        for (int i = 0; i < ItemDetailList.size(); i++) {
            List<String> RowData = ItemDetailList.get(i);

            ItemFactory Factory = new ItemFactory();

            Item newItem = Factory.createItem(RowData, "Payment");
            ItemList.add(newItem);
        }

        return ItemList;
    }
    
    private List<Item> getPRRelatedToPO() {
        String ID = this.getID();
        
//        Reading and Writing To The Relationship File
        this.reader.setFileName("PRToPO");
        
//        Get All Product/PR/PO Rows Related To The PR/PO/Payment
        List<List<String>> Relationship = this.reader.FitlerData("POID", ID);
        
        if (Relationship.isEmpty()) {
            return null;
        }
        
        List<String> RelatedItemIDList = new ArrayList<>();
        
        for (List<String> r : Relationship) {
            RelatedItemIDList.add(r.get(1)); // get ID of All Related Items 
        }

//        Read Product File And Get All Related Products To PR/Sales
        this.reader.setFileName("PR");

        List<List<String>> ItemDetailList = this.reader.FitlerData("PRID", RelatedItemIDList);

        List<Item> ItemList = new ArrayList<>();

        for (int i = 0; i < ItemDetailList.size(); i++) {
            List<String> RowData = ItemDetailList.get(i);

            ItemFactory Factory = new ItemFactory();

            Item newItem = Factory.createItem(RowData, "PR");
            ItemList.add(newItem);
        }

        return ItemList;
    }

    @Override
    public Boolean CanBeDeleted() {
        List<String> DownwardsRelations = Arrays.asList("PR", "Product");
        
        List<String> UpwardsRelations = Arrays.asList("Payment");
        
        
        for (String Relations : DownwardsRelations) {
            List<Item> RelatedItems = this.getDownwardsRelatedItems(Relations);
            
            if (RelatedItems == null) {
                return true;
            }
            
            if (!RelatedItems.isEmpty()) {
                return false; 
            }
        }
        
        for (String Relations : UpwardsRelations) {
            List<Item> RelatedItems = this.getUpwardsRelatedItems(Relations);
            
            if (RelatedItems == null) {
                continue;
            }
            
            if (!RelatedItems.isEmpty()) {
                return false; 
            }
        }
        
        return !this.getFieldValue("Status").equals("Paid");
    }

}
