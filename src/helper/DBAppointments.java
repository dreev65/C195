package helper;

import model.Appointments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that holds the database queries used for the appointments table view page of the application
 * @author Daniel Reeve
 */
public class DBAppointments {

    /**
     * Populates table view with appointments and applies filter
     * @return the appointments list
     * @throws SQLException throws stack trace
     */
    public static ObservableList<Appointments> getAppointmentsTable() throws SQLException {

        ObservableList<Appointments> appointmentsTable = FXCollections.observableArrayList();

        try {

            String sql = "SELECT appointments.*, contacts.Contact_ID, contacts.Contact_Name\n"
                    + "FROM appointments, contacts\n"
                    + "WHERE appointments.Contact_ID = contacts.Contact_ID\n"
                    + "ORDER BY Appointment_ID";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                //assigns variables with data from db for insertion into appointments observablelist//
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String contact = rs.getString("Contact_Name");
                String type = rs.getString("Type");
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");

                //get database time as local datetime
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();

                Appointments A = new Appointments(appointmentID, title, description, location, contact, type, start, end, customerID, userID);
                appointmentsTable.add(A);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentsTable;
    }

    /**
     * Used to delete the customer from the database
     * @param id the appointment id
     * @throws Exception throws stack trace
     */
    public static void deleteDBAppointment(int id) throws Exception {

        try {
            String sql = "DELETE FROM Appointments WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, id);

            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
