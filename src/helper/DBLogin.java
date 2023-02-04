package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Users;

/**
 * Class that holds the database queries used for the login page of the application
 * @author Daniel Reeve
 */
public class DBLogin {

    public static ObservableList<Appointments> appointmentReminder = FXCollections.observableArrayList();
    public static DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public static ZoneId localZoneId = ZoneId.systemDefault();

    /**
     * Get all users from database
     * 
     * @return all the users
     * @throws SQLException throws stack trace
     */
    public static ObservableList<Users> getAllUsers() throws SQLException {

        ObservableList<Users> users = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String username = rs.getString("User_Name");
                String password = rs.getString("Password");
                Users U = new Users(userID, username, password) {
                };
                users.add(U);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }   

    /**
     * Get userID in reference to userName
     * 
     * @param username the username
     * @return the user id
     * @throws SQLException ignore
     */
    public static int getUserID(String username) throws SQLException {
        int userID = -1;

        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT User_ID FROM users WHERE User_Name ='" + username + "'";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            userID = result.getInt("User_ID");
        }
        return userID;
    }
    
    /**
     * Check if password is a valid password
     * 
     * @param userID the user id
     * @param password the user password
     * @return if the password is valid or not
     * @throws SQLException ignore
     */
    public static boolean validPassword(int userID, String password) throws SQLException {

        Statement statement = JDBC.connection.createStatement();
        String sqlStatement = "SELECT Password FROM users WHERE User_ID ='" + userID + "'";
        ResultSet result = statement.executeQuery(sqlStatement);

        while (result.next()) {
            if (result.getString("Password").equals(password)) {
                return true;
            }
        }
        return false;
    }  

    /**
     * Reminder list for upcoming appointment
     */
    public static void createReminderList() {

        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(
                    "SELECT * FROM appointments\n"
                    + "WHERE appointments.User_ID = ? "
                    + "\nORDER BY Start");
            ps.setInt(1, Users.getUserID());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                //pulls start time from database and converts it into local time zone
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();

                //pulls select data fields for use in appointmentReminderOL observablelist
                int appointmentId = rs.getInt("Appointment_Id");
                int user = rs.getInt("User_ID");

                //inserts Appointment objects into observablelist
                appointmentReminder.add(new Appointments(appointmentId, start, user));
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
