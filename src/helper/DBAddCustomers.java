package helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class that holds the database queries used to add customers
 * 
 * @author Daniel Reeve
 */
public class DBAddCustomers {

    /**
     * Gets a list of the divisions tied to a specific country
     * 
     * @param index the index of the selected country
     * @return the division list
     * @throws SQLException throws the stack trace
     */
    public static ObservableList<String> getDivisionComboBox(int index) throws SQLException {

        ObservableList<String> DivisionComboBox = FXCollections.observableArrayList();

        try {
            String sql = """
                         SELECT Division, first_level_divisions.Country_ID, countries.Country_ID, country
                         FROM first_level_divisions, countries
                         WHERE first_level_divisions.Country_ID = countries.Country_ID && countries.Country_ID ='""" + (index) + "'";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String division = rs.getString("Division");

                DivisionComboBox.add(division);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return DivisionComboBox;
    }

    /**
     * Gets all the countries
     * 
     * @return list of the countries
     * @throws SQLException throws the stack trace
     */
    public static ObservableList<String> getCountryComboBox() throws SQLException {

        ObservableList<String> CountryComboBox = FXCollections.observableArrayList();

        try {
            String sql = "SELECT Country FROM countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String country = rs.getString("Country");

                CountryComboBox.add(country);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return CountryComboBox;
    }

    /**
     * Gets the division id of the selected division
     * 
     * @param selectedDivision the index of the selected division
     * @return the division id
     * @throws SQLException throws the stack trace
     * @throws Exception ignore
     */
    public static int getDivisionID(String selectedDivision) throws SQLException, Exception {

        int divisionID = -1;

        try {
            String sql = """
                         SELECT Division, Division_ID
                         FROM first_level_divisions
                         WHERE Division = '""" + selectedDivision + "'";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                divisionID = rs.getInt("Division_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisionID;
    }
}
