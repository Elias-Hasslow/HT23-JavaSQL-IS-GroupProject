package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import se.lu.ics.models.ProductCategory;

public class CategoryDAO {
    private static ObservableList<ProductCategory> categories = FXCollections.observableArrayList();

    static {
        updateCategoriesFromDatabase();
    }

    public static void updateCategoriesFromDatabase() {
        String query = "SELECT DISTINCT(CategoryName) FROM ProductCategory";
        try (Connection connection = ConnectionHandler.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            categories.clear();
            while (resultSet.next()) {
                ProductCategory category = new ProductCategory();
                category.setCategoryName(resultSet.getString("CategoryName"));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
}
