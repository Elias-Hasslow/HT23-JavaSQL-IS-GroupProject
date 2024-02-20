package se.lu.ics.models;

import java.util.HashSet;
import java.util.Set;

import se.lu.ics.data.ProductDAO;

public class Product {
    private String ProductId;
    private String ProductName;
    private String SupplierId;
    private String WarehouseId;
    private String CategoryName;

    // Constructor
    public Product(String ProductId, String ProductName, String SupplierId, String WarehouseId, String CategoryName) {
        this.ProductId = ProductId;
        this.ProductName = ProductName;
        this.SupplierId = SupplierId;
        this.WarehouseId = WarehouseId;
        this.CategoryName = CategoryName;
        }

    public Product() {
    }

    // Getters
    public String getProductId() {
        return ProductId;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getSupplierId() {
        return SupplierId;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    // Setters

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public void setSupplierId(String SupplierId) {
        this.SupplierId = SupplierId;
    }

    public void setWarehouseId(String WarehouseId) {
        this.WarehouseId = WarehouseId;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public static String generateId()  {
        Set<String> usedIds = new HashSet<>();
        
        // Iterate through the products to collect used IDs
        for (Product product : ProductDAO.findProductsEvenDeleted()) {
            usedIds.add(product.getProductId());
        }

        // Find the first gap in the sequence
        for (int i = 1; i <= usedIds.size() + 1; i++) {
            String idToCheck = "P" + i;
            if (!usedIds.contains(idToCheck)) {
                return idToCheck;
            }
        }
        return null;
    }
}
