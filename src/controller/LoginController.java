package controller;

import helper.DBLogin;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointments;
import model.DateTime;
import model.Users;

/**
 * FXML Controller class for the login page
 *
 * @author Daniel Reeve
 */
public class LoginController implements Initializable {

    @FXML
    private PasswordField passwordBox;
    @FXML
    private TextField usernameBox;
    @FXML
    private Label userLocationLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button exitButton;
    @FXML
    private Button loginButton;

    /**
     * List of appointments to be used when determining if there are any upcoming appointments
     */
    ObservableList<Appointments> appointmentReminderOL = DBLogin.appointmentReminder;

    /**
     * Sets user location label
     */
    public void setLocationLabel() {
        String userZone = ZoneId.systemDefault().toString();
        userLocationLabel.setText(userZone);
    }

    /**
     * Filters the reminder list and alerts if appointment is within 15 minutes and 
     * determines the user's location to show the correct language for the alerts.
     * <br><br>
     * Lambda is used to filter through the appointments to find any that are within within 15 minutes of the users login. 
     * A lambda was used because it is more efficient that using a for loop to iterate through each appointment because the appointments are in a collection. 
     */
    public void appointmentAlert() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = now.plusMinutes(15);
        System.out.println("Now: " + now);
        System.out.println("NowPlus15: " + nowPlus15Min);
        
        FilteredList<Appointments> filteredData = new FilteredList<>(appointmentReminderOL);
        
    //Lambda expression//
        filteredData.setPredicate((Appointments row) -> {
            LocalDateTime rowDate = row.getStart();
            return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min);
        });
        if (filteredData.isEmpty()) {
            if (Locale.getDefault().getLanguage().equals("en")) {
                Alert noApptAlert = new Alert(Alert.AlertType.INFORMATION, "No upcoming appointments.");
                noApptAlert.showAndWait();
            } else if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert noApptAlert = new Alert(Alert.AlertType.INFORMATION, "Aucun rendez-vous à venir.");
                noApptAlert.showAndWait();
            }    
        } else {
            int id = filteredData.get(0).getAppointmentID();
            LocalDateTime start = filteredData.get(0).getStart();
            if (Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Upcoming Appointment");
                alert.setHeaderText("An appointment is scheduled to start within 15 minutes.");
                alert.setContentText("Your upcoming appointment is currently set to begin at " + start + ".\nThe appointment ID is: " + id);
                alert.showAndWait();
            } else if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Rendez-vous à venir");
                alert.setHeaderText("Un rendez-vous est prévu dans 15 minutes.");
                alert.setContentText("Votre prochain rendez-vous est actuellement fixé pour commencer à " + start + ".\nL'identifiant du rendez-vous est: " + id);
                alert.showAndWait();
            }
        }
    }
    
    /**
     * Used to switch the user to the application directory scene
     * 
     * @param actionEvent switches scenes
     * @throws IOException ignore
     */
    public void switchToScheduler(ActionEvent actionEvent) throws Exception {

        String username = usernameBox.getText();
        String password = passwordBox.getText();
        int userID = DBLogin.getUserID(username);
        Users user = new Users();
        boolean validLogin;

        if (DBLogin.validPassword(userID, password)) {

            validLogin = true;

            user.setUserID(userID);
            user.setUsername(username);

            loginLog(user.getUsername(), validLogin);
            DBLogin.createReminderList();
            appointmentAlert();

            Parent root = FXMLLoader.load(getClass().getResource("/view/ApplicationDirectory.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            validLogin = false;
            user.setUsername(username);
            loginLog(user.getUsername(), validLogin);

            if (Locale.getDefault().getLanguage().equals("en")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username or Password are invalid!");
                alert.showAndWait();
            } else if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Le nom d'utilisateur ou le mot de passe sont invalides!");
                alert.showAndWait();
            }
        }
    }

    /**
     * Creates a new log file if one doesn't exist and inserts login information for current user
     * 
     * @param user the user
     * @param validLogin whether the login is valid or not
     */
    public void loginLog(String user, boolean validLogin) {
        boolean valid = validLogin;
        try {
            if (valid == true) {
                String fileName = "login_activity.txt";
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.append(DateTime.getTimeStamp() + " " + user + " Success" + " " + "\n");
                System.out.println("New login recorded in log file.");
                writer.flush();
                writer.close();
            } else {
                String fileName = "login_activity.txt";
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.append(DateTime.getTimeStamp() + " " + user + " Failed" + " " + "\n");
                System.out.println("New login recorded in log file.");
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            System.out.println(e);
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
     * Initializes the controller class and sets the language of each display item based on the users system language
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            rb = ResourceBundle.getBundle("properties/lang", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")) {
                titleLabel.setText(rb.getString("title"));
                usernameLabel.setText(rb.getString("usernameLabel"));
                passwordLabel.setText(rb.getString("passwordLabel"));
                locationLabel.setText(rb.getString("locationLabel"));
                usernameBox.setPromptText(rb.getString("usernameBox"));
                passwordBox.setPromptText(rb.getString("passwordBox"));
                exitButton.setText(rb.getString("exitButton"));
                loginButton.setText(rb.getString("loginButton"));
            } 
        } catch (MissingResourceException e) {
            System.out.println("Missing resource");
        }
        setLocationLabel();   
    }
}
