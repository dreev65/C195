
package model;

import java.time.LocalDateTime;

/**
 *
 * @author Daniel Reeve
 */
public class Reports {
    
    private String Month;
    private int Online;
    private int InPerson;
    private int Phone;
    private String type;
    private LocalDateTime start;
    
    /**
     * Constructor used for the MonthType report
     * 
     * @param Month the month
     * @param Online online type
     * @param InPerson in person type
     * @param Phone phone type
     */
    public Reports(String Month, int Online, int InPerson, int Phone) {
        this.Month = Month;
        this.Online = Online;
        this.InPerson = InPerson;
        this.Phone = Phone;
    }
    
    /**
     * Used for the getMonthTypeData function in DBReports to hold month and type combinations 
     * 
     * @param type appointment type
     * @param start appointment start
     */
    public Reports(String type, LocalDateTime start) {
        this.type = type;
        this.start = start;
    }
    
    
//Setters

    /**
     *
     * @param Month the month to set
     */
    private void setMonth(String Month) {
        this.Month = Month;
    }
    
    /**
     *
     * @param Online online appointment type to set
     */
    private void setOnline(int Online) {
        this.Online = Online;
    }
    
    /**
     *
     * @param InPerson in person appointment type to set
     */
    private void setInPerson(int InPerson) {
        this.InPerson = InPerson;
    }
    
    /**
     *
     * @param Phone phone appointment type to set
     */
    private void setPhone(int Phone) {
        this.Phone = Phone;
    }
    
    /**
     *
     * @param type the appointment type to set
     */
    private void setType(String type) {
        this.type = type;
    }
    
    /**
     *
     * @param start the start time to set
     */
    private void setStart(LocalDateTime start) {
        this.start = start;
    }
    
    
//Getters

    /**
     *
     * @return the month
     */
    public String getMonth() {
        return Month;
    }
    
    /**
     *
     * @return online appointment type
     */
    public int getOnline() {
        return Online;
    }
    
    /**
     *
     * @return in person appointment type
     */
    public int getInPerson() {
        return InPerson;
    }
    
    /**
     *
     * @return phone appointment type
     */
    public int getPhone() {
        return Phone;
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
     * @return the appointment start
     */
    public LocalDateTime getStart() {
        return start;
    }
    
}
