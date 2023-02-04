package model;

/**
 * Customers class used to store the customer data
 * @author Daniel Reeve
 */
public class Customers {

    private int customerID;
    private String customerName;
    private String address;
    private String division;
    private String postalCode;
    private String phone;
    private String country;
    private int countryID;
    private int divisionID;
    private String updateTime;
    private String updateBy;

    //Customers Table//

    /**
     * Constructor for the customer table view
     * 
     * @param customerID the customer id
     * @param customerName the customer name
     * @param address the customer address
     * @param division the customer division
     * @param postalCode the customer postal code
     * @param phone the customer phone number
     * @param country the customer country
     */
    public Customers(int customerID, String customerName, String address, String division, String postalCode, String phone, String country) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.division = division;
        this.postalCode = postalCode;
        this.phone = phone;
        this.country = country;
    }

//Setters//
    /**
     *
     * @param customerID the customer id to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     *
     * @param customerName the customer name to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *
     * @param adress the customer address to set
     */
    public void setAddress(String adress) {
        this.address = adress;
    }

    /**
     *
     * @param postalCode the customer postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     *
     * @param phone the customer phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     *
     * @param updateTime the time of last update to set
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    /**
     *
     * @param updateBy the user that performed the update to set
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     *
     * @param division the customer division to set
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     *
     * @param country the customer country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @param countryID the country id to set
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     *
     * @param divisionID the division id to set
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

//Getters//

    /**
     *
     * @return the customer id
     */
    public int getCustomerID() {
        return customerID;
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
     * @return the address 
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @return the time the customer was updated
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     *
     * @return who updated the customer
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     *
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @return the country id
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     *
     * @return the division id
     */
    public int getDivisionID() {
        return divisionID;
    }

    @Override
    public String toString() {
        if (division != null) {
            return (division);
        } else {
            return (country);
        }
    }
}
