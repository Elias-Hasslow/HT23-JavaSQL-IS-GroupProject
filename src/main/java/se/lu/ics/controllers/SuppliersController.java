package se.lu.ics.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.data.SupplierDAO;
import se.lu.ics.data.WarehouseDAO;
import se.lu.ics.models.Supplier;
import se.lu.ics.models.Warehouse;

public class SuppliersController implements Initializable{

    @FXML
    private TextField textFieldSearchId;
    @FXML
    private Button buttonSupplierSearch;
    @FXML
    private TableView<Supplier> tableViewSupplier;
    @FXML
    private TableColumn<Supplier, String> tableColumnSupplierName;
    @FXML
    private TableColumn<Supplier, String> tableColumnSupplierId;
    @FXML
    private Button buttonShowAll;
    @FXML
    private TextField textFieldName;
    @FXML
    private Button buttonAddSupplier;
    @FXML
    private Button buttonUpdateSupplier;
    @FXML
    private ComboBox<String> comboBoxSupplierIdUpdate;
    @FXML
    private Button buttonDeleteSupplier;
    @FXML
    private ComboBox<String> comboBoxSupplierIdDelete;
    @FXML
    private Button buttonFilterByWarehouse;
    @FXML
    private ComboBox<String> comboBoxWarehouseId;
    @FXML
    private Button buttonShowInActive;
    @FXML
    private Label labelError;


        @Override
    public void initialize(URL url, ResourceBundle rb) {

        tableColumnSupplierName.setCellValueFactory(new PropertyValueFactory<Supplier, String>("SupplierName"));
        tableColumnSupplierId.setCellValueFactory(new PropertyValueFactory<Supplier, String>("SupplierId"));

        tableViewSupplier.setItems(SupplierDAO.getSuppliers());

        for(Supplier supplier : SupplierDAO.getSuppliers()) {
            comboBoxSupplierIdUpdate.getItems().add(supplier.getSupplierId());
        }

        for(Supplier supplier : SupplierDAO.getSuppliers()) {
            comboBoxSupplierIdDelete.getItems().add(supplier.getSupplierId());
        }

        for(Warehouse warehouse : WarehouseDAO.getWarehouses()) {
            comboBoxWarehouseId.getItems().add(warehouse.getWarehouseId());
        }

    }

    public void buttonSupplierSearch_OnClick() {
        tableViewSupplier.setItems(SupplierDAO.getSupplierById(textFieldSearchId.getText()));
    }

    public void buttonShowAll_OnClick() {
        SupplierDAO.updateSuppliersFromDatabase();
        tableViewSupplier.setItems(SupplierDAO.getSuppliers());
    }

    public void buttonAddSupplier_OnClick() {
        SupplierDAO.addSupplier(textFieldName.getText());
        tableViewSupplier.setItems(SupplierDAO.getSuppliers());
        if(textFieldName.getText().isEmpty()) {
            labelError.setText("Make sure to enter a name");
        }
    }

    public void buttonUpdateSupplier_OnClick() {
        SupplierDAO.updateSupplierName(comboBoxSupplierIdUpdate.getValue(), textFieldName.getText());
        tableViewSupplier.setItems(SupplierDAO.getSuppliers());
        if(comboBoxSupplierIdUpdate.getValue() == null) {
            labelError.setText("Make sure to select an ID to update");
        }
    }
    
    public void buttonDeleteSupplier_OnClick() {
        SupplierDAO.deleteSupplier(comboBoxSupplierIdDelete.getValue());
        tableViewSupplier.setItems(SupplierDAO.getSuppliers());
        if(comboBoxSupplierIdDelete.getValue() == null) {
            labelError.setText("Make sure to select an ID to delete");
        }
    }

    public void buttonFilterByWarehouse_OnClick() {
        tableViewSupplier.setItems(SupplierDAO.getSuppliersByWarehouse(comboBoxWarehouseId.getValue()));
    } 

    public void buttonShowInActive_OnClick() {
        tableViewSupplier.setItems(SupplierDAO.getInactiveSuppliers());
    }
}
