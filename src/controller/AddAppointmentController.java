package controller;

import helper.DBAddAppointment;
import helper.JDBC;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import model.Users;

/**
 * FXML controller for adding appointments 
 *
 * @author Daniel Reeve
 */
public class AddAppointmentController implements Initializable {

    @FXML
    private TextField addAppointmentTitleBox;
    @FXML
    private TextField addAppointmentDescriptionBox;
    @FXML
    private TextField addAppointmentLocationBox;
    @FXML
    private ComboBox<String> addAppointmentContactBox;
    @FXML
    private ComboBox<String> addAppointmentTypeBox;
    @FXML
    private DatePicker addAppointmentStartDateBox;
    @FXML
    private ComboBox<LocalTime> addAppointmentStartTimeBox;
    @FXML
    private DatePicker addAppointmentEndDateBox;
    @FXML
    private ComboBox<LocalTime> addAppointmentEndTimeBox;
    @FXML
    private TextField addAppointmentCustomerIDBox;
    @FXML
    private TextField addAppointmentUserIDBox;

    /**
     * Set the appointment contacts combo box
     */
    public void setContactsComboBox() {
        DBAddAppointment.getAppointmentContacts();
        ObservableList<String> ContactsBox = DBAddAppointment.getAppointmentContacts();
        for (String Contact : ContactsBox) {
            addAppointmentContactBox.setItems(ContactsBox);
        }
    }
    
    /**
     * Set the appointment types combo box
     */
    public void setTypesBox(){
        ObservableList<String> types = FXCollections.observableArrayList();
        types.add("Online");
        types.add("In Person");
        types.add("On Phone");
        addAppointmentTypeBox.setItems(types);
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

        addAppointmentStartTimeBox.setItems(startTimes);
        addAppointmentEndTimeBox.setItems(endTimes);
    }
    
    /**
     * Checks if the selected appointment time is within EST business hours
     * 
     * @param localStart start time of the appointment
     * @param localEnd end time of the appointment
     * @return if the appointment is valid or not
     */
    public static boolean checkAppointmentTime(LocalDateTime localStart, LocalDateTime localEnd) {
        boolean validAppointment = false;
        
        //convert to eastern time zone
        ZoneId zoneID = ZoneId.systemDefault();
        ZonedDateTime lStart = localStart.atZone(zoneID);
        ZonedDateTime lEnd = localEnd.atZone(zoneID);
        ZonedDateTime easternStart = lStart.withZoneSameInstant(ZoneId.of("US/Eastern"));
        ZonedDateTime easternEnd = lEnd.withZoneSameInstant(ZoneId.of("US/Eastern"));
        
        //get the time from the eastern time conversion
        LocalTime start = easternStart.toLocalTime();
        LocalTime end = easternEnd.toLocalTime();
        
        //check if the start and end time are within eastern time business hours
        if (start.isAfter(LocalTime.of(7, 59, 59)) && start.isBefore(LocalTime.of(21, 0, 1))
                && end.isAfter(LocalTime.of(8, 59, 59)) && end.isBefore(LocalTime.of(22, 0, 1))) {
            
            validAppointment = true;
        }
        
        return validAppointment;
    }

    /**
     * Checks if there are overlapping appointments
     * 
     * @param id the customer id
     * @param localStart the appointment start
     * @param localEnd the appointment end
     * @return if there is an overlapping appointment
     * @throws Exception ignore
     */
    public boolean checkOverlappingAppointments(int id, LocalDateTime localStart, LocalDateTime localEnd) throws Exception{
        boolean overlappingAppointment = false;
      
        List<LocalDateTime[]> list = DBAddAppointment.getCustomerAppointments(id);

        LocalDateTime start = localStart;
        LocalDateTime end = localEnd;

        for (LocalDateTime[] arr : list) {
            LocalDateTime appStart = arr[0];
            LocalDateTime appEnd = arr[1];

            if (start.isEqual(appStart) || end.isEqual(appEnd) || start.isAfter(appStart) && start.isBefore(appEnd)){
                overlappingAppointment = true;
                System.out.println("Overlapping appointments");
                break;
            }
        }
        return overlappingAppointment;
    }
    
    /**
     * Disable past days and weekends in the DatePickers
     */
    public void disablePastDates(){
        addAppointmentStartDateBox.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || item.compareTo(today) < 0);
                
