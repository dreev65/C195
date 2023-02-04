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
import model.Customers;
import model.Users;

/**
 * FXML controller for updating customers
 *
 * @author Daniel Reeve
 */
public class UpdateCustomerController implements Initializable {

    @FXML
    private TextField updateNameBox;
    @FXML
    private TextField updateAddressBox;
    @FXML
    private TextField updatePostalCodeBox;
    @FXML
    private ComboBox<String> updateCountryBox;
    @FXML
    private ComboBox<String> updateDivisionBox;
    @FXML
    private TextField updatePhoneBox;

    private int customerID;
    private int countryIndex = -1;
    private String selectedDivision;
    private int selectedDivisionID = -1;
    public Customers selectedCustomer;

    /**
     * Gets customer information from user selection to input into the display fields
     * 
     * @param selectedCustomer the selected customer
     * @throws Exception ignore
     */
    public void transferCustomer(Customers selectedCustomer) throws Exception {
        this.selectedCustomer = selectedCustomer;
        customerID = selectedCustomer.getCustomerID();
        updateNameBox.setText(selectedCustomer.getCustomerName());
        updateAddressBox.setText(selectedCustomer.getAddress());
        updatePostalCodeBox.setText(selectedCustomer.getPostalCode());
        updatePhoneBox.setText(selectedCustomer.getPhone());
        updateCountryBox.setValue(selectedCustomer.getCountry());
        updateDivisionBox.setValue(selectedCustomer.getDivision());
    }

    /**
     * Sets the country box with all countries
     */
    public void setCountryComboBox() {
        try {
            DBAddCustomers.getCountryComboBox();
            ObservableList<String> CountryComboBox = DBAddCustomers.getCountryComboBox();

            for (String Country : CountryComboBox) {
                updateCountryBox.setItems(CountryComboBox);
            }

        } catch (SQLException ex) {
            Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the country index for the division combo box
     * 
     * @return the index
     */
    public int getCountryIndex() {
        countryIndex = updateCountryBox.getSelectionModel().getSelectedIndex();
        System.out.println("This is the country index: " + (countryIndex + 1));
        return (countryIndex + 1);
    }

    /**
     * Get division box with current division
     * @param index the index of the selected division
     */
    public void setDivisionCombobox(int index) {
        try {
            int selectedIndex = index;
            DBAddCustomers.getDivisionComboBox(selectedIndex);
            ObservableList<String> DivisionComboBox = DBAddCustomers.getDivisionComboBox(selectedIndex);

            for (String Division : DivisionComboBox) {
                updateDivisionBox.setItems(DivisionComboBox);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get the selected division ID from the user selection in the combo box
     * @return the selected division id
     * 
     * @throws Exception ignore
     */
    public int getSelectedDivisionID() throws Exception {
        selectedDivision = updateDivisionBox.getSelectionModel().getSelectedItem();
        selectedDivisionID = DBAddCustomers.getDivisionID(selectedDivision);
        System.out.println("This is the selected division id: " + selectedDivisionID);
        return selectedDivisionID;
    }

    /**
     * Used to update and save the customer information in the database 
     * 
     * @param actionEvent initiated when the save button used
     * @throws SQLException throws stack trace
     * @throws IOException ignore
     * @throws Exception ignore
     */
    public void saveButton(ActionEvent actionEvent) throws IOException, Exception {
        String customerName = updateNameBox.getText();
        String customerAddress = updateAddressBox.getText();
        updateCountryBox.getSelectionModel();
        updateDivisionBox.getSelectionModel();
        String customerPostalCode = updatePostalCodeBox.getText();
        String customerPhone = updatePhoneBox.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to save this customer?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            if (updateCountryBox.getSelectionModel().isEmpty()) {
                Alert countryAlert = new Alert(Alert.AlertType.ERROR, "Country has not been selected.");
                countryAlert.showAndWait();
            } else if (updateDivisionBox.getSelectionModel().isEmpty()) {
                Alert countryAlert = new Alert(Alert.AlertType.ERROR, "Division has not been selected.");
                countryAlert.showAndWait();
            } else if (customerName.isEmpty() || customerAddress.isEmpty() || customerPostalCode.isEmpty() || customerPhone.isEmpty()) {
                Alert emptyAlert = new Alert(Alert.AlertType.ERROR, "Not all fields have been filled.\nPlease check entries.");
                emptyAlert.showAndWait();
            } else {
                //Insert into database//
                try {
                    PreparedStatement ps = JDBC.getConnection().prepareStatement("UPDATE customers \n"
                            + "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, "
                            + "Last_Update = CURRENT_TIMESTAMP(), Last_Updated_By = ?, Division_ID = ? \n"
                            + "WHERE Customer_ID = " + customerID + "");

                    ps.setString(1, updateNameBox.getText());
                    ps.setString(2, updateAddressBox.getText());
                    ps.setString(3, updatePostalCodeBox.getText());
                    ps.setString(4, updatePhoneBox.getText());
                    ps.setString(5, Users.getUsername());
                    ps.setInt(6, getSelectedDivisionID());

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

        //Lambda expression to set the division box values
        updateCountryBox.setOnAction((ActionEvent e) -> {
            int index = getCountryIndex();
            setDivisionCombobox(index);
        });
    }
}
