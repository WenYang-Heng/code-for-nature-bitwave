module com.codefornature {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.codefornature to javafx.fxml;
    exports com.codefornature;
    exports com.codefornature.model;
    opens com.codefornature.model to javafx.fxml;
    exports com.codefornature.dao;
    opens com.codefornature.dao to javafx.fxml;
}