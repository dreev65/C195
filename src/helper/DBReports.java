package helper;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import model.Reports;

/**
 * Class that holds the database queries used for the monthType report
 * 
 * @author Daniel Reeve
 */
public class DBReports {

    static ObservableList<Reports> monthType = FXCollections.observableArrayList();

    //MonthType Report

    /**
     * Used to get a list for the MonthType Report
     * @return the monthType list
     * @throws SQLException throws stack trace
     */
    public static ObservableList<Reports> getMonthTypeData() throws SQLException {
        try {
            String sql = "SELECT Start, Type\n"
                    + "FROM appointments";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();

                Reports R = new Reports(type, start);
                monthType.add(R);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return monthType;
    }
    
    public static ObservableList<Appointments> getContactsReport() throws SQLException {
        ObservableList<Appointments> contactsReport = FXCollections.observableArrayList();

        try {
            String sql = "SELECT appointments.*, contacts.Contact_ID, contacts.Contact_Name\n"
                    + "FROM appointments, contacts\n"
                    + "WHERE appointments.Contact_ID = contacts.Contact_ID\n"
                    + "ORDER BY Start && contacts.Contact_Name";

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
                contactsReport.add(A);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contactsReport;
    }
}
