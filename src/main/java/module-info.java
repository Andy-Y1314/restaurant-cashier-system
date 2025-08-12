module phoenixoriental.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jdk.compiler;


    opens phoenixoriental.restaurant to javafx.fxml;
    exports phoenixoriental.restaurant;
}