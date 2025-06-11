module phoenixoriental.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens phoenixoriental.restaurant to javafx.fxml;
    exports phoenixoriental.restaurant;
}