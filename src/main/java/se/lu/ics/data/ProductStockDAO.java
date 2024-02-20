package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.ProductStock;
import se.lu.ics.models.Warehouse;

public class ProductStockDAO {
    private static ObservableList<ProductStock> productStocks = FXCollections.observableArrayList();

    static {
        updateProductStockFromDatabase();
    }

    public static void updateProductStockFromDatabase() {
        String query = "SELECT * FROM ProductStock";
        productStocks.clear();
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ProductStock productStock = new ProductStock();
                productStock.setProductStockId(resultSet.getInt("ProductStockId"));
                productStock.setProductId(resultSet.getString("ProductId"));
                productStock.setWarehouseId(resultSet.getString("WarehouseId"));
                productStock.setSupplierId(resultSet.getString("SupplierId"));
                productStock.setStockCount(resultSet.getInt("Stock"));
                productStocks.add(productStock);
            }
        } catch (SQLException e) {
        }
    }

    public static ObservableList<ProductStock> getProductStocks() {
        return productStocks;
    }


    // Methods

    //Method to get totla sothck count for a product
    public static int getTotalStockCountForProduct(String productId) {
        String query = "SELECT SUM(Stock) AS TotalStock FROM ProductStock WHERE ProductId = ?";
        try (Connection connection = ConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, productId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalStock");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Return 0 if no stock information found
    }

    //Method that returns total amount of stock for certain warehouse
    public static ObservableList<ProductStock> getStockForWarehouse(String warehouseId) {
        String query = "SELECT PS.ProductId, P.ProductName, PS.Stock " +
                       "FROM ProductStock AS PS " +
                       "INNER JOIN Product AS P ON PS.ProductId = P.ProductId " +
                       "WHERE PS.WarehouseId = ?";
        ObservableList<ProductStock> stockList = FXCollections.observableArrayList();
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, warehouseId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ProductStock productStock = new ProductStock();
                    productStock.setProductStockId(resultSet.getInt("ProductStockId"));
                    productStock.setProductId(resultSet.getString("ProductId"));
                    productStock.setWarehouseId(resultSet.getString("WarehouseId"));
                    productStock.setSupplierId(resultSet.getString("SupplierId"));
                    productStock.setStockCount(resultSet.getInt("Stock"));
                    stockList.add(productStock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return stockList;
    }

    //method to delete product stock from warehouse, warehouseid keept in product to maintain history
    public static boolean removeProductFromWarehouse(String productStockId) {
        String query = "DELETE FROM ProductStock WHERE ProductStockId = ?";
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setString(1, productStockId);
    
            int rowsDeleted = preparedStatement.executeUpdate();
    
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getCurrentStockCount(String productStockId) {
        String query = "SELECT Stock FROM ProductStock WHERE ProductStockId = ?";
        try (Connection connection = ConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, productStockId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("Stock");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Return 0 if no stock information found
    }

        public static int getProductStockMaxCapacity(String productStockId) {
            String query = "SELECT MaxCapacity FROM Warehouse WHERE WarehouseId = (SELECT WarehouseId FROM ProductStock WHERE ProductStockId = ?)";
            try (Connection connection = ConnectionHandler.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                
                preparedStatement.setString(1, productStockId);
                
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("MaxCapacity");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            return 0; // Return 0 if no stock information found
        }   
    
    
    

    
    public static boolean addStockToProductStock(String productStockId, int addedStock) {
        int currentStock = getCurrentStockCount(productStockId);
        int maxCapacity = getProductStockMaxCapacity(productStockId);
        String query = "UPDATE ProductStock SET Stock = ? WHERE ProductStockId = ?";
    
        // Calculate the new stock after adding
        int newStock = currentStock + addedStock;
    
        // Check if the new stock exceeds the max capacity
        if (newStock > maxCapacity) {
            System.out.println("Warning: Stock cannot exceed the maximum capacity.");
            return false; // Stock addition not allowed
        }
    
        // Check if the new stock is between 80% and 100% of the max capacity
        if (newStock >= 0.8 * maxCapacity && newStock <= maxCapacity) {
            

        }
    
        // Perform the actual stock addition and update the database
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, newStock);
            preparedStatement.setString(2, productStockId);
            int rowsUpdated = preparedStatement.executeUpdate();
    
            if (rowsUpdated > 0) {
                System.out.println("Stock added successfully.");
                return true;
            }
        } catch (SQLException e) {

        }
    
        return false; // Stock addition or database update failed
    }

    public static ObservableList<ProductStock> getProductStockById(String productStockId) {
        for(ProductStock productStock : productStocks) {
            if (Integer.toString(productStock.getProductStockId()).equals(productStockId)) {
                ObservableList<ProductStock> productStockList = FXCollections.observableArrayList();
                productStockList.add(productStock);
                return productStockList;
            }
        }
        return null;
    }

    public static int showStockForCategory(String categoryName) {
        String query = "SELECT SUM(Stock) AS TotalStock FROM ProductStock WHERE ProductId IN (SELECT ProductId FROM Product WHERE CategoryName = ?)";
        try (Connection connection = ConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, categoryName);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TotalStock");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Return 0 if no stock information found

    }

    //Method for aggregation of stock
    public static String getStockInfo(String warehouseId, String categoryName, String productName) {
        // Define the base SQL query
        String query = "SELECT SUM(ps.Stock) AS TotalStock FROM ProductStock ps " +
                       "INNER JOIN Product p ON ps.ProductId = p.ProductId " +
                       "INNER JOIN ProductCategory c ON p.CategoryName = c.CategoryName " +
                       "INNER JOIN Warehouse w ON ps.WarehouseId = w.WarehouseId " +
                       "WHERE (w.WarehouseId = ? OR ? IS NULL OR w.WarehouseId = '')";
    
        // Check if productName is provided, and if so, add a condition to the query
        if (productName != null && !productName.isEmpty()) {
            query += " AND (p.ProductName = ? OR ? IS NULL OR p.ProductName = '')";
        }
    
        // Check if categoryName is provided, and if so, add a condition to the query
        if (categoryName != null && !categoryName.isEmpty()) {
            query += " AND (c.CategoryName = ? OR ? IS NULL OR c.CategoryName = '')";
        }
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            // Set parameters based on user input
            preparedStatement.setString(1, warehouseId);
            preparedStatement.setString(2, warehouseId);
    
            // Set parameters for productName and categoryName if provided
            int parameterIndex = 3; // Start parameter index after warehouseId placeholders
    
            if (productName != null && !productName.isEmpty()) {
                preparedStatement.setString(parameterIndex++, productName);
                preparedStatement.setString(parameterIndex++, productName);
            }
    
            if (categoryName != null && !categoryName.isEmpty()) {
                preparedStatement.setString(parameterIndex++, categoryName);
                preparedStatement.setString(parameterIndex, categoryName);
            }
    
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int totalStock = resultSet.getInt("TotalStock");
    
                if (!warehouseId.isEmpty()) {
                    if (!categoryName.isEmpty()) {
                        if (!productName.isEmpty()) {
                            // Aggregated on product level
                            return "The stock for product '" + productName + "'" + "\n"
                                 +  "In category '" + categoryName + "'" + "\n"
                                 +  "In warehouse '" + warehouseId + "' is: " + totalStock;
                        } else {
                            // Aggregated on category level
                            return "The stock for category '" + categoryName + "'" + "\n"
                                +   "In warehouse '" + warehouseId + "' is: " + totalStock;
                        }
                    } else {
                        // Aggregated on warehouse level
                        return "The stock for warehouse '" + warehouseId + "' is: " + totalStock;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return null; // Return null if no data is found or in case of an error
    }

    public static int calculateTotalStock(String warehouseName, String categoryName, String productName) {
        String query = "SELECT SUM(ps.Stock) AS TotalStock FROM ProductStock ps WHERE 1=1";
    
        if (!warehouseName.isEmpty()) {
            query += " AND ps.WarehouseId = ?";
        }
    
        if (!categoryName.isEmpty()) {
            query += " AND ps.CategoryName = ?";
        }
    
        if (!productName.isEmpty()) {
            query += " AND ps.ProductName = ?";
        }
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            int parameterIndex = 1;
    
            if (!warehouseName.isEmpty()) {
                preparedStatement.setString(parameterIndex, warehouseName);
                parameterIndex++;
            }
    
            if (!categoryName.isEmpty()) {
                preparedStatement.setString(parameterIndex, categoryName);
                parameterIndex++;
            }
    
            if (!productName.isEmpty()) {
                preparedStatement.setString(parameterIndex, productName);
            }
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                return resultSet.getInt("TotalStock");
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return 0; // Return 0 if no stock found or an error occurred
    }

    public static ObservableList<ProductStock> getWarehousesById(String warehouseId) {
        String query = "SELECT * FROM ProductStock WHERE WarehouseId = ?";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, warehouseId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<ProductStock> productStockList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                ProductStock productStock = new ProductStock();
                productStock.setProductStockId(resultSet.getInt("ProductStockId"));
                productStock.setProductId(resultSet.getString("ProductId"));
                productStock.setWarehouseId(resultSet.getString("WarehouseId"));
                productStock.setSupplierId(resultSet.getString("SupplierId"));
                productStock.setStockCount(resultSet.getInt("Stock"));
                productStockList.add(productStock);
            }
            return productStockList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Warehouse findWarehouseThroughStockId(String StockId) {
        for(ProductStock productStock : productStocks) {
            if (Integer.toString(productStock.getProductStockId()).equals(StockId)) {
                return WarehouseDAO.findSingleWarehouseFromId(productStock.getWarehouseId());
            }
        }
        return null;
    }

    
    
}