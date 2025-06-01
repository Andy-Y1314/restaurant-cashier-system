module phoenixoriental.restaurant {
    requires javafx.controls;
    requires javafx.fxml;


    opens phoenixoriental.restaurant to javafx.fxml;
    exports phoenixoriental.restaurant;
}