module com.sudombia {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.sudombia to javafx.fxml;
    opens com.sudombia.controller to javafx.fxml;
    
    exports com.sudombia;
}
