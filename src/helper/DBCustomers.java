package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
     * Used to see if there are any appointments for a specific customer
     * @param id the customer id
     * @return if there are any appointments
     * @throws Exception ignore
     */
    public static boolean checkForAppointments(int id) throws Exception {

        int appointmentID = -1;
        boolean appointmentCheck = false;

        try {
            String sql = "SELECT customers.Customer_ID, appointments.Customer_ID, Appointment_ID\n"
                    + "FROM customers, appointments\n"
                    + "WHERE customers.Customer_ID = ? && appointments.Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ps.setInt(1, id);
            ps.setInt(2, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                appointmentID = rs.getInt("Appointment_ID");
            }

            if (appointmentID == -1) {
                appointmentCheck = true;
            } else {
                appointmentCheck = false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentCheck;
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
