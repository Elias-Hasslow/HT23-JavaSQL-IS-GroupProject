package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.Warehouse;

public class WarehouseDAO {
    private static ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();

    static {
        updateProductsFromDatabase();
    }

    // Methods
    public static void updateProductsFromDatabase() {
        String query = "SELECT * FROM Warehouse";
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            warehouses.clear();
            while (resultSet.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setWarehouseId(resultSet.getString("WarehouseId"));
                warehouse.setWarehouseName(resultSet.getString("WarehouseName"));
                warehouse.setMaxCapacity(resultSet.getInt("MaxCapacity"));
                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Warehouse> getWarehousesFromDatabase() {
        return warehouses;
    }


    //Method to create Warehouse
    public static boolean addWarehouse(String warehouseName, int maxCapacity) {
        // SQL INSERT statement to add a new warehouse
        String insertQuery = "INSERT INTO Warehouse (WarehouseId, WarehouseName, MaxCapacity) VALUES (?, ?, ?)";
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
    
            String warehouseId = Warehouse.generateId(); // Generate a unique warehouse ID
            if (warehouseId == null) {
                return false; // Unable to generate a unique ID
            }
    
            preparedStatement.setString(1, Warehouse.generateId());
            preparedStatement.setString(2, warehouseName);
            preparedStatement.setInt(3, maxCapacity);
    
            int rowsAffected = preparedStatement.executeUpdate();
    
            return rowsAffected > 0;
    
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //Method to retrieve warehouse by id
    public static ObservableList<Warehouse> getWarehouseById(String warehouseId) {
        // SQL SELECT statement to retrieve a warehouse by ID
        ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();
        String query = "SELECT * FROM Warehouse WHERE WarehouseId = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, warehouseId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Warehouse warehouse = new Warehouse();
                    warehouse.setWarehouseId(resultSet.getString("WarehouseId"));
                    warehouse.setWarehouseName(resultSet.getString("WarehouseName"));
                    warehouse.setMaxCapacity(resultSet.getInt("MaxCapacity"));
                    warehouses.add(warehouse);
                }
                return warehouses;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public static ObservableList<Warehouse> getWarehouses() {
        String query = "SELECT * FROM Warehouse";
        ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                Warehouse warehouse = new Warehouse();
                warehouse.setWarehouseId(resultSet.getString("WarehouseId"));
                warehouse.setWarehouseName(resultSet.getString("WarehouseName"));
                warehouse.setMaxCapacity(resultSet.getInt("MaxCapacity"));
                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return warehouses;
    }
    
    //Method to delete a product from a warehouse
    public static boolean removeProductFromWarehouse(String productId, String warehouseId) {
        String query = "DELETE FROM ProductStock WHERE ProductId = ? AND WarehouseId = ?";
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, productId);
            preparedStatement.setString(2, warehouseId);
            
            int rowsDeleted = preparedStatement.executeUpdate();
            
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false;
    }


    //Method to change stock of a product in a warehouse
    public static boolean changeProductStock(String productId, String warehouseId, int newStockCount) {
        String query = "UPDATE ProductStock SET StockCount = ? WHERE ProductId = ? AND WarehouseId = ?";
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            preparedStatement.setInt(1, newStockCount);
            preparedStatement.setString(2, productId);
            preparedStatement.setString(3, warehouseId);
    
            // Get the current stock count for the warehouse
            int currentStockCount = getCurrentStockCount(productId, warehouseId);
    
            // Calculate the new total stock count
            int newTotalStockCount = currentStockCount + newStockCount;
    
            // Check if the stock level exceeds 80% of max capacity
            int maxCapacity = getMaxCapacityForWarehouse(warehouseId);
            double stockPercentage = (double) newTotalStockCount / maxCapacity;
    
            if (stockPercentage >= 0.8) {
                System.out.println("Warning: Stock level exceeds 80% of max capacity for this warehouse.");
            }
    
            if (newTotalStockCount > maxCapacity) {
                System.out.println("Error: Stock level exceeds max capacity for this warehouse.");
                return false; // Abort the stock update
            }
    
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false;
    }

    //Method to get current stock in warehouse through product id and warehouse id
    public static int getCurrentStockCount(String productId, String warehouseId) {
        String query = "SELECT StockCount FROM ProductStock WHERE ProductId = ? AND WarehouseId = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, productId);
            preparedStatement.setString(2, warehouseId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("StockCount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Return 0 if no stock count is found
    }
    
    //Method to get max capacity for warehouse through warehouse id
    public static int getMaxCapacityForWarehouse(String warehouseId) {
        String query = "SELECT MaxCapacity FROM Warehouse WHERE WarehouseId = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, warehouseId);
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("MaxCapacity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0; // Return 0 if no max capacity is found
    }

    public static Warehouse findSingleWarehouseFromId(String warehouseId){
        for (Warehouse warehouse : warehouses) {
            if (warehouse.getWarehouseId().equals(warehouseId)) {
                return warehouse;
            }
        }
        return null;
    }


        public static ObservableList<Warehouse> getWarehousesWithAllProductTypes() {
            ObservableList<Warehouse> warehouses = FXCollections.observableArrayList();
    
            String query = "SELECT DISTINCT w.WarehouseId, w.WarehouseName " +
                           "FROM Warehouse w " +
                           "WHERE NOT EXISTS " +
                           "(SELECT DISTINCT p.CategoryName " +
                           " FROM Product p " +
                           " WHERE NOT EXISTS " +
                           " (SELECT 1 " +
                           "  FROM ProductStock ps " +
                           "  WHERE ps.WarehouseId = w.WarehouseId " +
                           "  AND ps.ProductId = p.ProductId " +
                           "  AND IsDeleted = 0))";
    
            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
                ResultSet resultSet = preparedStatement.executeQuery();
    
                while (resultSet.next()) {
                    String warehouseId = resultSet.getString("WarehouseId");
                    String warehouseName = resultSet.getString("WarehouseName");
    
                    Warehouse warehouse = new Warehouse(warehouseId, warehouseName, WarehouseDAO.findSingleWarehouseFromId(warehouseId).getMaxCapacity());
                    warehouses.add(warehouse);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            return warehouses;
        }
        

      

    
    
    
    


}

