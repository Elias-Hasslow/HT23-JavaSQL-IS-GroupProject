package se.lu.ics.models;

import java.util.ArrayList;

public class ProductCategory {
    private String CategoryName;
    private ArrayList<Product> Products;
    
    // Constructor
    public ProductCategory(String CategoryName, ArrayList<Product> Products) {
        this.CategoryName = CategoryName;
        this.Products = Products;
    }

    public ProductCategory() {
    }

    // Getters

    public String getCategoryName() {
        return CategoryName;
    }

    public ArrayList<Product> getProducts() {
        return Products;
    }

    // Setters

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public void setProducts(ArrayList<Product> Products) {
        this.Products = Products;
    }

    // Methods

    public void addProduct(Product product) {
        this.Products.add(product);
    }

    public void removeProduct(Product product) {
        this.Products.remove(product);
    }
    
}
