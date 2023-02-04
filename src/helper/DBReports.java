package helper;

import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

/**
 * Class that holds the database queries used for the monthType report
 * 
 * @author Daniel Reeve
 */
public class DBReports {

    static ObservableList<Appointments> monthType = FXCollections.observableArrayList();

    //MonthType Report

    /**
     * Used to get a list for the MonthType Report
     * @return the monthType list
     * @throws SQLException throws stack trace
     */
    public static ObservableList<Appointments> getMonthTypeData() throws SQLException {
        try {
            String sql = "SELECT Appointment_ID, Type, Start\n"
                    + "FROM appointments";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();

                Appointments A = new Appointments(appointmentID, type, start);
                monthType.add(A);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return monthType;
    }
}
