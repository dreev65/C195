package controller;

import helper.DBAddCustomers;
import helper.JDBC;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Users;

/**
 * FXML Controller for adding customers
 *
 * @author Daniel Reeve
 */
public class AddCustomerController implements Initializable {

    @FXML
    private TextField addNameBox;
    @FXML
    private TextField addAddressBox;
    @FXML
    private TextField addPostalCodeBox;
    @FXML
    private TextField addPhoneBox;
    @FXML
    private ComboBox<String> addCountryBox;
    @FXML
    private ComboBox<String> addDivisionBox;

    private int selectedIndex;
    private int countryIndex = -1;
    private String selectedDivision;
    private int selectedDivisionID = -1;

    /**
     * Sets the country combo box
     */
    public void setCountryComboBox() {
        try {
            DBAddCustomers.getCountryComboBox();
            ObservableList<String> CountryComboBox = DBAddCustomers.getCountryComboBox();

            for (String Country : CountryComboBox) {
                addCountryBox.setItems(CountryComboBox);
                addCountryBox.setPromptText("Select Country");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the index of the selected country from the combo box selection
     * 
     * @return the selected index
     */
    public int getCountryIndex() {
        countryIndex = addCountryBox.getSelectionModel().getSelectedIndex();
        System.out.println("This is the country index: " + (countryIndex + 1));
        return (countryIndex + 1);
    }

    /**
     * Set division combo box
     */
    public void setDivisionCombobox() {
        try {
            int selectedIndex = getCountryIndex();
            DBAddCustomers.getDivisionComboBox(this.selectedIndex);
            ObservableList<String> DivisionComboBox = DBAddCustomers.getDivisionComboBox(selectedIndex);

            for (String Division : DivisionComboBox) {
                addDivisionBox.setItems(DivisionComboBox);
                addDivisionBox.setPromptText("Select Division");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the selected division ID from the user selection in the combo box 
     * @return the selected division id
     * @throws Exception ignore
     */
    public int getSelectedDivisionID() throws Exception {
        selectedDivision = addDivisionBox.getSelectionModel().getSelectedItem();
        selectedDivisionID = DBAddCustomers.getDivisionID(selectedDivision);
        System.out.println("This is the selected division id: " + selectedDivisionID);
        return selectedDivisionID;
    }

//Save Button//

    /**
     * Used to save the customer information in the database 
     * 
     * @param actionEvent initiated when the save button used
     * @throws SQLException throws stack trace
     * @throws IOException ignore
     * @throws Exception ignore
     */
    public void saveButton(ActionEvent actionEvent) throws SQLException, IOException, Exception {

        try {
            String customerName = addNameBox.getText();
            String customerAddress = addAddressBox.getText();
            addCountryBox.getSelectionModel();
            addDivisionBox.getSelectionModel();
            String customerPostalCode = addPostalCodeBox.getText();
            String customerPhone = addPhoneBox.getText();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to save this customer?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {
                if (addCountryBox.getSelectionModel().isEmpty()) {
                    Alert countryAlert = new Alert(Alert.AlertType.ERROR, "Country has not been selected.");
                    countryAlert.showAndWait();
                } else if (addDivisionBox.getSelectionModel().isEmpty()) {
                    Alert countryAlert = new Alert(Alert.AlertType.ERROR, "Division has not been selected.");
                    countryAlert.showAndWait();
                } else if (customerName.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty() || customerPhone.isEmpty()) {
                    Alert emptyAlert = new Alert(Alert.AlertType.ERROR, "Not all fields have been filled.\nPlease check entries.");
                    emptyAlert.showAndWait();
                } else {
                    //Insert into database//
                    try {
                        PreparedStatement ps = JDBC.getConnection().prepareStatement("INSERT INTO customers (Customer_Name, Address, Postal_Code, phone, Create_Date, Created_By, "
                                + "Last_Update, Last_Updated_By, Division_ID) "
                                + "VALUES (?, ?, ?, ?, " + "CURRENT_TIMESTAMP()" + ", ?, " + "CURRENT_TIMESTAMP()" + ", ?, ?)");

                        ps.setString(1, addNameBox.getText());
                        ps.setString(2, addAddressBox.getText());
                        ps.setString(3, addPostalCodeBox.getText());
                        ps.setString(4, addPhoneBox.getText());
                        ps.setString(5, Users.getUsername());
                        ps.setString(6, Users.getUsername());
                        ps.setInt(7, getSelectedDivisionID());

                        ps.executeUpdate();

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                    Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } catch (NumberFormatException e) {
            //Ignore
        }
    }

    /**
     * Cancels the adding an appointment
     * 
     * @param actionEvent cancels the adding an appointment when button is used
     * @throws IOException ignore
     */
    public void cancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customers.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the controller class
     * <br><br>
     * Lambda expression calls the setDivisionComboBoc function if the user selects a country. A lambda function was used because it efficiently handles an event call, 
     * which is what is occurring when the country is selected.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCountryComboBox();

        //lambda expression
        addCountryBox.setOnAction((ActionEvent e) -> {
            setDivisionCombobox();
        });
    }
}
