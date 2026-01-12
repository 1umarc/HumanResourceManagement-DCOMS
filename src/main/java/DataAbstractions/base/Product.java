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
public class Product extends Item {

    public Product(List<String> Details, String Type, DataReader reader, DataWriter writer) {
        super(Details, Type, reader, writer);
    }

    @Override
    public List<Item> getUpwardsRelatedItems(String Type) {
        switch (Type) {
            case "PR", "Sales" -> {
                return this.getItemRelatedToProduct(Type);
            }
            case "PO" -> {
                List<Item> PRList = this.getUpwardsRelatedItems("PR");
                
                List<Item> POList = new ArrayList<>();
                
                for (Item currentPR : PRList) {
                    List<Item> PaymentRelatedToPO = currentPR.getUpwardsRelatedItems("PR");
                    
                    POList.addAll(PaymentRelatedToPO);
                }
                
                return POList;
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
    public List<Item> getDownwardsRelatedItems(String Type) { // No Downwards
        return null;
    }
    
    @Override
    public Boolean addRelatedItem(Item newItem) {return false;}

    @Override
    public Boolean deleteRelatedItem(Item newItem) {return false;}
    
    private List<Item> getItemRelatedToProduct(String Type) {
        String ID = this.getID();
        
//        Reading and Writing To The Relationship File
        String FileName = "ProductTo" + Type; // ProductToPR / ProductToSales

        this.reader.setFileName(FileName);
        
//        Get All Product/PR/PO Rows Related To The PR/PO/Payment
        List<List<String>> Relationship = this.reader.FitlerData("ProductID", ID);
        
        if (Relationship.isEmpty()) {
            return null;
        }
        
        List<String> RelatedItemIDList = new ArrayList<>();
        
        for (List<String> r : Relationship) {
            RelatedItemIDList.add(r.get(0)); // get ID of All Related Items 
        }

//        Read Product File And Get All Related Products To PR/Sales
        this.reader.setFileName(Type);
        
        String IDType = Type + "ID"; // PRID / SalesID

        List<List<String>> ItemDetailList = this.reader.FitlerData(IDType, RelatedItemIDList);

        List<Item> ItemList = new ArrayList<>();

        for (int i = 0; i < ItemDetailList.size(); i++) {
            List<String> RowData = ItemDetailList.get(i);

            ItemFactory Factory = new ItemFactory();

            Item newItem = Factory.createItem(RowData, Type);
            ItemList.add(newItem);
        }

        return ItemList;
    }

    @Override
    public Boolean CanBeDeleted() {
        List<String> UpwardsRelations = Arrays.asList("PR", "Sales");
        
        
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
