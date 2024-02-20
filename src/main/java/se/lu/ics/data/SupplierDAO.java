package se.lu.ics.data;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.Product;
import se.lu.ics.models.Supplier;

public class SupplierDAO {
    private static ObservableList<Supplier> suppliers = FXCollections.observableArrayList();

    static {
        updateSuppliersFromDatabase();
    }

    public static ObservableList<Supplier> updateSuppliersFromDatabase() {
        String query = "SELECT * FROM Supplier WHERE IsActive = 1";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            suppliers.clear();
            while (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(resultSet.getString("SupplierId"));
                supplier.setSupplierName(resultSet.getString("SupplierName"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    // Methods

    //Method to add a new supplier, returns true if operation was successful
        public static boolean addSupplier(String supplierName) {
        String insertQuery = "INSERT INTO Supplier (SupplierId, SupplierName, IsActive) VALUES (?, ?, 1)";
        
        try (Connection connection = ConnectionHandler.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            
            String supplierId = Supplier.generateId(); // Generate a unique supplier ID
            if (supplierId == null) {
                return false; // Unable to generate a unique ID
            }
            
            preparedStatement.setString(1, supplierId);
            preparedStatement.setString(2, supplierName);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Method for retrieving a supplier by ID
    public static ObservableList<Supplier> getSupplierById(String supplierId) {
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        String query = "SELECT * FROM Supplier WHERE SupplierId = ? AND IsActive = 1";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
    
            preparedStatement.setString(1, supplierId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setSupplierId(resultSet.getString("SupplierId"));
                    supplier.setSupplierName(resultSet.getString("SupplierName"));
                    suppliers.add(supplier);
                }
                return suppliers;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Method for retreiveing all products by a supplier
    public static ObservableList<Product> getProductsBySupplier(String supplierId) {
        String query = "SELECT * FROM Product WHERE SupplierId = ?";
        ObservableList<Product> products = FXCollections.observableArrayList();
    
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, supplierId);
    
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return products;
    }


    //Method to retieve all suppliers
    public static ObservableList<Supplier> getSuppliers() {
        String query = "SELECT * FROM Supplier WHERE IsActive = 1";
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(resultSet.getString("SupplierId"));
                supplier.setSupplierName(resultSet.getString("SupplierName"));
                suppliers.add(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return suppliers;
    }

    //Method to update supplier, returns true if operation was successful
    public static boolean updateSupplierName(String supplierId, String newSupplierName) {
        String query = "UPDATE Supplier SET SupplierName = ? WHERE SupplierId = ? AND IsActive = 1";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newSupplierName);
            preparedStatement.setString(2, supplierId);
            
            int rowsAffected = preparedStatement.executeUpdate();
            
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }

    //Method to update supplier name
    public static boolean updateProductInfoBySupplier(String supplierId, String newSupplierName) {
        String query = "UPDATE Product SET SupplierName = ? WHERE SupplierId = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, newSupplierName);
            preparedStatement.setString(2, supplierId);
            
            int rowsUpdated = preparedStatement.executeUpdate();
            
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Method to delete a supplier
    public static boolean deleteSupplier(String supplierId) {
        String query = "UPDATE Supplier SET IsActive = 0 WHERE SupplierId = ?";
        
        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, supplierId);
    
            int rowsUpdated = preparedStatement.executeUpdate();
    
            // Check if the update was successful (rowsUpdated should be 1)
            if (rowsUpdated == 1) {
                return true; // Supplier soft-deleted successfully
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false; // Supplier not found or deletion failed
    }

    public static Supplier findSingleSupplierFromName(String suplierName) {
        for(Supplier supplier : suppliers) {
            if(supplier.getSupplierName().equals(suplierName)) {
                return supplier;
            }
        }
        return null;
    }


        //Method to retrieve all suppliers by warehouse
        public static ObservableList<Supplier> getSuppliersByWarehouse(String warehouseId) {
            ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    
            String query = "SELECT DISTINCT s.SupplierId, s.SupplierName FROM Supplier s INNER JOIN ProductStock p ON s.SupplierId = p.SupplierId WHERE p.WarehouseId = ?";
    
            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, warehouseId);
    
                ResultSet resultSet = preparedStatement.executeQuery();
    
                while (resultSet.next()) {
                    String supplierId = resultSet.getString("SupplierId");
                    String supplierName = resultSet.getString("SupplierName");
    
                    Supplier supplier = new Supplier(supplierId, supplierName);
                    suppliers.add(supplier);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            return suppliers;
        }

        public static ObservableList<Supplier> getInactiveSuppliers() {
            String query = "SELECT * FROM Supplier WHERE IsActive = 0";
            ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
            try(Connection connection = ConnectionHandler.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setSupplierId(resultSet.getString("SupplierId"));
                    supplier.setSupplierName(resultSet.getString("SupplierName"));
                    suppliers.add(supplier);
                }
                return suppliers;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static ObservableList<Supplier> getActiveAndInActive() {
            String query = "SELECT * FROM Supplier";
            ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
            try(Connection connection = ConnectionHandler.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setSupplierId(resultSet.getString("SupplierId"));
                    supplier.setSupplierName(resultSet.getString("SupplierName"));
                    suppliers.add(supplier);
                }
                return suppliers;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return suppliers;
        }

    

}

