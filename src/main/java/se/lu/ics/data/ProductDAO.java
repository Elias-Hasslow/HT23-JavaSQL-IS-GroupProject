package se.lu.ics.data;

import se.lu.ics.models.Product;
import se.lu.ics.models.Supplier;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;


import javafx.collections.ObservableList;

public class ProductDAO {
    private static ObservableList<Product> products = FXCollections.observableArrayList();

    static {
        updateProductsFromDatabase();
    }

    public static void updateProductsFromDatabase() {
        String query = "SELECT * FROM Product WHERE IsDeleted = 0";
        try(Connection  connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            products.clear();
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getString("ProductId"));
                product.setProductName(resultSet.getString("ProductName"));
                product.setWarehouseId(resultSet.getString("WarehouseId"));
                product.setSupplierId(resultSet.getString("SupplierId"));
                product.setCategoryName(resultSet.getString("CategoryName"));
                products.add(product);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static ObservableList<Product> getProducts() {
        return products;
    }

    // Methods

    // Method to add products
    public static boolean createProduct(String productName, String supplierId, String warehouseId, String category, int initialStock) {
        // Initialize a flag to track the success of the operation
        boolean success = false;

        // Generate a new product ID
        String productId = Product.generateId(); // Call your method to generate the ID

        // Define SQL statements for inserting into "Product" and "ProductStock" tables
        String insertProductSQL = "INSERT INTO Product (ProductId, ProductName, SupplierId, WarehouseId, CategoryName, IsDeleted) VALUES (?, ?, ?, ?, ?, 0)";
        String insertProductStockSQL = "INSERT INTO ProductStock (ProductId, Stock, SupplierId, WarehouseId) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement productStatement = connection.prepareStatement(insertProductSQL);
             PreparedStatement productStockStatement = connection.prepareStatement(insertProductStockSQL)) {

            // Set parameters for the "Product" table
            productStatement.setString(1, productId);
            productStatement.setString(2, productName);
            productStatement.setString(3, supplierId);
            productStatement.setString(4, warehouseId);
            productStatement.setString(5, category);

            // Execute the INSERT statement for "Product"
            int productAffectedRows = productStatement.executeUpdate();

            // Check if the insert into "Product" was successful
            if (productAffectedRows == 1) {
                // Set parameters for the "ProductStock" table
                productStockStatement.setString(1, productId);
                productStockStatement.setInt(2, initialStock);
                productStockStatement.setString(3, supplierId);
                productStockStatement.setString(4, warehouseId);

                // Execute the INSERT statement for "ProductStock"
                int stockAffectedRows = productStockStatement.executeUpdate();

                // Check if the stock insert was successful
                if (stockAffectedRows == 1) {
                    success = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        ProductDAO.updateProductsFromDatabase();
        return success;
    }

    //Method to retrieve products
    public static ObservableList<Product> getProductById(String productId) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT * FROM Product WHERE ProductId = ? AND IsDeleted = 0";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, productId);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Product product = new Product();
                    product.setProductId(resultSet.getString("ProductId"));
                    product.setProductName(resultSet.getString("ProductName"));
                    product.setSupplierId(resultSet.getString("SupplierId"));
                    product.setWarehouseId(resultSet.getString("WarehouseId"));
                    product.setCategoryName(resultSet.getString("CategoryName"));
                    products.add(product);
                    return products;
                }
            }

        } catch (SQLException e) {
        }
        return null;
    }

    //Method to update products

    public static boolean updateProduct(String productId, String productName, String supplierId, String warehouseId, String categoryName) {
        String query = "UPDATE Product SET ";
        boolean isFirst = true;
    
        if (!productName.isEmpty()) {
            query += "ProductName = ?";
            isFirst = false;
        }
    
        if (!supplierId.isEmpty()) {
            if (!isFirst) {
                query += ", ";
            }
            query += "SupplierId = ?";
            isFirst = false;
        }
    
        if (!warehouseId.isEmpty()) {
            if (!isFirst) {
                query += ", ";
            }
            query += "WarehouseId = ?";
            isFirst = false;
        }
    
        if (!categoryName.isEmpty()) {
            if (!isFirst) {
                query += ", ";
            }
            query += "CategoryName = ?";
        }
    
        query += " WHERE ProductId = ?";
    
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
    
            int parameterIndex = 1;
    
            if (productName != null && !productName.isEmpty()) {
                preparedStatement.setString(parameterIndex, productName);
                parameterIndex++; // Increment for the next parameter
            }
    
            if (supplierId != null && !supplierId.isEmpty()) {
                preparedStatement.setString(parameterIndex, supplierId);
                parameterIndex++; // Increment for the next parameter
            }
    
            if (warehouseId != null && !warehouseId.isEmpty()) {
                preparedStatement.setString(parameterIndex, warehouseId);
                parameterIndex++; // Increment for the next parameter
            }
    
            if (categoryName != null && !categoryName.isEmpty()) {
                preparedStatement.setString(parameterIndex, categoryName);
                parameterIndex++; // Increment for the next parameter
            }

            preparedStatement.setString(parameterIndex, productId);
 
    
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return false;
    }

    //Method to retrieve products by category
    public static ObservableList<Product> getProductsByCategory(String category) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        String query = "SELECT * FROM Product WHERE CategoryName = ? AND IsDeleted = 0";

        try (Connection connection = ConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, category);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setProductId(resultSet.getString("ProductId"));
                    product.setProductName(resultSet.getString("ProductName"));
                    product.setSupplierId(resultSet.getString("SupplierId"));
                    product.setWarehouseId(resultSet.getString("WarehouseId"));
                    product.setCategoryName(resultSet.getString("CategoryName"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    //Method to retrieve productstock by productid
    public static int getProductStock(String productId) {
        String query = "SELECT SUM(Stock) AS TotalStock FROM ProductStock WHERE ProductId = ? AND IsDeleted = 0";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, productId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalStock");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if product not found or an error occurred
    }

    //Method to retrieve all products supplied by a supplier
    public static ObservableList<Product> getProductsBySupplier(String supplierId) {
        // Define your SQL query to retrieve products by supplier
        String query = "SELECT * FROM Product WHERE SupplierId = ? AND IsDeleted = 0";
        
        try (Connection connection = ConnectionHandler.getConnection()) {
            // Create a prepared statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            // Set the supplierId parameter
            preparedStatement.setString(1, supplierId);
            
            // Execute the query and collect the results in a list
            ObservableList<Product> products = FXCollections.observableArrayList();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    // Set product attributes from the result set
                    product.setProductId(resultSet.getString("ProductId"));
                    product.setProductName(resultSet.getString("ProductName"));
                    product.setSupplierId(resultSet.getString("SupplierId"));
                    product.setWarehouseId(resultSet.getString("WarehouseId"));
                    product.setCategoryName(resultSet.getString("CategoryName"));
                    // Add the product to the list
                    products.add(product);
                }
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Method to retrieve all products in a warehouse
    public static ObservableList<Product> getProductsInWarehouse(String warehouseId) {
        String query = "SELECT * FROM Product WHERE WarehouseId = ? AND IsDeleted = 0";
        ObservableList<Product> productsInWarehouse = FXCollections.observableArrayList();
    
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, warehouseId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setProductId(resultSet.getString("ProductId"));
                    product.setProductName(resultSet.getString("ProductName"));
                    product.setSupplierId(resultSet.getString("SupplierId"));
                    product.setWarehouseId(resultSet.getString("WarehouseId"));
                    product.setCategoryName(resultSet.getString("CategoryName"));
                    productsInWarehouse.add(product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return productsInWarehouse;
    }
    
    //Delete product by id
    public static boolean deleteProductById(String productId) {
        String query = "UPDATE Product SET IsDeleted = 1 WHERE ProductId = ?";
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productId);
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Delete product by category
    public static boolean deleteProductsByCategory(String categoryName) {
        String query = "UPDATE Product SET IsDeleted = 1 WHERE CategoryName = ?";
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, categoryName);
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    


    //Method to count total amount of products
    public static int getTotalNumberOfProducts() {
        String query = "SELECT COUNT(*) AS TotalProducts FROM Product WHERE IsDeleted = 0";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            if (resultSet.next()) {
                return resultSet.getInt("TotalProducts");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Return 0 if there's an error or no products found
    }

    
    //Method to retrieve the totals stock for products within a category
    public static int getTotalStockByCategory(String category) {
        String query = "SELECT SUM(ps.Stock) AS TotalStock " +
                       "FROM Product p " +
                       "INNER JOIN ProductStock ps ON p.ProductId = ps.ProductId " +
                       "WHERE p.CategoryName = ? " +
                       "AND p.IsDeleted = 0";
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, category);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalStock");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        // Return 0 if there was an error or no result found
        return 0;
    }
    


    //Method to retrieve all products with stock below 50
    public static ObservableList<Product> getProductsBelowStock50() {
        String query = "SELECT p.ProductId, p.ProductName, p.WarehouseId, p.SupplierId, ps.Stock " +
                       "FROM Product p " +
                       "INNER JOIN ProductStock ps ON p.ProductId = ps.ProductId " +
                       "WHERE ps.Stock < 50 " +
                       "AND p.IsDeleted = 0";
        
        ObservableList<Product> products = FXCollections.observableArrayList();
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setProductId(resultSet.getString("ProductId"));
                    product.setProductName(resultSet.getString("ProductName"));
                    product.setSupplierId(resultSet.getString("SupplierId"));
                    product.setWarehouseId(resultSet.getString("WarehouseId"));
                    product.setCategoryName(resultSet.getString("CategoryName"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }

    //Method to retrieve all suppliers who have supplied to a warehouse
    public static ObservableList<Supplier> getSuppliersByWarehouseId(String warehouseId) {
        String query = "SELECT DISTINCT s.* " +
                       "FROM Supplier s " +
                       "JOIN ProductStock ps ON s.SupplierId = ps.SupplierId " +
                       "WHERE ps.WarehouseId = ? " +
                       "AND ps.Stock > 0 " +
                       "AND s.IsDeleted = 0";
    
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, warehouseId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setSupplierId(resultSet.getString("SupplierId"));
                    supplier.setSupplierName(resultSet.getString("SupplierName"));
                    suppliers.add(supplier);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return suppliers;
    }

    public static Product findSingleProductFromId(String productId) {
        for(Product product : products) {
            if(product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public static ObservableList<Product> findProductsEvenDeleted() {
        String query = "SELECT * FROM Product";
        try(Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            products.clear();
            while (resultSet.next()) {
                Product product = new Product();
                product.setProductId(resultSet.getString("ProductId"));
                product.setProductName(resultSet.getString("ProductName"));
                product.setWarehouseId(resultSet.getString("WarehouseId"));
                product.setSupplierId(resultSet.getString("SupplierId"));
                product.setCategoryName(resultSet.getString("CategoryName"));
                products.add(product);
            } 
            return products;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }



        

    
}





    
    






