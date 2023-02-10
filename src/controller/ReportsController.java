package controller;

import helper.DBReports;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.DBClientApp;
import model.Reports;

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
    private TableColumn monthTypeMonthCol;
    @FXML
    private TableColumn monthTypeOnlineCol;
    @FXML
    private TableColumn monthTypeInPersonCol;
    @FXML
    private TableColumn monthTypePhoneCol;
    

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
     * Initializes an array to store the MonthType report data
     */
    private int monthTypes[][] = new int[][] {
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0},
        {0,0,0,0}
    };
    
    /**
     * Gets a list of all appointments then breaks them down for display in the MonthType report
     * 
     * @throws SQLException ignore
     */
    public void monthTypeReport() throws SQLException{
        ObservableList<Reports> typesByMonthOL = FXCollections.observableArrayList();
        ObservableList<Reports> appointments = DBReports.getMonthTypeData();
        
        
        for(int i = 0; i < appointments.size(); i++) {
            
            Reports appointment = appointments.get(i);
            
            //Get the appointment month
            LocalDateTime startDateTime = appointment.getStart();
            String month = startDateTime.getMonth().toString();
            
            //Get the appointment type
            String type = appointment.getType();
            
            //Convert month to int
            int m = -1;
            switch (month) {
                case "JANUARY" -> m = 0;
                case "FEBRUARY" -> m = 1;
                case "MARCH" -> m = 2;
                case "APRIL" -> m = 3;
                case "MAY" -> m = 4;
                case "JUNE" -> m = 5;
                case "JULY" -> m = 6;
                case "AUGUST" -> m = 7;
                case "SEPTEMBER" -> m = 8;
                case "OCTOBER" -> m = 9;
                case "NOVEMBER" -> m = 10;
                case "DECEMBER" -> m = 11;
            }
 
            //Update the count of the month associated with the appointment
            if(m == 0) {
                if(type.equals("Online")) {
                    monthTypes[0][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[0][1]++;
                } else {
                    monthTypes[0][2]++;
                }
            } else if(m == 1) {
                if(type.equals("Online")) {
                    monthTypes[1][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[1][1]++;
                } else {
                    monthTypes[1][2]++;
                }
            } else if(m == 2) {
                if(type.equals("Online")) {
                    monthTypes[2][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[2][1]++;
                } else {
                    monthTypes[2][2]++;
                }
            } else if(m == 3) {
                if(type.equals("Online")) {
                    monthTypes[3][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[3][1]++;
                } else {
                    monthTypes[3][2]++;
                }
            } else if(m == 4) {
                if(type.equals("Online")) {
                    monthTypes[4][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[4][1]++;
                } else {
                    monthTypes[4][2]++;
                }
            } else if(m == 5) {
                if(type.equals("Online")) {
                    monthTypes[5][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[5][1]++;
                } else {
                    monthTypes[5][2]++;
                }
            } else if(m == 6) {
                if(type.equals("Online")) {
                    monthTypes[6][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[6][1]++;
                } else {
                    monthTypes[6][2]++;
                }
            } else if(m == 7) {
                if(type.equals("Online")) {
                    monthTypes[7][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[7][1]++;
                } else {
                    monthTypes[7][2]++;
                }
            } else if(m == 8) {
                if(type.equals("Online")) {
                    monthTypes[8][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[8][1]++;
                } else {
                    monthTypes[8][2]++;
                }
            } else if(m == 9) {
                if(type.equals("Online")) {
                    monthTypes[9][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[9][1]++;
                } else {
                    monthTypes[9][2]++;
                }
            } else if(m == 10) {
                if(type.equals("Online")) {
                    monthTypes[10][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[10][1]++;
                } else {
                    monthTypes[10][2]++;
                }
            } else if(m == 11) {
                if(type.equals("Online")) {
                    monthTypes[11][0]++;
                } else if(type.equals("In Person")) {
                    monthTypes[11][1]++;
                } else {
                    monthTypes[11][2]++;
                }
            }
        }
        for (int i = 0; i < 12; i++) {
            int onlineCount = monthTypes[i][0];
            int inPersonCount = monthTypes[i][1];
            int phoneCount = monthTypes[i][2];
            
            System.out.println("\nOnline Count: " + onlineCount);
            System.out.println("In Person Count: " + inPersonCount);
            System.out.println("Phone Count: " + phoneCount);
            
            typesByMonthOL.add(new Reports(getMonth(i), onlineCount, inPersonCount, phoneCount));
        }
        
        //Populate MonthType report
        PropertyValueFactory<Reports, String> monthFactory = new PropertyValueFactory<>("Month");
        PropertyValueFactory<Reports, Integer> onlineFactory = new PropertyValueFactory<>("Online");
        PropertyValueFactory<Reports, Integer> inPersonFactory = new PropertyValueFactory<>("InPerson");
        PropertyValueFactory<Reports, Integer> phoneFactory = new PropertyValueFactory<>("Phone");

        monthTypeTableView.setItems(typesByMonthOL);
        monthTypeMonthCol.setCellValueFactory(monthFactory);
        monthTypeOnlineCol.setCellValueFactory(onlineFactory);
        monthTypeInPersonCol.setCellValueFactory(inPersonFactory);
        monthTypePhoneCol.setCellValueFactory(phoneFactory);
    }  
    
    /**
     * Gets the full month of each appointment
     * @param month integer value of the month
     * @return full month name
     */
    private String getMonth(int month) {
        String monthString = null;
        int m = month;
        
        if (m == 0) {
            monthString = "JANUARY";
        }
        if (m == 1) {
            monthString = "FEBRUARY";
        }
        if (m == 2) {
            monthString = "MARCH";
        }
        if (m == 3) {
            monthString = "APRIL";
        }
        if (m == 4) {
            monthString = "MAY";
        }
        if (m == 5) {
            monthString = "JUNE";
        }
        if (m == 6) {
            monthString = "JULY";
        }
        if (m == 7) {
            monthString = "AUGUST";
        }
        if (m == 8) {
            monthString = "SEPTEMBER";
        }
        if (m == 9) {
            monthString = "OCTOBER";
        }
        if (m == 10) {
            monthString = "NOVEMBER";
        }
        if (m == 11) {
            monthString = "DECEMBER";
        }
        return monthString;
    }

    
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

        //Calls monthTypeReport to popluate the MonthType report
        try {
            monthTypeReport();
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

            contactTableView.setItems(DBReports.getContactsReport());
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
