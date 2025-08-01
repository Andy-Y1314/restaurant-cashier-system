package phoenixoriental.restaurant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class MainFromController implements Initializable {
    @FXML
    private Button customers_btn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clearBtn;

    @FXML
    private TableColumn<ProductData, String> inventory_col_date;

    @FXML
    private TableColumn<ProductData, String> inventory_col_price;

    @FXML
    private TableColumn<ProductData, String> inventory_col_productID;

    @FXML
    private TableColumn<ProductData, String> inventory_col_productName;

    @FXML
    private TableColumn<ProductData, String> inventory_col_status;

    @FXML
    private TableColumn<ProductData, String> inventory_col_stock;

    @FXML
    private TableColumn<ProductData, String> inventory_col_type;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private TableView<ProductData> inventory_tableView;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button menu_btn;

    @FXML
    private Label username;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TextField inventory_price;

    @FXML
    private ComboBox<String> inventory_status;

    @FXML
    private ComboBox<String> inventory_type;

    private Alert alert;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    public void inventoryAddBtn() {
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || Data.path== null) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String checkProdID = "SELECT prod_id FROM product WHERE prod_id = '" + inventory_productID.getText() + "'";

            connect = Database.connectDB();

            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(inventory_productID.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    String insertData = "INSERT INTO product " +
                            "(prod_id, prod_name, type, stock, price, status, image, date) " +
                            "VALUES(?,?,?,?,?,?,?,?)";

                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, inventory_productID.getText());
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(3, inventory_type.getSelectionModel().getSelectedItem());
                    prepare.setString(4, inventory_stock.getText());
                    prepare.setString(5, inventory_price.getText());
                    prepare.setString(6, inventory_status.getSelectionModel().getSelectedItem());

                    String path = Data.path;
                    path = path.replace("\\", "\\\\");
                    prepare.setString(7, path);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setString(8, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    inventoryShowData();
                }
            } catch(Exception e) {e.printStackTrace();}
        }
    }

    public void inventoryImportBtn() {
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("Open Image File", "png", "*jpg"));

        File file = openFile.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            Data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 124, 141, false, true);

            inventory_imageView.setImage(image);
        }

    }

    //Merge all data
    public ObservableList<ProductData> inventoryDataList() {
        ObservableList<ProductData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM product";

        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ProductData prodData;

            while (result.next()) {
                prodData = new ProductData(result.getInt("id"), result.getString("prod_id"),
                        result.getString("prod_name"), result.getString("type"),
                        result.getInt("stock"), result.getDouble("price"),
                        result.getString("status"), result.getString("image"),
                        result.getDate("date"));

                listData.add(prodData);

            }
        } catch(Exception e) {e.printStackTrace();}
        return listData;
    }

    //To show data on the table
    private ObservableList<ProductData> inventoryListData;
    public void inventoryShowData() {
        inventoryListData = inventoryDataList();

        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableView.setItems(inventoryListData);
    }

    private String[] typeList = {"Meals", "Drinks"};

    public void inventoryTypeList() {
        List<String> typeL = new ArrayList<>();

        for (String data : typeList) {
            typeL.add(data);
        }
        ObservableList<String> listData = FXCollections.observableArrayList(typeL);
        inventory_type.setItems(listData);
    }

    private String[] statusList = {"Available", "Unavailable"};

    public void inventoryStatusList() {
        List<String> statusL = new ArrayList<>();

        for (String data : statusList) {
            statusL.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList(statusL);
        inventory_status.setItems(listData);
    }

    public void logout() {
        try {
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                //To hide the main-form
                logout_btn.getScene().getWindow().hide();

                //Link the login form and show it
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-menu.fxml")));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                stage.setTitle("Restaurant Management System");

                stage.setScene(scene);
                stage.show();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void displayUserName() {
        String user = Data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUserName();
        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();
    }
}
