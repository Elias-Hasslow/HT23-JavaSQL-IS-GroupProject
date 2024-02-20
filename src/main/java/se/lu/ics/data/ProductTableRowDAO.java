package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.ProductTableRow;

public class ProductTableRowDAO {
    private static ObservableList<ProductTableRow> productTableRows = FXCollections.observableArrayList();
    
    static {
        updateProductTableRowsFromDatabase();
    }

    public static void updateProductTableRowsFromDatabase() {
        String query = "SELECT p.ProductId, p.ProductName, ps.Stock FROM Product p INNER JOIN ProductStock ps ON p.ProductId = ps.ProductId WHERE ps.Stock < 50";
        try (Connection connection = ConnectionHandler.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            productTableRows.clear();
            while (resultSet.next()) {
                ProductTableRow productTableRow = new ProductTableRow();
                productTableRow.setProductId(resultSet.getString("ProductId"));
                productTableRow.setProductName(resultSet.getString("ProductName"));
                productTableRow.setStock(resultSet.getInt("Stock"));
                productTableRows.add(productTableRow);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<ProductTableRow> getProductTableRows() {
        return productTableRows;
    }

}
