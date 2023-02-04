package controller;

import helper.DBAppointments;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
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
import model.Users;

/**
 * FXML Controller for the appointments table view scene
 *
 * @author Daniel Reeve
 */
public class AppointmentsController implements Initializable {

    @FXML
    private TableView<Appointments> appointmentTableView;
    @FXML
    private TableColumn<Appointments, String> appointmentIDCol;
    @FXML
    private TableColumn<Appointments, String> appointmentTitleCol;
    @FXML
    private TableColumn<Appointments, String> appointmentDescriptionCol;
    @FXML
    private TableColumn<Appointments, String> appointmentLocationCol;
    @FXML
    private TableColumn<Appointments, String> appointmentContactCol;
    @FXML
    private TableColumn<Appointments, String> appointmentTypeCol;
    @FXML
    private TableColumn<Appointments, String> appointmentStartDateCol;
    @FXML
    private TableColumn<Appointments, String> appointmentEndDateCol;
    @FXML
    private TableColumn<Appointments, String> appointmentCustomerIDCol;
    @FXML
    private TableColumn<Appointments, String> appointmentUserIDCol;

    /**
     * Used to switch the user to the add appointments scene
     * 
     * @param actionEvent switches scenes
     * @throws java.io.IOException ignore
     */
    public void switchToAddAppointment(ActionEvent actionEvent) throws IOException {
        appointmentTableView.getItems().clear();

        Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Used to switch the user to the update appointments scene
     * 
     * @param actionEvent switches scenes
     * @throws IOException ignore
     * @throws Exception ignore
     */
    public void switchToUpdateAppointment(ActionEvent actionEvent) throws IOException, Exception {
        try {
            Appointments selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
            if (selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "An appointment was not selected to update.\nPlease select an appointment before clicking the update button.");
                alert.showAndWait();
            } else {
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdateAppointment.fxml"));
                Parent scene = loader.load();
                UpdateAppointmentController controller = loader.getController();
                controller.transferAppointment(selectedAppointment);
                stage.setScene(new Scene(scene));
                stage.show();

                appointmentTableView.getItems().clear();
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
        appointmentTableView.getItems().clear();

        Parent root = FXMLLoader.load(getClass().getResource("/view/ApplicationDirectory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Creates the observable list of appointments for weekly sorting when the Weekly radio button is pressed 
     * 
     * @param actionEvent marks the weekly radio button as selected
     * @throws SQLException ignore
     */
    public void sortByWeekly(ActionEvent actionEvent) throws SQLException {
        ObservableList appointmentsOL = DBAppointments.getAppointmentsTable();
        weeklySort(appointmentsOL);
    }

    /**
     * Filters the appointments list for the next week and loads the table view
     * 
     * @param appointmentsOL the observable list of all appointment
     */
    public void weeklySort(ObservableList<Appointments> appointmentsOL) {
        
        ObservableList<Appointments> weeklyList = FXCollections.observableArrayList();

        //filter appointments for week
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Week = now.plusWeeks(1);

        for(Appointments row: appointmentsOL) {

            String datetime = String.valueOf(row.getStart());
            String[] start = datetime.split("T");
            String date = start[0];

            LocalDate rowDate = LocalDate.parse(date);

            if(rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Week)){
                weeklyList.add(row);
            }
        }
        
        PropertyValueFactory<Appointments, String> appointmentAppointmentIDFactory = new PropertyValueFactory<>("appointmentID");
        PropertyValueFactory<Appointments, String> appointmentTitleFactory = new PropertyValueFactory<>("title");
        PropertyValueFactory<Appointments, String> appointmentDescriptionFactory = new PropertyValueFactory<>("description");
        PropertyValueFactory<Appointments, String> appointmentLocationFactory = new PropertyValueFactory<>("location");
        PropertyValueFactory<Appointments, String> appointmentContactFactory = new PropertyValueFactory<>("contact");
        PropertyValueFactory<Appointments, String> appointmentTypeFactory = new PropertyValueFactory<>("type");
        PropertyValueFactory<Appointments, String> appointmentStartFactory = new PropertyValueFactory<>("start");
        PropertyValueFactory<Appointments, String> appointmentEndFactory = new PropertyValueFactory<>("end");
        PropertyValueFactory<Appointments, String> appointmentCustomerIDFactory = new PropertyValueFactory<>("customerID");
        PropertyValueFactory<Appointments, String> appointmentUserIDFactory = new PropertyValueFactory<>("userID");
        appointmentTableView.setItems(weeklyList);
        appointmentIDCol.setCellValueFactory(appointmentAppointmentIDFactory);
        appointmentTitleCol.setCellValueFactory(appointmentTitleFactory);
        appointmentDescriptionCol.setCellValueFactory(appointmentDescriptionFactory);
        appointmentLocationCol.setCellValueFactory(appointmentLocationFactory);
        appointmentContactCol.setCellValueFactory(appointmentContactFactory);
        appointmentTypeCol.setCellValueFactory(appointmentTypeFactory);
        appointmentStartDateCol.setCellValueFactory(appointmentStartFactory);
        appointmentEndDateCol.setCellValueFactory(appointmentEndFactory);
        appointmentCustomerIDCol.setCellValueFactory(appointmentCustomerIDFactory);
        appointmentUserIDCol.setCellValueFactory(appointmentUserIDFactory);
    }

    /**
     * Creates the observable list of appointments for monthly sorting when the Monthly radio button is pressed 
     * 
     * @param actionEvent marks the monthly radio button as selected
     * @throws SQLException ignore
     */
    public void sortByMonthly(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointments> appointmentsOL = DBAppointments.getAppointmentsTable();
        monthlySort(appointmentsOL);
    }

    /**
     * Filters the appointments list for the next month and loads the table view
     * 
     * @param appointmentsOL the observable list of all appointment
     * @throws java.sql.SQLException ignore
     */
    public void monthlySort(ObservableList<Appointments> appointmentsOL) throws SQLException {
        
        ObservableList<Appointments> monthlyList = FXCollections.observableArrayList();
        
        //filter appointments for month
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Month = now.plusMonths(1);

        for(Appointments row: appointmentsOL) {

            String datetime = String.valueOf(row.getStart());
            String[] start = datetime.split("T");
            String date = start[0];

            LocalDate rowDate = LocalDate.parse(date);

            if (rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Month)) {
                monthlyList.add(row);
            }
        }
         
        PropertyValueFactory<Appointments, String> appointmentAppointmentIDFactory = new PropertyValueFactory<>("appointmentID");
        PropertyValueFactory<Appointments, String> appointmentTitleFactory = new PropertyValueFactory<>("title");
        PropertyValueFactory<Appointments, String> appointmentDescriptionFactory = new PropertyValueFactory<>("description");
        PropertyValueFactory<Appointments, String> appointmentLocationFactory = new PropertyValueFactory<>("location");
        PropertyValueFactory<Appointments, String> appointmentContactFactory = new PropertyValueFactory<>("contact");
        PropertyValueFactory<Appointments, String> appointmentTypeFactory = new PropertyValueFactory<>("type");
        PropertyValueFactory<Appointments, String> appointmentStartFactory = new PropertyValueFactory<>("start");
        PropertyValueFactory<Appointments, String> appointmentEndFactory = new PropertyValueFactory<>("end");
        PropertyValueFactory<Appointments, String> appointmentCustomerIDFactory = new PropertyValueFactory<>("customerID");
        PropertyValueFactory<Appointments, String> appointmentUserIDFactory = new PropertyValueFactory<>("userID");
        appointmentTableView.setItems(monthlyList);
        appointmentIDCol.setCellValueFactory(appointmentAppointmentIDFactory);
        appointmentTitleCol.setCellValueFactory(appointmentTitleFactory);
        appointmentDescriptionCol.setCellValueFactory(appointmentDescriptionFactory);
        appointmentLocationCol.setCellValueFactory(appointmentLocationFactory);
        appointmentContactCol.setCellValueFactory(appointmentContactFactory);
        appointmentTypeCol.setCellValueFactory(appointmentTypeFactory);
        appointmentStartDateCol.setCellValueFactory(appointmentStartFactory);
        appointmentEndDateCol.setCellValueFactory(appointmentEndFactory);
        appointmentCustomerIDCol.setCellValueFactory(appointmentCustomerIDFactory);
        appointmentUserIDCol.setCellValueFactory(appointmentUserIDFactory);
    }

    /**
     * Used to delete the selected appointment from the both the table view and database
     * 
     * @param actionEvent deletes the appointment
     * @throws SQLException ignore
     * @throws Exception ignore
     */
    public void deleteAppointment(ActionEvent actionEvent) throws SQLException, Exception {
        if (appointmentTableView.getSelectionModel().getSelectedItem() != null) {

            Appointments selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
            int custID = selectedAppointment.getCustomerID();
            int apptID = selectedAppointment.getAppointmentID();
            String type = selectedAppointment.getType();
            int userID = Users.getUserID();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete this appointment?\n"
                    + "Appointment ID: " + apptID + "\n"
                    + "Appointment Type: " + type);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                DBAppointments.deleteDBAppointment(apptID);
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Appointment deleted.");
                alert2.showAndWait();

                Appointments temp = new Appointments(apptID, userID, custID, type);
                Appointments.addDeletedAppointments(temp);
                
                appointmentTableView.getItems().remove(appointmentTableView.getSelectionModel().getSelectedItem());

            } else {
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION, "Delete appointment canceled.");
                cancelAlert.showAndWait();
            }
        } else {
            Alert invalidAlert = new Alert(Alert.AlertType.INFORMATION, "No appointment was selected for deletion.");
                invalidAlert.showAndWait();
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
     * Initializes the controller class and loads the table view with all appointments
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Populate AppointmentsTable with all appointments
        try {

            PropertyValueFactory<Appointments, String> appointmentAppointmentIDFactory = new PropertyValueFactory<>("appointmentID");
            PropertyValueFactory<Appointments, String> appointmentTitleFactory = new PropertyValueFactory<>("title");
            PropertyValueFactory<Appointments, String> appointmentDescriptionFactory = new PropertyValueFactory<>("description");
            PropertyValueFactory<Appointments, String> appointmentLocationFactory = new PropertyValueFactory<>("location");
            PropertyValueFactory<Appointments, String> appointmentContactFactory = new PropertyValueFactory<>("contact");
            PropertyValueFactory<Appointments, String> appointmentTypeFactory = new PropertyValueFactory<>("type");
            PropertyValueFactory<Appointments, String> appointmentStartFactory = new PropertyValueFactory<>("start");
            PropertyValueFactory<Appointments, String> appointmentEndFactory = new PropertyValueFactory<>("end");
            PropertyValueFactory<Appointments, String> appointmentCustomerIDFactory = new PropertyValueFactory<>("customerID");
            PropertyValueFactory<Appointments, String> appointmentUserIDFactory = new PropertyValueFactory<>("userID");
            appointmentTableView.setItems(DBAppointments.getAppointmentsTable());
            appointmentIDCol.setCellValueFactory(appointmentAppointmentIDFactory);
            appointmentTitleCol.setCellValueFactory(appointmentTitleFactory);
            appointmentDescriptionCol.setCellValueFactory(appointmentDescriptionFactory);
            appointmentLocationCol.setCellValueFactory(appointmentLocationFactory);
            appointmentContactCol.setCellValueFactory(appointmentContactFactory);
            appointmentTypeCol.setCellValueFactory(appointmentTypeFactory);
            appointmentStartDateCol.setCellValueFactory(appointmentStartFactory);
            appointmentEndDateCol.setCellValueFactory(appointmentEndFactory);
            appointmentCustomerIDCol.setCellValueFactory(appointmentCustomerIDFactory);
            appointmentUserIDCol.setCellValueFactory(appointmentUserIDFactory);
            
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
