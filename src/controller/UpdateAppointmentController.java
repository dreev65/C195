package controller;

import helper.DBAddAppointment;
import helper.JDBC;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;
import model.Users;

/**
 * FXML controller for updating appointments 
 *
 * @author Daniel Reeve
 */
public class UpdateAppointmentController implements Initializable {

    @FXML
    private TextField updateAppointmentTitle;
    @FXML
    private TextField updateAppointmentDescription;
    @FXML
    private TextField updateAppointmentLocation;
    @FXML
    private ComboBox<String> updateAppointmentContact;
    @FXML
    private ComboBox<String> updateAppointmentType;
    @FXML
    private DatePicker updateAppointmentStartDate;
    @FXML
    private ComboBox<LocalTime> updateAppointmentStartTime;
    @FXML
    private DatePicker updateAppointmentEndDate;
    @FXML
    private ComboBox<LocalTime> updateAppointmentEndTime;
    @FXML
    private TextField updateAppointmentCustomerID;
    @FXML
    private TextField updateAppointmentUserID;

    private int appointmentID;
    public Appointments selectedAppointment;

    /**
     * Sets the display fields with the appointment information from the selected appointment
     * 
     * @param selectedAppointment the selected appointment
     * @throws Exception ignore
     */
    public void transferAppointment(Appointments selectedAppointment) throws Exception {
        this.selectedAppointment = selectedAppointment;
        appointmentID = selectedAppointment.getAppointmentID();
        updateAppointmentTitle.setText(selectedAppointment.getTitle());
        updateAppointmentDescription.setText(selectedAppointment.getDescription());
        updateAppointmentLocation.setText(selectedAppointment.getLocation());
        updateAppointmentType.setValue(selectedAppointment.getType());
        updateAppointmentCustomerID.setText(String.valueOf(selectedAppointment.getCustomerID()));
        updateAppointmentUserID.setText(String.valueOf(selectedAppointment.getUserID()));
        updateAppointmentContact.setValue(selectedAppointment.getContact());

        //convert LocalDateTime to strings
        String startDateTime = String.valueOf(selectedAppointment.getStart());
        String[] start = startDateTime.split("T");

        //split into time and date strings
        String endDateTime = String.valueOf(selectedAppointment.getEnd());
        String[] end = endDateTime.split("T");

        //convert to LocalDate and LocalTime
        LocalDate startDate = LocalDate.parse(start[0]);
        LocalTime startTime = LocalTime.parse(start[1]);
        LocalDate endDate = LocalDate.parse(end[0]);
        LocalTime endTime = LocalTime.parse(end[1]);

        updateAppointmentStartDate.setValue(startDate);
        updateAppointmentStartTime.setValue(startTime);
        updateAppointmentEndDate.setValue(endDate);
        updateAppointmentEndTime.setValue(endTime);
    }

    /**
     * Limits start and end times to hours between 8am-5pm Appointment intervals
     * are set at 60 min increments
     */
    public void fillStartTimesList() {
        ObservableList<LocalTime> startTimes = FXCollections.observableArrayList();
        ObservableList<LocalTime> endTimes = FXCollections.observableArrayList();

        LocalTime time = LocalTime.of(0, 0);
        do {
            startTimes.add(time);
            endTimes.add(time);
            time = time.plusMinutes(60);
        } while (!time.equals(LocalTime.of(0, 0)));
        endTimes.remove(0);
        endTimes.add(LocalTime.of(0, 0));

        updateAppointmentStartTime.setItems(startTimes);
        updateAppointmentEndTime.setItems(endTimes);
    }

    /**
     * Set the appointment contacts combo box
     */
    public void setContactsComboBox() {
        DBAddAppointment.getAppointmentContacts();
        ObservableList<String> ContactsBox = DBAddAppointment.getAppointmentContacts();
        for (String Contact : ContactsBox) {
            updateAppointmentContact.setItems(ContactsBox);
        }
    }
    
    /**
     * Set the appointment types combo box
     */
    public void setTypesBox(){
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("Online");
        types.add("In Person");
        types.add("Phone");
        updateAppointmentType.setItems(types);
    }
    
    public boolean checkOverlappingAppointments(int custID, LocalDateTime localStart, LocalDateTime localEnd) throws Exception {
        boolean overlappingAppointment = false;
        int currentAppointmentID = selectedAppointment.getAppointmentID();
        System.out.println("Current Appointment ID: " + currentAppointmentID);
        
        List<String[]> list = DBAddAppointment.getCustomerAppointmentsForUpdate(custID);

        LocalDateTime start = localStart;
        LocalDateTime end = localEnd;

        for (String[] arr : list) {
            
            int appID = Integer.valueOf(arr[0]);
            LocalDateTime appStart = LocalDateTime.parse(arr[1]);
            LocalDateTime appEnd = LocalDateTime.parse(arr[2]);;

            System.out.println("appID: " + appID);
            System.out.println("appStart: " + appStart);
            System.out.println("appEnd: " + appEnd);

            //TODO
            if (start.isEqual(appStart) && end.isEqual(appEnd) && appID == currentAppointmentID){
                break;
            } else if (start.isAfter(appStart.minusSeconds(1)) && start.isBefore(appEnd)){
                overlappingAppointment = true;
                System.out.println("Overlapping appointments (Update Button Pressed)");
                break; 
            }
        }
        return overlappingAppointment;
    }

