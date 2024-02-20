package se.lu.ics.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import se.lu.ics.data.MetadataDAO;

public class MetadataController implements Initializable {

    @FXML 
    private TableView<String> tableViewMetaTableNames;
    @FXML
    private TableColumn<String, String> tableColumnMetaTableNames;
    @FXML
    private TableView<String> tableViewMetaPKNames;
    @FXML
    private TableColumn<String, String> tableColumnMetaPKNames;
    @FXML
    private TableView<String> tableViewMetaFKNames;
    @FXML
    private TableColumn<String, String> tableColumnMetaFKNames;
    @FXML
    private TableView<String> tableViewMetaColumnNames;
    @FXML
    private TableColumn<String, String> tableColumnMetaColumnNames;
    @FXML
    private Label labelMetadatResult;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tableColumnMetaTableNames.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableColumnMetaPKNames.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableColumnMetaFKNames.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        tableColumnMetaColumnNames.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        tableViewMetaTableNames.setItems(MetadataDAO.getTableNames());
        tableViewMetaPKNames.setItems(MetadataDAO.getPrimaryKeyNames());
        tableViewMetaFKNames.setItems(MetadataDAO.getForeignKeyNames());
        tableViewMetaColumnNames.setItems(MetadataDAO.getColumnNames());
        labelMetadatResult.setText(MetadataDAO.getTableWithMostRowsInfo());
    }


}
