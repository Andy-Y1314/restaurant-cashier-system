module phoenixoriental.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens phoenixoriental.restaurant to javafx.fxml;
    exports phoenixoriental.restaurant;
}