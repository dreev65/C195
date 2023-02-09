package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Appointments class used to store the appointment data
 *
 * @author Daniel Reeve
 */
public class Appointments {

    private static ObservableList<Appointments> deletedAppointments = FXCollections.observableArrayList();

    private int appointmentID;
    private int customerID;
    private int userID;
    private int contactID;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String customerName;
    private DateTime createDate;
    private String createdBy;
    private Timestamp updateTime;
    private String updatedBy;

    /**
     * Used for the appointment table view
     *
     * @param appointmentID the appointment id
     * @param title the appointment title
     * @param description the appointment description
     * @param location the appointment location
     * @param contact the appointment contact
     * @param type the appointment type
     * @param start the appointment start time and date
     * @param end the appointment end time and date
     * @param customerID the customer id
     * @param userID the user id
     */
    public Appointments(int appointmentID, String title, String description, String location, String contact, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        startTime = start;
        endTime = end;
        this.customerID = customerID;
        this.userID = userID;
    }

    /**
     * Used for the appointment reminder alert
     *
     * @param appointmentId the appointment id
     * @param newLocalStart the appointment start time and date
     * @param user the user id
     */
    public Appointments(int appointmentId, LocalDateTime newLocalStart, int user) {
        appointmentID = appointmentId;
        startTime = newLocalStart;
        userID = user;
    }

    /**
     * Used when deleting a customer
     *
     * @param appointmentID the appointment id
     * @param userID the user id
     * @param custID the customer id
     * @param type the appointment type
     */
    public Appointments(int appointmentID, int userID, int custID, String type) {
        this.appointmentID = appointmentID;
        this.userID = userID;
        customerID = custID;
        this.type = type;
    }

    /**
     * Creates an observable list of deleted customers
     *
     * @param newAppointments the new appointment to add
     */
    public static void addDeletedAppointments(Appointments newAppointments) {
        deletedAppointments.add(newAppointments);
    }

    /**
     *
     * @return the deleted appointments list
     */
    public static ObservableList<Appointments> getDeletedAppointments() {
        return deletedAppointments;
    }

//Setters//
    /**
     * @param appointmentID the appointment id to set
     */
    private void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;//auto generated
    }

    /**
     * @param customerID the customer id to set
     */
    private void setCustomerID(int customerID) {
        this.customerID = customerID;//auto generated
    }

    /**
     * @param userID the user id to set
     */
    private void setUserID(int userID) {
        this.userID = userID;//auto generated        
    }

    /**
     * @param contactID the contact id to set
     */
    private void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * @param title the tile to set
     */
    private void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param description the description to set
     */
    private void setDescription(String description) {
        this.description = description;
    }

    /**
     * @param location the location to set
     */
    private void setLocation(String location) {
        this.location = location;
    }

    /**
     * @param contact the contact to set
     */
    private void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @param type the type to set
     */
    private void setType(String type) {
        this.type = type;
    }

    /**
     * @param startTime the start time and date to set
     */
    private void setStart(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @param endTime the end time and date to set
     */
    private void setEnd(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * @param createDate the time and date of creation to be set
     */
    private void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * @param createdBy sets who created the appointment
     */
    private void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @param updateTime sets the time and date the appointment was updated
     */
    private void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @param updatedBy sets who updated the appointment
     */
    private void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @param customerName the customer name to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @param consultant sets the consultant
     */
    public void setConsultant(String consultant) {
        this.createdBy = consultant;
    }

//Getters//
    /**
     *
     * @return the appointment ID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     *
     * @return the customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     *
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     *
     * @return the contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     *
     * @return the appointment title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return the appointment description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return the appointment location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @return the appointment contact
     */
    public String getContact() {
        return contact;
    }

    /**
     *
     * @return the appointment type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return the appointment start time and date
     */
    public LocalDateTime getStart() {
        return startTime;
    }

    /**
     *
     * @return the appointment end time and date
     */
    public LocalDateTime getEnd() {
        return endTime;
    }

    /**
     *
     * @return the appointment creation time and date
     */
    public DateTime getCreateDate() {
        return createDate;
    }

    /**
     *
     * @return who created the appointment
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @return the appointment update time and date
     */
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    /**
     *
     * @return who updated the appointment
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *
     * @return the contact name
     */
    public String getConsultantName() {
        return this.createdBy;
    }

    /**
     * Overrides the print statement
     *
     * @return the contact name
     */
    @Override
    public String toString() {
        return contact;
    }

}
