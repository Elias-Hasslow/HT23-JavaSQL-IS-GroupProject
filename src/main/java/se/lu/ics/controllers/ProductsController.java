package se.lu.ics.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.data.ProductDAO;
import se.lu.ics.models.Product;

public class ProductsController implements Initializable {

    @FXML
    private TextField textFieldSearchName;
    @FXML
    private Button buttonProductSearch;
    @FXML
    private TableView<Product> tableViewProduct;
    @FXML
    private TableColumn<Product, String> tableColumnProductName;
    @FXML
    private TableColumn<Product, String> tableColumnProductId;
    @FXML
    private TableColumn<Product, String> tableColumnProductWarehouseId;
    @FXML
    private TableColumn<Product, String> tableColumnProductSupplierId;
    @FXML
    private TableColumn<Product, String> tableColumnProductCategory;
    @FXML
    private Button buttonAddProduct;
    @FXML
    private TextField textFieldAddProductName;
    @FXML
    private TextField textFieldAddProductWarehouseId;
    @FXML
    private TextField textFieldAddProductSupplierId;
    @FXML
    private TextField textFieldAddProductCategory;
    @FXML
    private Button buttonShowAll;
    @FXML
    private Button buttonFilterByCategory;
    @FXML
    private Button buttonFilterBySupplier;
    @FXML
    private Button buttonFilterByWarehouse;
    @FXML
    private Button buttonUpdateProduct;
    @FXML
    private ComboBox<String> comboBoxProductIdUpdate;
    @FXML
    private ComboBox<String> comboBoxProductIdDelete;
    @FXML
    private Button buttonDeleteProduct;
    @FXML
    private Button buttonStock;
    @FXML
    private ComboBox<String> comboBoxStock;
    @FXML
    private Label labelStock;
    @FXML
    private Label labelError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tableColumnProductName.setCellValueFactory(new PropertyValueFactory<Product, String>("ProductName"));
        tableColumnProductId.setCellValueFactory(new PropertyValueFactory<Product, String>("ProductId"));
        tableColumnProductWarehouseId.setCellValueFactory(new PropertyValueFactory<Product, String>("WarehouseId"));
        tableColumnProductSupplierId.setCellValueFactory(new PropertyValueFactory<Product, String>("SupplierId"));
        tableColumnProductCategory.setCellValueFactory(new PropertyValueFactory<Product, String>("CategoryName"));

        tableViewProduct.setItems(ProductDAO.getProducts());

        for (Product product : ProductDAO.getProducts()) {
            comboBoxProductIdUpdate.getItems().add(product.getProductId());
        }

        for (Product product : ProductDAO.getProducts()) {
            comboBoxProductIdDelete.getItems().add(product.getProductId());
        }

        for (Product product : ProductDAO.getProducts()) {
            comboBoxStock.getItems().add(product.getProductId());
        }

    }

    public void buttonProductSearch_OnClick() {
        tableViewProduct.setItems(ProductDAO.getProductById(textFieldSearchName.getText()));
    }

    public void buttonAddProduct_OnClick() {
        try {
            String productName = textFieldAddProductName.getText();
            String supplierId = textFieldAddProductSupplierId.getText();
            String warehouseId = textFieldAddProductWarehouseId.getText();
            String category = textFieldAddProductCategory.getText();
            int initialStock = 0;
    
            if (validateInputs(productName, supplierId, warehouseId, category)) {
                boolean success = ProductDAO.createProduct(productName, supplierId, warehouseId, category, initialStock);
    
                if (success) {
                    tableViewProduct.setItems(ProductDAO.getProducts());
                    for (Product product : ProductDAO.getProducts()) {
                        comboBoxProductIdUpdate.getItems().add(product.getProductId());
                        comboBoxProductIdDelete.getItems().add(product.getProductId());
                        comboBoxStock.getItems().add(product.getProductId());
                    }
                } else {
                    labelError.setText("Make sure to enter name, supplier ID, warehouse ID and category.");
                }
            } else {
                // Handle invalid input data
                // Provide user feedback or display an error message
            }
        } catch (Exception e) {
            // Handle all exceptions with a generic error message
            labelError.setText("An error occurred. Please try again.");
        }
    }
    

    private boolean validateInputs(String productName, String supplierId, String warehouseId, String category) {
        // Add validation logic here
        // Return true if inputs are valid, otherwise false
        // Example: Check if fields are not empty and if IDs exist in their respective
        // tables
        return !productName.isEmpty() && !supplierId.isEmpty() && !warehouseId.isEmpty() && !category.isEmpty();
    }

    public void buttonShowAll_OnClick() {
        ProductDAO.updateProductsFromDatabase();
        tableViewProduct.setItems(ProductDAO.getProducts());
    }

    public void buttonFilterByCategory_OnClick() {
        try {
        tableViewProduct.setItems(ProductDAO.getProductsByCategory(textFieldAddProductCategory.getText()));
         } catch (Exception e) {
            labelError.setText("An error occurred. Make sure to enter category name.");
         }
    }

    public void buttonFilterBySupplier_OnClick() {
        tableViewProduct.setItems(ProductDAO.getProductsBySupplier(textFieldAddProductSupplierId.getText()));
    }

    public void buttonFilterByWarehouse_OnClick() {
        tableViewProduct.setItems(ProductDAO.getProductsInWarehouse(textFieldAddProductWarehouseId.getText()));
    }

    public void buttonUpdateProduct_OnClick() {
        String productName = textFieldAddProductName.getText();
        String supplierId = textFieldAddProductSupplierId.getText();
        String warehouseId = textFieldAddProductWarehouseId.getText();
        String category = textFieldAddProductCategory.getText();

        if (ProductDAO.updateProduct(comboBoxProductIdUpdate.getValue(), productName, supplierId, warehouseId,
                category)) {
            tableViewProduct.setItems(ProductDAO.getProducts());
        } else {
            labelError.setText("Make sure to enter an existing supplier, warehouse or category");
        }
    }

    public void buttonDeleteProduct_OnClick() {
        try {
            if (ProductDAO.deleteProductById(comboBoxProductIdDelete.getValue())) {
                tableViewProduct.setItems(ProductDAO.getProducts());
                comboBoxProductIdUpdate.getItems().remove(comboBoxProductIdDelete.getValue());
                comboBoxProductIdDelete.getItems().remove(comboBoxProductIdDelete.getValue());
                comboBoxStock.getItems().remove(comboBoxProductIdDelete.getValue());
            } else {
                labelError.setText("Make sure to select the ID of the product to delete");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void buttonStock_OnClick() {
        labelStock.setText(String.valueOf(ProductDAO.getProductStock(comboBoxStock.getValue())));
    }

 

}
