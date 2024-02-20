package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.StockSummary;

public class StockSummaryDAO {


     public static ObservableList<StockSummary> getStockSummaryForWarehouse(String warehouseId) {
        ObservableList<StockSummary> stockSummaries = FXCollections.observableArrayList();

        String query = "SELECT " +
                       "  p.ProductId, " +
                       "  p.ProductName, " +
                       "  p.CategoryName, " +
                       "  w.WarehouseId, " +
                       "  SUM(ps.Stock) AS TotalStock " +
                       "FROM " +
                       "  Product p " +
                       "  INNER JOIN ProductStock ps ON p.ProductId = ps.ProductId " +
                       "  INNER JOIN Warehouse w ON ps.WarehouseId = w.WarehouseId " +
                       "WHERE " +
                       "  w.WarehouseId = ? " +
                       "GROUP BY " +
                       "  p.ProductId, " +
                       "  p.ProductName, " +
                       "  p.CategoryName, " +
                       "  w.WarehouseId";

        try (Connection connection = ConnectionHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, warehouseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                StockSummary stockSummary = new StockSummary();
                stockSummary.setProductId(resultSet.getString("ProductId"));
                stockSummary.setProductName(resultSet.getString("ProductName"));
                stockSummary.setCategoryName(resultSet.getString("CategoryName"));
                stockSummary.setWarehouseId(resultSet.getString("WarehouseId"));
                stockSummary.setStock(resultSet.getInt("TotalStock"));
                stockSummaries.add(stockSummary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stockSummaries;
    }
}
    
