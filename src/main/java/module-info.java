module com.codefornature {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.codefornature to javafx.fxml;
    exports com.codefornature;
}