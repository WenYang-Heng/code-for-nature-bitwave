module com.codefornature {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    //requires mysql.connector.java;



    opens com.codefornature to javafx.fxml;
    exports com.codefornature;
}