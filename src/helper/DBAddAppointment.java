package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that holds the database queries used to add appointments
 * 
 * @author Daniel Reeve
 */
public class DBAddAppointment {

    /**
     * Finds all contacts to be used in the contacts box of the application
     * 
     * @return the contacts list
     */
    public static ObservableList<String> getAppointmentContacts() {

        ObservableList<String> ContactsBox = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contact = rs.getString("Contact_Name");

                ContactsBox.add(contact);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return ContactsBox;
    }

    /**
     * Used to get the contact id of the contact that was selected
     * 
     * @param index the index of the selected contact
     * @return the contact id
     * @throws SQLException throws the stack trace
     * @throws Exception ignore
     */
    public static int getContactID(int index) throws SQLException, Exception {

        int contactID = -1;

        try {
            String sql = "SELECT Contact_ID "
                    + "FROM contacts "
                    + "WHERE  Contact_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, index);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                contactID = rs.getInt("Contact_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return contactID;
    }
    
    /**
     * Used to get the appointments associated with a particular customer
     * 
     * @param customerID the customer id
     * @return the list of appointments
     * @throws SQLException throws the stack trace
     * @throws Exception ignore
     */
    public static List<LocalDateTime[]> getCustomerAppointments(int customerID) throws SQLException, Exception {
        
        List<LocalDateTime[]> list = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            
            ps.setInt(1, customerID);
            
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                
                LocalDateTime[] arr = {start, end};
                
                list.add(arr);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
    
    public static List<String[]> getCustomerAppointmentsForUpdate(int customerID) throws SQLException, Exception {
        
        List<String[]> list = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM appointments "
                       + "WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            
            ps.setInt(1, customerID);
            
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String ID = rs.getString("Appointment_ID");
                LocalDateTime localStart = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime localEnd = rs.getTimestamp("End").toLocalDateTime();
                
                String start = String.valueOf(localStart);
                String end = String.valueOf(localEnd);
                
                
                String[] arr = {ID, start, end};
                
                list.add(arr);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return list;
    }
}
