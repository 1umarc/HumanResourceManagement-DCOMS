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
public class Supplier extends Item {

    public Supplier(List<String> Details, String Type, DataReader reader, DataWriter writer) {
        super(Details, Type, reader, writer);
    }

    @Override
    public List<Item> getUpwardsRelatedItems(String Type) {
        return null;
    }

    @Override
    public List<Item> getDownwardsRelatedItems(String Type) {
        switch (Type) {
            case "Product" -> {
                return this.getProductRelatedToSupplier();
            }
            default -> {
                return null;
            }
        }
    }
    
    @Override
    public Boolean addRelatedItem(Item newItem) {return false;}

    @Override
    public Boolean deleteRelatedItem(Item newItem) {return false;}
    
    private List<Item> getProductRelatedToSupplier() {
        String ID = this.getID();
        
//        Reading and Writing To The Relationship File
        this.reader.setFileName("Product");
        
//        Get All Product/PR/PO Rows Related To The PR/PO/Payment
        List<List<String>> ItemDetailList = this.reader.FitlerData("SupplierID", ID);
        
        if (ItemDetailList.isEmpty()) {
            return null;
        }
        
        List<Item> ItemList = new ArrayList<>();
        
        for (int i = 0; i < ItemDetailList.size(); i++) {
            List<String> RowData = ItemDetailList.get(i);

            ItemFactory Factory = new ItemFactory();

            Item newItem = Factory.createItem(RowData, "Product");
            ItemList.add(newItem);
        }

        return ItemList;
    }

    @Override
    public Boolean CanBeDeleted() {
        List<Item> RelatedItems = this.getDownwardsRelatedItems("Product");
            
        if (RelatedItems == null || RelatedItems.isEmpty()) {
            return true; 
        }
        
        for (Item currentItem : RelatedItems) {
            if (!currentItem.CanBeDeleted()) {
                return false;
            }
        }
        
        return true;
    }
    
}
