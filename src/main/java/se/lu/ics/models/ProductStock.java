package se.lu.ics.models;

import java.util.HashSet;
import java.util.Set;

import se.lu.ics.data.ProductStockDAO;

public class ProductStock {
    private int ProductStockId;
    private String ProductId;
    private String WarehouseId;
    private String SupplierId;
    private int StockCount;

    // Constructor
    public ProductStock(int ProductStockId, String ProductId, String WarehouseId, String SupplierId, int StockCount) {
        this.ProductStockId = ProductStockId;
        this.ProductId = ProductId;
        this.WarehouseId = WarehouseId;
        this.SupplierId = SupplierId;
        this.StockCount = StockCount;
    }
    
    public ProductStock() {
    }

    // Getters

    public int getProductStockId() {
        return ProductStockId;
    }

    public String getProductId() {
        return ProductId;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public String getSupplierId() {
        return SupplierId;
    }

    public int getStockCount() {
        return StockCount;
    }

    // Setters

    public void setProductStockId(int ProductStockId) {
        this.ProductStockId = ProductStockId;
    }

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public void setWarehouseId(String WarehouseId) {
        this.WarehouseId = WarehouseId;
    }

    public void setSupplierId(String SupplierId) {
        this.SupplierId = SupplierId;
    }

    public void setStockCount(int StockCount) {
        this.StockCount = StockCount;
    }

    // Methods

    public void addStock(int StockCount) {
        this.StockCount += StockCount;
    }

    public void removeStock(int StockCount) {
        this.StockCount -= StockCount;
    }

      public static int generateId()  {
        Set<Integer> usedIds = new HashSet<>();
        
        // Iterate through the products to collect used IDs
        for (ProductStock productStock : ProductStockDAO.getProductStocks()) {
            usedIds.add(productStock.getProductStockId());
        }

        // Find the first gap in the sequence
        for (int i = 1; i <= usedIds.size() + 1; i++) {
            int idToCheck =  i;
            if (!usedIds.contains(idToCheck)) {
                return idToCheck;
            }
        }
        return 0;
    }
}
