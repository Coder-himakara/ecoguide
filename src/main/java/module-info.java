module com.ecosupport.ecoguide {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.ecosupport.ecoguide to javafx.fxml;
    exports com.ecosupport.ecoguide;
}