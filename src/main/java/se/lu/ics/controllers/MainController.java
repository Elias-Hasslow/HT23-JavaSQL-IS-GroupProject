    package se.lu.ics.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class MainController implements Initializable {

        @FXML
        private Tab tabProducts;

        @Override
        public void initialize(URL url, ResourceBundle rb) {

            FXMLLoader loader = new FXMLLoader();
            try {
                AnchorPane anch1 = loader.load(getClass().getResource("/fxml/Products.fxml").openStream());
            }   catch (Exception e) {
                e.printStackTrace();
            }
        }


   
    }
       