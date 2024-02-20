package se.lu.ics.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.data.WarehouseDAO;
import se.lu.ics.models.Warehouse;

public class WarehousesController implements Initializable {

    @FXML
    private TextField textFieldSearchId;
    @FXML
    private Button buttonWarehouseSearch;
    @FXML
    private TableView<Warehouse> tableViewWarehouse;
    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseName;
    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseId;
    @FXML
    private TableColumn<Warehouse, String> tableColumnWarehouseMaxCapacity;
    @FXML
    private Button buttonShowAll;
    @FXML
    private Button buttonShowAllProducts;
    @FXML
    private Button buttonAdd;
    @FXML
    private TextField textFieldWarehouseName;
    @FXML
    private TextField textFieldMaxCap;
    @FXML
    private Label labelError;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tableColumnWarehouseName.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("WarehouseName"));
        tableColumnWarehouseId.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("WarehouseId"));
        tableColumnWarehouseMaxCapacity.setCellValueFactory(new PropertyValueFactory<Warehouse, String>("MaxCapacity"));

        tableViewWarehouse.setItems(WarehouseDAO.getWarehouses());




    }

    public void buttonWarehouseSearch_OnClick() {
        tableViewWarehouse.setItems(WarehouseDAO.getWarehouseById(textFieldSearchId.getText()));
    }

    public void buttonShowAll_OnClick() {
        tableViewWarehouse.setItems(WarehouseDAO.getWarehouses());
    }

    public void buttonShowAllProducts_OnClick() {
        tableViewWarehouse.setItems(WarehouseDAO.getWarehousesWithAllProductTypes());
    }

    public void buttonAdd_OnClick() {
        int maxCap = Integer.parseInt(textFieldMaxCap.getText());
        WarehouseDAO.addWarehouse(textFieldWarehouseName.getText(), maxCap);
        tableViewWarehouse.setItems(WarehouseDAO.getWarehouses());
    }
    
}