    /**
     * Disable past days in the DatePickers
     */
    public void disablePastDates(){
        updateAppointmentStartDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || item.compareTo(today) < 0 );
            }
        });
        updateAppointmentEndDate.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || item.compareTo(today) < 0 );
            }
        });
    }

    /**
     * Used to update and save the appointment information in the database 
     * 
     * @param actionEvent initiated when the save button used
     * @throws IOException ignore
     * @throws Exception ignore
     */
    public void saveButton(ActionEvent actionEvent) throws IOException, Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to save this appointment?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            if (updateAppointmentTitle.getText().isEmpty() || updateAppointmentDescription.getText().isEmpty() || updateAppointmentLocation.getText().isEmpty()
                    || updateAppointmentType.getValue().isEmpty() || updateAppointmentStartDate.getValue() == null || updateAppointmentStartTime.getSelectionModel().isEmpty()
                    || updateAppointmentEndDate.getValue() == null || updateAppointmentEndTime.getSelectionModel().isEmpty() || updateAppointmentCustomerID.getText().isEmpty()
                    || updateAppointmentContact.getSelectionModel().isEmpty() || updateAppointmentUserID.getText().isEmpty()) {

                Alert emptyAlert = new Alert(Alert.AlertType.ERROR, "Not all fields have been filled.\nPlease check entries.");
                emptyAlert.showAndWait();
            }
            
            String title = updateAppointmentTitle.getText();
            String description = updateAppointmentDescription.getText();
            String location = updateAppointmentLocation.getText();
            String type = updateAppointmentType.getValue();
            String custID = updateAppointmentCustomerID.getText();
            String userID = updateAppointmentUserID.getText();

            //get start and end time in local time (user input)
            LocalDate localStartDate = updateAppointmentStartDate.getValue(); //returns date value without time
            LocalDate localEndDate = updateAppointmentEndDate.getValue(); //returns date value without time
            LocalTime localStartTime = updateAppointmentStartTime.getSelectionModel().getSelectedItem();
            LocalTime localEndTime = updateAppointmentEndTime.getSelectionModel().getSelectedItem();

            //combine date and start/end times together       
            LocalDateTime localStart = LocalDateTime.of(localStartDate, localStartTime);
            LocalDateTime localEnd = LocalDateTime.of(localEndDate, localEndTime);

            boolean validAppointment = AddAppointmentController.checkAppointmentTime(localStart, localEnd);
            boolean overlappingApp = checkOverlappingAppointments(Integer.valueOf(custID), localStart, localEnd);
 
            if (validAppointment == false) {
                Alert validAppointmentAlert = new Alert(Alert.AlertType.ERROR, "Appointment time is invalid.\nPlease check the time entered.\n"
                            + "EST business hours are 8:00am to 10:00pm.");
                    validAppointmentAlert.showAndWait();
            } else if (overlappingApp == true) {
                    Alert overlappingAppointmentAlert = new Alert(Alert.AlertType.ERROR, "Customer has other appointments at this time.\nPlease check the date and time entered.\n");
                    overlappingAppointmentAlert.showAndWait();
            } else {
                //Insert into database//
                try {
                    PreparedStatement ps = JDBC.getConnection().prepareStatement("UPDATE appointments \n"
                            + "SET Title = ?, Description = ?, Location = ?, Type = ?, "
                            + "Start = ?, End = ?, Last_Update = CURRENT_TIMESTAMP(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ?\n"
                            + "WHERE Appointment_ID = " + appointmentID + "");

                    ps.setString(1, title);
                    ps.setString(2, description);
                    ps.setString(3, location);
                    ps.setString(4, type);
                    ps.setTimestamp(5, Timestamp.valueOf(localStart));
                    ps.setTimestamp(6, Timestamp.valueOf(localEnd));
                    ps.setString(7, Users.getUsername());
                    ps.setString(8, custID);
                    ps.setInt(9, Integer.valueOf(userID));
                    ps.setInt(10, DBAddAppointment.getContactID(updateAppointmentContact.getSelectionModel().getSelectedIndex() + 1));

                    ps.executeUpdate();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                Parent root = FXMLLoader.load(getClass().getResource("/view/Appointments.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } else {
            Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION, "Save appointment canceled.");
            cancelAlert.showAndWait();
        }
    }

    /**
     * Cancels the adding an appointment
     * 
     * @param actionEvent cancels the adding an appointment when button is used
     * @throws IOException ignore
     */
    public void cancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Appointments.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Initializes the controller class
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setContactsComboBox();
        setTypesBox();
        fillStartTimesList();
        disablePastDates();
    }
}
