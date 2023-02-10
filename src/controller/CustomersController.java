package controller;

import helper.DBAppointments;
import model.DBClientApp;
import helper.DBCustomers;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;
import model.Users;

/**
 * FXML Controller for the customers table view scene
 *
 * @author Daniel Reeve
 */
public class CustomersController implements Initializable {

    @FXML
    private TableView<Customers> customersTableView;
    @FXML
    private TableColumn<Customers, String> customerIDCol;
    @FXML
    private TableColumn<Customers, String> customerNameCol;
    @FXML
    private TableColumn<Customers, String> customerAddressCol;
    @FXML
    private TableColumn<Customers, String> customerDivisionCol;
    @FXML
    private TableColumn<Customers, String> customerPostalCodeCol;
    @FXML
    private TableColumn<Customers, String> customerPhoneCol;

    /**
     * Used to switch the user to the add customer scene
     * 
     * @param actionEvent switches scenes
     * @throws java.io.IOException ignore
     */
    public void switchToAddCustomer(ActionEvent actionEvent) throws IOException {
        customersTableView.getItems().clear();

        Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Used to switch the user to the update customer scene
     * 
     * @param actionEvent switches scenes
     * @throws IOException ignore
     * @throws Exception ignore
     */
    public void switchToUpdateCustomer(ActionEvent actionEvent) throws IOException, Exception {

        try {
            Customers selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "A customer was not selected to update.\nPlease select a customer before clicking the update button.");
                alert.showAndWait();
            } else {
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateCustomer.fxml"));
                Parent scene = loader.load();
                UpdateCustomerController controller = loader.getController();
                controller.transferCustomer(selectedCustomer);
                stage.setScene(new Scene(scene));
                stage.show();

                customersTableView.getItems().clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }  

    /**
     * Used to switch the user back to the application directory scene
     * 
     * @param actionEvent switches scenes
     * @throws IOException ignore
     */
    public void backToDirectory(ActionEvent actionEvent) throws IOException {
        customersTableView.getItems().clear();

        Parent root = FXMLLoader.load(getClass().getResource("/view/ApplicationDirectory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Used to delete the selected customer from the both the table view and database. Checks if there are any appointments for the customer before deleting the customer.
     * If there are any appointments, they are automatically deleted.
     * 
     * @param actionEvent deletes the customer and any associated appointments
     * @throws SQLException ignore
     * @throws Exception ignore
     */
    public void deleteCustomer(ActionEvent actionEvent) throws Exception {
        if (customersTableView.getSelectionModel().getSelectedItem() != null) {

            Customers selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
            String custName = selectedCustomer.getCustomerName();
            int custID = selectedCustomer.getCustomerID();
            ObservableList<Appointments> appointments = DBCustomers.checkForAppointments(custID);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete this customer?\nCustomer Name: " + custName + "\nCustomer ID: " + custID);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (appointments.isEmpty()){
                    DBCustomers.deleteDBCustomer(custID);
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Customer deleted.");
                    alert2.showAndWait();

                    customersTableView.getItems().remove(customersTableView.getSelectionModel().getSelectedItem());
                    
                } else {
                    Alert appointmentAlert = new Alert(Alert.AlertType.CONFIRMATION, "Customer has appointments assigned to them.\nWould you like to delete all appointments first?");
                    Optional<ButtonType> delete = appointmentAlert.showAndWait();
                    if (delete.get() == ButtonType.OK) {
                        for (int i = 0; i <= appointments.size()-1; i++){
                            Appointments appointment = appointments.get(i);
                            int apptID = appointment.getAppointmentID();
                            int userID = Users.getUserID();
                            String type = appointment.getType();   

                            Appointments temp = new Appointments(apptID, userID, custID, type);
                            Appointments.addDeletedAppointments(temp);

                            DBAppointments.deleteDBAppointment(apptID);
                            System.out.println("Appointment Deleted"); 
                            }       

                        DBCustomers.deleteDBCustomer(custID);
                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Customer deleted.");
                        alert2.showAndWait();

                        customersTableView.getItems().remove(customersTableView.getSelectionModel().getSelectedItem());
                    }
                    else {
                        Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION, "Customer deletion was canceled.");
                        cancelAlert.showAndWait();
                    }
                }
            } else {
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION, "Customer deletion was canceled.");
                cancelAlert.showAndWait();
            }
        } else {
            Alert nonselectedAlert = new Alert(Alert.AlertType.INFORMATION, "No customer was selected. Please select a customer to delete them.");
            nonselectedAlert.showAndWait();
        }
    }

    /**
     * Used to exit the application
     * 
     * @param actionEvent closes application
     */
    public void exitButton(ActionEvent actionEvent) {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
    }

    /**
     * Initializes the controller class and loads the table view with all customers
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Populate CustomersTable with values
        try {

            PropertyValueFactory<Customers, String> customerIDFactory = new PropertyValueFactory<>("customerID");
            PropertyValueFactory<Customers, String> customerNameFactory = new PropertyValueFactory<>("customerName");
            PropertyValueFactory<Customers, String> customerAddressFactory = new PropertyValueFactory<>("address");
            PropertyValueFactory<Customers, String> customerDivisionFactory = new PropertyValueFactory<>("division");
            PropertyValueFactory<Customers, String> customerPostalCodeFactory = new PropertyValueFactory<>("postalCode");
            PropertyValueFactory<Customers, String> customerPhoneFactory = new PropertyValueFactory<>("phone");

            customersTableView.setItems(DBCustomers.getCustomersTable());
            customerIDCol.setCellValueFactory(customerIDFactory);
            customerNameCol.setCellValueFactory(customerNameFactory);
            customerAddressCol.setCellValueFactory(customerAddressFactory);
            customerDivisionCol.setCellValueFactory(customerDivisionFactory);
            customerPostalCodeCol.setCellValueFactory(customerPostalCodeFactory);
            customerPhoneCol.setCellValueFactory(customerPhoneFactory);

        } catch (SQLException ex) {
            Logger.getLogger(DBClientApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
