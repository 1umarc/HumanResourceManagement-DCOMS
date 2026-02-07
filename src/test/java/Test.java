/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package nodes;

import DataAbstractions.*;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author JONATHAN
 */
public class POTS {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        ItemCollectionFactory Factory = new ItemCollectionFactory();
        ItemCollection POCollection = Factory.createItemCollection("User");
        
        String[] DetailsArray = {"3", "6", "1", "10/10/24"};
        List<String> Details = Arrays.asList(DetailsArray);
        
        System.out.println(Details);
        
//        Details.add("3");
// //        Details.add("5");
// //        Details.add("1");
// //        Details.add("10/10/24");

// //        
// //      get PO with id 3
//         Item PO = POCollection.getItem("3");
        
//         System.out.println(PO.CanBeDeleted());
        
// //        String[] NewDetailsArray = {"3", "6", "1", "10/10/25"};
// //        List<String> NewDetails = Arrays.asList(NewDetailsArray);
// //
// //        
// //        PO.setDetails(NewDetails);
// //        
// //        POCollection.updateItem(PO);
        
// //      Change Date
//         PO.setFieldValue("Date", "10/10/25");
        
// //        Save changes to file
//         POCollection.UpdateFile();
        
        
// //      Create array with wanted field and values
//         String[] Fields = {"UserID", "Date"};
//         String[] Values = {"5", "10/10/24"};
        
//         // Convert Array to list
//         List<String> FieldList = Arrays.asList(Fields);
//         List<String> ValuesList = Arrays.asList(Values);
        
// //        System.out.println(POCollection + "adn");
        
// //        Filter PO using List of fields and List of values
// //        List<Item> PoList = POCollection.filter(FieldList, ValuesList);
        
// //        for (Item FilteredPO : PoList) {
// //            System.out.println("FUCK");
// //            System.out.println(FilteredPO);
// //        }
        
// //        Filter PO using String Field and List of Values
// //        POCollection.filter("POID", ValuesList);
        
//         // Create an interface to interact with the PR text file
//         ItemCollection PRCollection = Factory.createItemCollection("PR");
        
//         // Create a list with the details of the PR
//         String[] DetailsList = {"5","1","1","1"};
        
//         // create item with the data from DetailsList
//         Item PR = PRCollection.createItem(DetailsList);
        
//         // If item exist then get the item instead
//         if (PR == null) {
//             PR = PRCollection.getItem(DetailsList[0]);
//         }
        
//         ItemCollection ProductCollection = Factory.createItemCollection("Product");
        
// //        List<Item> SortedList = ProductCollection.getSortedItems((a, b) -> {
// //            return (Integer.parseInt(a.getFieldValue("Quantity")) / Integer.parseInt(a.getFieldValue("Threshold"))) - (Integer.parseInt(b.getFieldValue("Quantity")) / Integer.parseInt(b.getFieldValue("Threshold"))) ;
// //        });
        
//         // Print out sorted Products
// //        for (Item SortedProduct : SortedList) {
// //            System.out.println(SortedProduct);
// //        }
        
        
//         // Filter out product - products that are below threshold and have 
//         List<Item> RequestedProduct = ProductCollection.filter((a) -> {
//             return Integer.parseInt(a.getFieldValue("Quantity")) <= Integer.parseInt(a.getFieldValue("Threshold"));
//         });
        
//         for (Item currentProduct : RequestedProduct) {
            
//             // Checks if same Supplier
//             if (!PR.getFieldValue("SupplierID").equals(currentProduct.getFieldValue("SupplierID"))) {
//                 continue;
//             }
            
//             // Set the quantity that want to purchase
//             currentProduct.setFieldValue("Quantity", "100");
            
//             // add relationship and print out if the relationship is successfuly created
//             System.out.println(PR.addRelatedItem(currentProduct));
//         }
        
//         List<Item> AllRelatedProducts = PR.getDownwardsRelatedItems("Product");
        
//         for (Item RelatedProduct : AllRelatedProducts) {
//             System.out.println(RelatedProduct);
//         }
        
//         PO.addRelatedItem(PR);
//         PO.deleteRelatedItem(PR);
        
        
// //        for (Item newPO : POCollection.getAll()) {
// //            System.out.println("PO Details \n\n");
// //            
// //            System.out.println(newPO);
// //            
// //            List<Item> PRCollection = newPO.getDownwardsRelatedItems("PR");
// //
// //            if (PRCollection == null) {
// //                continue;
// //            }
// //
// //            System.out.println("PR Details \n\n");
// //            
// //            for (Item newPR : PRCollection) {
// //                System.out.println(newPR);
// //                
// //                List<Item> ProductCollection = newPR.getDownwardsRelatedItems("Product");
// //
// //                if (ProductCollection == null) {
// //                    continue;
// //                }
// //
// //                System.out.println("Product Details \n\n");
// //
// //
// //                for (Item a : ProductCollection) {
// //                    System.out.println(a);
// //                }
// //
// //            }
// //        }
        
        
        
        
    
    }
    
    
}


