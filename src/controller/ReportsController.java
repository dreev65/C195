package controller;

import helper.DBAppointments;
import helper.DBReports;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.DBClientApp;

/**
 * FXML Controller class for the reports that the customer can review
 *
 * @author Daniel Reeve
 */
public class ReportsController implements Initializable {

    //Month/Type Report
    @FXML
    private TableView monthTypeTableView;
    @FXML
    private TableColumn monthTypeAppointmentIDCol;
    @FXML
    private TableColumn monthTypeTypeCol;
    @FXML
    private TableColumn monthTypeMonthCol;
    @FXML
    private Label totalAppointmentsLabel;

    //Contact Schedule Report
    @FXML
    private TableView contactTableView;
    @FXML
    private TableColumn contactAppointmentIDCol;
    @FXML
    private TableColumn contactTitleCol;
    @FXML
    private TableColumn contactTypeCol;
    @FXML
    private TableColumn contactDescriptionCol;
    @FXML
    private TableColumn contactStartCol;
    @FXML
    private TableColumn contactEndCol;
    @FXML
    private TableColumn contactCustomerIDCol;
    @FXML
    private TableColumn contactContactCol;

    //Deleted Appointments Report
    @FXML
    private TableView deletedAppointmentsTableView;
    @FXML
    private TableColumn deletedUserIDCol;
    @FXML
    private TableColumn deletedCustomerNameCol;
    @FXML
    private TableColumn deletedTypeCol;
    @FXML
    private TableColumn deletedAppointmentsIDCol;  

    /**
     * Used to switch the user back to the application directory scene
     * 
     * @param actionEvent switches scenes
     * @throws IOException ignore
     */
    public void backToDirectory(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/ApplicationDirectory.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
     * Initializes the controller class and loads each report tab with their respective data
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Populate MonthType table with values
        try {

            PropertyValueFactory<Appointments, String> monthIDFactory = new PropertyValueFactory<>("appointmentID");
            PropertyValueFactory<Appointments, String> monthTypeFactory = new PropertyValueFactory<>("type");
            PropertyValueFactory<Appointments, String> monthStartFactory = new PropertyValueFactory<>("start");

            monthTypeTableView.setItems(DBReports.getMonthTypeData());
            monthTypeAppointmentIDCol.setCellValueFactory(monthIDFactory);
            monthTypeTypeCol.setCellValueFactory(monthTypeFactory);
            monthTypeMonthCol.setCellValueFactory(monthStartFactory);

            int size = monthTypeTableView.getItems().size();

            totalAppointmentsLabel.setText(String.valueOf(size));
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Populate contact schedule table with values
        try {

            PropertyValueFactory<Appointments, String> IDFactory = new PropertyValueFactory<>("appointmentID");
            PropertyValueFactory<Appointments, String> titleFactory = new PropertyValueFactory<>("title");
            PropertyValueFactory<Appointments, String> descriptionFactory = new PropertyValueFactory<>("description");
            PropertyValueFactory<Appointments, String> contactFactory = new PropertyValueFactory<>("contact");
            PropertyValueFactory<Appointments, String> typeFactory = new PropertyValueFactory<>("type");
            PropertyValueFactory<Appointments, String> startFactory = new PropertyValueFactory<>("start");
            PropertyValueFactory<Appointments, String> endFactory = new PropertyValueFactory<>("end");
            PropertyValueFactory<Appointments, String> customerIDFactory = new PropertyValueFactory<>("customerID");

            contactTableView.setItems(DBAppointments.getAppointmentsTable());
            contactAppointmentIDCol.setCellValueFactory(IDFactory);
            contactTitleCol.setCellValueFactory(titleFactory);
            contactDescriptionCol.setCellValueFactory(descriptionFactory);
            contactContactCol.setCellValueFactory(contactFactory);
            contactTypeCol.setCellValueFactory(typeFactory);
            contactStartCol.setCellValueFactory(startFactory);
            contactEndCol.setCellValueFactory(endFactory);
            contactCustomerIDCol.setCellValueFactory(customerIDFactory);

        } catch (SQLException ex) {
            Logger.getLogger(DBClientApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Populate deleted appointments table with values
        try {

            PropertyValueFactory<Appointments, Integer> deletedAppointmentIDFactory = new PropertyValueFactory<>("appointmentID");
            PropertyValueFactory<Appointments, Integer> deletedUserIDFactory = new PropertyValueFactory<>("userID");
            PropertyValueFactory<Appointments, Integer> deletedCustomerIDFactory = new PropertyValueFactory<>("customerID");
            PropertyValueFactory<Appointments, String> deletedTypeFactory = new PropertyValueFactory<>("type");

            deletedAppointmentsTableView.setItems(Appointments.getDeletedAppointments());
            deletedAppointmentsIDCol.setCellValueFactory(deletedAppointmentIDFactory);
            deletedUserIDCol.setCellValueFactory(deletedUserIDFactory);
            deletedCustomerNameCol.setCellValueFactory(deletedCustomerIDFactory);
            deletedTypeCol.setCellValueFactory(deletedTypeFactory);

        } catch (Exception e) {
            Logger.getLogger(DBClientApp.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
