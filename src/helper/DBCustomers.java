package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Customers;

/**
 * Class that holds the database queries used for the customers table view page of the application
 * 
 * @author Daniel Reeve
 */
public class DBCustomers {

    static ObservableList<Customers> customersTable = FXCollections.observableArrayList();

    /**
     * Used to get a list of all the customers
     * 
     * @return the customer list
     * @throws SQLException throws stack trace
     */
    public static ObservableList<Customers> getCustomersTable() throws SQLException {
        try {
            String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, customers.Division_ID, first_level_divisions.Division_ID, Division, first_level_divisions.Country_ID, countries.Country_ID, Country\n"
                    + "FROM customers, first_level_divisions, countries\n"
                    + "WHERE customers.Division_ID = first_level_divisions.Division_ID && countries.Country_ID = first_level_divisions.Country_ID\n"
                    + "ORDER BY Customer_ID";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerID = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String division = rs.getString("Division");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                String country = rs.getString("Country");

                Customers C = new Customers(customerID, customerName, address, division, postalCode, phone, country);
                customersTable.add(C);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customersTable;
    }

    /**
     * Used to see if there are any appointments for a specific customer. 
     * Gets the deleted appointment information for the deleted appointment report
     * 
     * @param id the customer id
     * @return a list of the appointments tied to a customer
     * @throws Exception ignore
     */
    public static ObservableList<Appointments> checkForAppointments(int id) throws Exception {

        ObservableList<Appointments> appointments = FXCollections.observableArrayList();

        try {
            String sql = "SELECT customers.Customer_ID, appointments.*\n"
                    + "FROM customers, appointments\n"
                    + "WHERE customers.Customer_ID = ? && appointments.Customer_ID = ?\n"
                    + "ORDER BY Appointment_ID";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, id);
            ps.setInt(2, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int apptID = rs.getInt("Appointment_ID");
                String type = rs.getString("Type");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                
               Appointments A = new Appointments(apptID, userID, custID, type);
               appointments.add(A);
            }
 
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointments;
    }
    
    /**
     * Gets the deleted appointment information for the deleted appointment report
     * @param id the appointment id
     * @return the appointment 
     * @throws SQLException ignore
     */
    public static ObservableList<Appointments> appointmentToAddToReport(int id) throws SQLException {
        
        ObservableList<Appointments> appointmentToAddToReport = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT appointments.*"
                   + "FROM appointments"
                   + "WHERE appointments.Appointment_ID = ?";

           PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
           
           ps.setInt(1, id);
           ResultSet rs = ps.executeQuery();

           while (rs.next()) {

               //assigns variables with data from db for insertion into appointments observablelist//
               int apptID = rs.getInt("Appointment_ID");
               String type = rs.getString("Type");
               int custID = rs.getInt("Customer_ID");
               int userID = rs.getInt("User_ID");

               Appointments A = new Appointments(apptID, userID, custID, type);
               appointmentToAddToReport.add(A);
           }
       } catch (SQLException throwables) {
           throwables.printStackTrace();
       }
       return appointmentToAddToReport;
    }
        

    /**
     * Used to delete the customer from the database
     * 
     * @param id the customer id
     * @throws Exception throws stack trace
     */
    public static void deleteDBCustomer(int id) throws Exception {

        try {
            String sql = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, id);

            ps.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
