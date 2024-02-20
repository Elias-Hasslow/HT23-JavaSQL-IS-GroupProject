package se.lu.ics.models;

public class StockSummary {
    private String ProductName;
    private String ProductId;
    private int Stock;
    private String SupplierId;
    private String WarehouseId;
    private String CategoryName;

    public StockSummary() {

    }

    public StockSummary(String ProductName, String ProductId, int Stock, String SupplierId, String WarehouseId, String CategoryName) {
        this.ProductName = ProductName;
        this.ProductId = ProductId;
        this.Stock = Stock;
        this.WarehouseId = WarehouseId;
        this.CategoryName = CategoryName;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductId() {
        return ProductId;
    }

    public int getStock() {
        return Stock;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setProductId(String string) {
    }

    public void setProductName(String string) {
    }

    public void setStock(int int1) {
    }

    public void setWarehouseId(String string) {
    }

    public void setCategoryName(String string) {
    }


    
}
