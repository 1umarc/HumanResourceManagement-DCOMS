/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataAbstractions.base;

import DataAbstractions.Item;
import java.util.List;

/**
 *
 * @author JONATHAN
 */
public class ItemFactory {
    public Item createItem(List<String> RowData, String Type) {
        switch (Type) {
            case "PO" -> {
                List<String> ReadPermissions = List.of(Type, "PR", "PRToPO", "Payment", "POToPayment");
                List<String> WritePermissions = List.of("PRToPO", "POToPayment");
                
                DataReader reader = new FileHandler(Type, ReadPermissions);
                DataWriter writer = new FileHandler(Type, WritePermissions);

                return new PO(RowData, Type, reader, writer);
            }
            case "PR" -> {
                List<String> ReadPermissions = List.of(Type, "Product", "ProductToPR", "PR", "PRToPO");
                List<String> WritePermissions = List.of("ProductToPR", "PRToPO");
                
                DataReader reader = new FileHandler(Type, ReadPermissions);
                DataWriter writer = new FileHandler(Type, WritePermissions);
                
                return new PR(RowData, Type, reader, writer);
            }
            case "Product" -> {
                List<String> ReadPermissions = List.of(Type, "ProductToSales", "Sales", "ProductToPR", "PR");
                List<String> WritePermissions = List.of("ProductToSales", "ProductToPR");
                
                DataReader reader = new FileHandler(Type, ReadPermissions);
                DataWriter writer = new FileHandler(Type, WritePermissions);
                
                return new Product(RowData, Type, reader, writer);
            }
            case "Payment" -> {
                List<String> ReadPermissions = List.of(Type, "POToPayment", "PO");
                List<String> WritePermissions = List.of("POToPayment");
                
                DataReader reader = new FileHandler(Type, ReadPermissions);
                DataWriter writer = new FileHandler(Type, WritePermissions);
                
                return new Payment(RowData, Type, reader, writer);
            }
            case "Sales" -> {
                List<String> ReadPermissions = List.of(Type, "ProductToSales", "Product");
                List<String> WritePermissions = List.of("ProductToSales");
                
                DataReader reader = new FileHandler(Type, ReadPermissions);
                DataWriter writer = new FileHandler(Type, WritePermissions);
                
                return new Sales(RowData, Type, reader, writer);
            }
            case "Supplier" -> {
                List<String> ReadPermissions = List.of(Type, "Product");
                List<String> WritePermissions = List.of();
                
                DataReader reader = new FileHandler(Type, ReadPermissions);
                DataWriter writer = new FileHandler(Type, WritePermissions);
                
                return new Supplier(RowData, Type, reader, writer);
            }
            case "User" -> {
                List<String> ReadPermissions = List.of(Type, "PO", "PR", "Sales");
                List<String> WritePermissions = List.of();
                
                DataReader reader = new FileHandler(Type, ReadPermissions);
                DataWriter writer = new FileHandler(Type, WritePermissions);
                
                return new User(RowData, Type, reader, writer);
            }
            default -> {
                return null;
            }
        }
    }
}
