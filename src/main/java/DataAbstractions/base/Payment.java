/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAbstractions.base;

import DataAbstractions.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JONATHAN
 */
public class Payment extends Item {

    public Payment(List<String> Details, String Type, DataReader reader, DataWriter writer) {
        super(Details, Type, reader, writer);
    }

    @Override
    public List<Item> getUpwardsRelatedItems(String Type) {
        return null;
    }

    @Override
    public List<Item> getDownwardsRelatedItems(String Type) {
        switch (Type) {
            case "PO" -> {
                return this.getPORelatedToPayment();
            }
            case "PR" -> {
                List<Item> POList = this.getDownwardsRelatedItems("PO");
                
                List<Item> PRList = new ArrayList<>();
                
                for (Item currentPO : POList) {
                    List<Item> PRRelatedToPO = currentPO.getDownwardsRelatedItems("PR");
                    
                    PRList.addAll(PRRelatedToPO);
                }
                
                return PRList;
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
        if (newItem == null || !newItem.getType().equals("PO")) {
            return false;
        }
        
//        A Payment can only contain POs of the same Supplier
        if (!this.getFieldValue("SupplierID").equals(newItem.getFieldValue("SupplierID"))) {
            return false;
        }
        
        this.writer.setFileName("POToPayment");
        
        List<String> FileDataFormat = new ArrayList<>();
        
        FileDataFormat.add(this.getID());
        FileDataFormat.add(newItem.getID());
        
        this.writer.appendData(FileDataFormat);
        
        return true;
    }

    @Override
    public Boolean deleteRelatedItem(Item newItem) {
        if (newItem == null || !newItem.getType().equals("PO")) {
            return false;
        }
        
        this.reader.setFileName("POToPayment");
        
        List<String> FileDataFormat = new ArrayList<>();
        
        FileDataFormat.add(this.getID());
        FileDataFormat.add(newItem.getID());
        
//        Checks if the Relationship Already Exists
        if (this.reader.getCompositeRow(FileDataFormat) != null) {
            return false;
        }

        this.writer.setFileName("POToPayment");
        
        this.writer.deleteCompositeData(FileDataFormat);
        
        return true;
    }
    
    private List<Item> getPORelatedToPayment() {
        String ID = this.getID();
        
//        Reading and Writing To The Relationship File
        this.reader.setFileName("POToPayment");
        
//        Get All Product/PR/PO Rows Related To The PR/PO/Payment
        List<List<String>> Relationship = this.reader.FitlerData("PaymentID", ID);
        
        if (Relationship.isEmpty()) {
            return null;
        }
        
        List<String> RelatedItemIDList = new ArrayList<>();
        
        for (List<String> r : Relationship) {
            RelatedItemIDList.add(r.get(1)); // get ID of All Related Items 
        }

//        Read Product File And Get All Related Products To PR/Sales
        this.reader.setFileName("PO");

        List<List<String>> ItemDetailList = reader.FitlerData("POID", RelatedItemIDList);

        List<Item> ItemList = new ArrayList<>();

        for (int i = 0; i < ItemDetailList.size(); i++) {
            List<String> RowData = ItemDetailList.get(i);

            ItemFactory Factory = new ItemFactory();

            Item newItem = Factory.createItem(RowData, "PO");
            ItemList.add(newItem);
        }

        return ItemList;
    }

    @Override
    public Boolean CanBeDeleted() {
        return true;
    }

}
