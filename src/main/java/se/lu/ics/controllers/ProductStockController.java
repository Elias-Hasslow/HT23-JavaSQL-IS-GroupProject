package se.lu.ics.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.data.ProductStockDAO;
import se.lu.ics.data.WarehouseDAO;
import se.lu.ics.models.ProductStock;
import se.lu.ics.models.Warehouse;

public class ProductStockController implements Initializable{

    @FXML
    private TextField textFieldSearchProductStock;
    @FXML
    private Button buttonProductStockSearch;
    @FXML
    private TableView<ProductStock> tableViewProductStock;
    @FXML
    private TableColumn<ProductStock, String> tableColumnProductStockId;
    @FXML
    private TableColumn<ProductStock, String> tableColumnProductId;
    @FXML
    private TableColumn<ProductStock, String> tableColumnWarehouseId;
    @FXML
    private TableColumn<ProductStock, String> tableColumnStock;
    @FXML
    private TableColumn<ProductStock, String> tableColumnSupplierId;
    @FXML
    private ComboBox<String> comboBoxProductStockID;
    @FXML
    private ComboBox<String> comboBoxWarehouseId;
    @FXML
    private TextField textFieldStockAmount;
    @FXML
    private Button buttonStockAdd;
    @FXML
    private Button buttonStockRemove;
    @FXML 
    private Button buttonShowAll;
    @FXML
    private TextField textFieldCategoryName;
    @FXML
    private Button buttonFilterByCategory;
    @FXML 
    private Label labelShowStockCategory;
    @FXML
    private ComboBox<String> comboBoxProductStockId;
    @FXML
    private Button buttonRemoveProductStock;
    @FXML
    private TextField textFieldAggregationWarehouseId;
    @FXML
    private TextField textFieldAggregationCategoryName;
    @FXML
    private TextField textFieldAggregationProductName;
    @FXML
    private Button buttonAggregationSearch;
    @FXML
    private Label labelAggregationResult;   
    @FXML
    private Button buttonFilterWarehouse;
    @FXML
    private ComboBox<String> comboBoxFilterWarehouse;
    @FXML
    private Label labelError;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        tableColumnProductStockId.setCellValueFactory(new PropertyValueFactory<ProductStock, String>("ProductStockId"));
        tableColumnProductId.setCellValueFactory(new PropertyValueFactory<ProductStock, String>("ProductId"));
        tableColumnWarehouseId.setCellValueFactory(new PropertyValueFactory<ProductStock, String>("WarehouseId"));
        tableColumnStock.setCellValueFactory(new PropertyValueFactory<ProductStock, String>("StockCount"));
        tableColumnSupplierId.setCellValueFactory(new PropertyValueFactory<ProductStock, String>("SupplierId"));

        tableViewProductStock.setItems(ProductStockDAO.getProductStocks());



        for(ProductStock productStock : ProductStockDAO.getProductStocks()) {
            String productStockId = Integer.toString(productStock.getProductStockId());
            comboBoxProductStockID.getItems().add(productStockId);
        }

        for(ProductStock productStock : ProductStockDAO.getProductStocks()) {
            String productStockId = Integer.toString(productStock.getProductStockId());
            comboBoxProductStockId.getItems().add(productStockId);
        }

        for(Warehouse warehouse : WarehouseDAO.getWarehouses()) {
            String warehouseId = warehouse.getWarehouseId();
            comboBoxFilterWarehouse.getItems().add(warehouseId);    
        }


    }

    public void buttonProductStockSearch_OnClick() {
        tableViewProductStock.setItems(ProductStockDAO.getProductStockById(textFieldSearchProductStock.getText()));
    }

    public void buttonStockAdd_OnClick() {
        int stockAmount = Integer.parseInt(textFieldStockAmount.getText());
        ProductStockDAO.addStockToProductStock(comboBoxProductStockID.getValue(), stockAmount);
        if(stockAmount + ProductStockDAO.getCurrentStockCount(comboBoxProductStockID.getValue()) > 0.8 * ProductStockDAO.findWarehouseThroughStockId(comboBoxProductStockID.getValue()).getMaxCapacity() && stockAmount + ProductStockDAO.getCurrentStockCount(comboBoxProductStockID.getValue()) <= ProductStockDAO.findWarehouseThroughStockId(comboBoxProductStockID.getValue()).getMaxCapacity()) {
        labelError.setText("Stock amount is above 80% of the warehouse max capacity");
        }      
        ProductStockDAO.updateProductStockFromDatabase();
        tableViewProductStock.setItems(ProductStockDAO.getProductStocks());
    }

    public void buttonShowAll_OnClick() {
        ProductStockDAO.updateProductStockFromDatabase();
        tableViewProductStock.setItems(ProductStockDAO.getProductStocks());
    }

    public void buttonFilterByCategory_OnClick() {
        String stockAmount = String.valueOf(ProductStockDAO.showStockForCategory(textFieldCategoryName.getText()));
        labelShowStockCategory.setText(stockAmount);
    }

    public void buttonRemoveProductStock_OnClick() {
        ProductStockDAO.removeProductFromWarehouse(comboBoxProductStockId.getValue());
        ProductStockDAO.updateProductStockFromDatabase();
        tableViewProductStock.setItems(ProductStockDAO.getProductStocks());
    }



 
    public void buttonAggregationSearch_OnClick() {
        String warehouseId = textFieldAggregationWarehouseId.getText();
        String categoryName = textFieldAggregationCategoryName.getText();
        String productName = textFieldAggregationProductName.getText();
        String result = ProductStockDAO.getStockInfo(warehouseId, categoryName, productName);
        labelAggregationResult.setText(result);
    }


    public void buttonFilterWarehouse_OnClick() {
        tableViewProductStock.setItems(ProductStockDAO.getWarehousesById(comboBoxFilterWarehouse.getValue()));
    }

}


