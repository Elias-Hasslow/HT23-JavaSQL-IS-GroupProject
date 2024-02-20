package se.lu.ics.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import se.lu.ics.data.ProductDAO;
import se.lu.ics.data.ProductTableRowDAO;
import se.lu.ics.models.ProductTableRow;
import se.lu.ics.models.StockSummary;

public class MiscController implements Initializable {

        @FXML
        private TableView<ProductTableRow> tableViewStockBelow50;
        @FXML
        private TableColumn<ProductTableRow, String> tableColumnStockBelow50Id;
        @FXML
        private TableColumn<ProductTableRow, String> tableColumnStockBelow50Name;
        @FXML
        private TableColumn<ProductTableRow, String> tableColumnStockBelow50Stock;
        @FXML
        private Label labelAmountOfProducts;
        @FXML
        private TableView<StockSummary> tableViewStockSummary;
        @FXML
        private TableColumn<StockSummary, String> tableColumnStockSummaryId;
        @FXML
        private TableColumn<StockSummary, String> tableColumnStockSummaryName;
        @FXML
        private TableColumn<StockSummary, String> tableColumnStockSummaryStock;
        @FXML
        private TableColumn<StockSummary, String> tableColumnStockSummaryWarehouseId;
        @FXML
        private TableColumn<StockSummary, String> tableColumnStockSummaryCategoryName;
        @FXML
        private Button buttonUpdate;
        @FXML
        private Button buttonOpenExcel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        tableColumnStockBelow50Id.setCellValueFactory(new PropertyValueFactory<>("ProductId"));
        tableColumnStockBelow50Name.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        tableColumnStockBelow50Stock.setCellValueFactory(new PropertyValueFactory<>("Stock"));

        tableViewStockBelow50.setItems(ProductTableRowDAO.getProductTableRows());

        labelAmountOfProducts.setText(String.valueOf(ProductDAO.getTotalNumberOfProducts()));
    }

    public void buttonUpdate_OnClick() {
        ProductTableRowDAO.updateProductTableRowsFromDatabase();
        tableViewStockBelow50.setItems(ProductTableRowDAO.getProductTableRows());
        labelAmountOfProducts.setText(String.valueOf(ProductDAO.getTotalNumberOfProducts()));


    }
    public void buttonOpenExcel_OnClick() {
System.out.println("buttonOpenExcel_OnClick");
    try {
        String path="src/main/resources/excel/SQLExcel.xlsx";

        File file = new File(path);
        if(file.exists()) {

            Process pro = Runtime.getRuntime().exec("open " + path);
            pro.waitFor();
        } else {
        } 
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
}
