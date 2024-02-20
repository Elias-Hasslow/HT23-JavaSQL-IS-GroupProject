module se.lu.ics {
    exports se.lu.ics.controllers;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens se.lu.ics.controllers to javafx.fxml;
    opens se.lu.ics.models to javafx.base;
    exports se.lu.ics;
}