                if (item.getDayOfWeek() == DayOfWeek.SATURDAY || item.getDayOfWeek() == DayOfWeek.SUNDAY){
                    setDisable(true);
                }
            }
        });
        addAppointmentEndDateBox.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || item.compareTo(today) < 0);
                
                if (item.getDayOfWeek() == DayOfWeek.SATURDAY || item.getDayOfWeek() == DayOfWeek.SUNDAY){
                    setDisable(true);
                }
            }
        });
    }

    /**
     * Used to save the appointment information in the database 
     * 
     * @param actionEvent initiated when the save button used
     * @throws IOException ignore
     * @throws Exception ignore
     */
    public void saveButton(ActionEvent actionEvent) throws IOException, Exception {
        try {    
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to save this appointment?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                if (addAppointmentTitleBox.getText().isEmpty() || addAppointmentDescriptionBox.getText().isEmpty() || addAppointmentLocationBox.getText().isEmpty()
                        || addAppointmentTypeBox.getSelectionModel().isEmpty() || addAppointmentStartDateBox.getValue() == null || addAppointmentStartTimeBox.getSelectionModel().isEmpty() 
                        || addAppointmentEndDateBox.getValue() == null || addAppointmentEndTimeBox.getSelectionModel().isEmpty() || addAppointmentCustomerIDBox.getText().isEmpty()
                        || addAppointmentUserIDBox.getText().isEmpty()) {

                    Alert emptyAlert = new Alert(Alert.AlertType.ERROR, "Not all fields have been filled.\nPlease check entries.");
                    emptyAlert.showAndWait();
                    return;
                }
                
                int customerID = Integer.valueOf(addAppointmentCustomerIDBox.getText());
                int userID = Integer.valueOf(addAppointmentUserIDBox.getText());

                //get start and end time in local time (user input)
                LocalDate localStartDate = addAppointmentStartDateBox.getValue(); //returns date value without time
                LocalDate localEndDate = addAppointmentEndDateBox.getValue(); //returns date value without time
                LocalTime localStartTime = addAppointmentStartTimeBox.getSelectionModel().getSelectedItem();
                LocalTime localEndTime = addAppointmentEndTimeBox.getSelectionModel().getSelectedItem();

                //combine date and start/end times together       
                LocalDateTime localStart = LocalDateTime.of(localStartDate, localStartTime);
                LocalDateTime localEnd = LocalDateTime.of(localEndDate, localEndTime);

                //calls the appointement timing checks
                boolean validAppointment = checkAppointmentTime(localStart, localEnd);
                boolean overlappingApp = checkOverlappingAppointments(customerID, localStart, localEnd);
                
                if (validAppointment == false) {
                    Alert validAppointmentAlert = new Alert(Alert.AlertType.ERROR, """
                                                                                   Appointment time is invalid.
                                                                                   Please check the time entered.
                                                                                   EST business hours are 8:00am to 10:00pm.""");
                    validAppointmentAlert.showAndWait();
                } else if (overlappingApp == true) {
                    Alert overlappingAppointmentAlert = new Alert(Alert.AlertType.ERROR, "Customer has other appointments at this time.\nPlease check the date and time entered.\n");
                    overlappingAppointmentAlert.showAndWait();
                } else {
                    //Insert into database//
                    try {
                        PreparedStatement ps = JDBC.getConnection().prepareStatement("INSERT INTO appointments(Title, Description, Location, Type, Start, End, "
                                + "Create_Date, Created_by, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) "
                                + "VALUES(?, ?, ?, ?, ?, ?, " + "CURRENT_TIMESTAMP()" + ", ?, " + "CURRENT_TIMESTAMP()" + ", ?, ?, ?, ?)");

                        //input the values for the sql statement
                        ps.setString(1, addAppointmentTitleBox.getText());
                        ps.setString(2, addAppointmentDescriptionBox.getText());
                        ps.setString(3, addAppointmentLocationBox.getText());
                        ps.setString(4, addAppointmentTypeBox.getValue());
                        ps.setTimestamp(5, Timestamp.valueOf(localStart));
                        ps.setTimestamp(6, Timestamp.valueOf(localEnd));
                        ps.setString(7, Users.getUsername());
                        ps.setString(8, Users.getUsername());
                        ps.setInt(9, customerID);
                        ps.setInt(10, userID);
                        ps.setInt(11, DBAddAppointment.getContactID(addAppointmentContactBox.getSelectionModel().getSelectedIndex() + 1));

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
