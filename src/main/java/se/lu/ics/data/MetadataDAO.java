package se.lu.ics.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MetadataDAO {
    private static ObservableList<String> tableNames = FXCollections.observableArrayList();
    private static ObservableList<String> primaryKeyNames = FXCollections.observableArrayList();
    private static ObservableList<String> foreignKeyNames = FXCollections.observableArrayList();
    private static ObservableList<String> columnNames = FXCollections.observableArrayList();

    static {
        getAllTableNames();
        getPrimaryKeyNamesForTable();
        getForeignKeyNamesForTable();
        getColumnNamesForTable();
    }

    public static ObservableList<String> getTableNames() {
        return tableNames;
    }

    public static ObservableList<String> getPrimaryKeyNames() {
        return primaryKeyNames;
    }

    public static ObservableList<String> getForeignKeyNames() {
        return foreignKeyNames;
    }

    public static ObservableList<String> getColumnNames() {
        return columnNames;
    }




    // Show names of all tables in the products
        public static ObservableList<String> getAllTableNames() {
            String query = "SELECT table_name = t.name FROM sys.tables AS t INNER JOIN sys.schemas AS s ON t.[schema_id] = s.[schema_id]";
            try (Connection connection = ConnectionHandler.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString("table_name"));
                }
                return tableNames;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return tableNames;
        }

        //Get all primary keys from metadata
        public static ObservableList<String> getPrimaryKeyNamesForTable() {
            String query = "SELECT i.name AS primary_key_name FROM sys.indexes i WHERE i.is_primary_key = 1";
            try (Connection connection = ConnectionHandler.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    primaryKeyNames.add(resultSet.getString("primary_key_name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return primaryKeyNames;
        }

        //Get all foreign keys from metadata
        public static ObservableList<String> getForeignKeyNamesForTable() {
            String query = "SELECT name AS foreign_key_name FROM sys.foreign_keys";
            try (Connection connection = ConnectionHandler.getConnection()) {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    foreignKeyNames.add(resultSet.getString("foreign_key_name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return foreignKeyNames;
        }

        //Method to return the table with most rows and amount of rows
        public static String getTableWithMostRowsInfo() {
            String result = "";
        
            String query = "SELECT TOP 1 t.name AS table_name, SUM(p.rows) AS row_count " +
                           "FROM sys.tables t " +
                           "INNER JOIN sys.partitions p ON t.object_id = p.object_id " +
                           "INNER JOIN sys.schemas s ON t.schema_id = s.schema_id " +
                           "WHERE t.type_desc = 'USER_TABLE' " +
                           "GROUP BY t.name " +
                           "ORDER BY SUM(p.rows) DESC";
        
            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
        
                if (resultSet.next()) {
                    String tableName = resultSet.getString("table_name");
                    int rowCount = resultSet.getInt("row_count");
                    result = "The table with most rows is " + tableName + " with " + rowCount + " rows";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
            return result;
        }
        

        public static ObservableList<String> getColumnNamesForTable() {
            String query = "SELECT COLUMN_NAME " +
                           "FROM INFORMATION_SCHEMA.COLUMNS " +
                           "WHERE TABLE_NAME = 'Product'"; // Modify the query to get column names for the 'Product' table
        
            try (Connection connection = ConnectionHandler.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
        
                while (resultSet.next()) {
                    String columnName = resultSet.getString("COLUMN_NAME");
                    columnNames.add(columnName); // Add each column name to the ObservableList
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        
            return columnNames;
        }
    
}