module com.codefornature {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;


    requires org.joda.time;

    opens com.codefornature to javafx.fxml;
    exports com.codefornature;
    exports com.codefornature.model;
    opens com.codefornature.model to javafx.fxml;
    exports com.codefornature.dao;
    opens com.codefornature.dao to javafx.fxml;
    exports com.codefornature.utils;
    opens com.codefornature.utils to javafx.fxml;
}