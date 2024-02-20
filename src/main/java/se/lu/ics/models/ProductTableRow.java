package se.lu.ics.models;

public class ProductTableRow {
    private String ProductName;
    private String ProductId;
    private int Stock;

    public ProductTableRow() {
    }

    public ProductTableRow(String ProductName, String ProductId, int Stock) {
        this.ProductName = ProductName;
        this.ProductId = ProductId;
        this.Stock = Stock;
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

    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }

    public void setStock(int Stock) {
        this.Stock = Stock;
    }
}
