
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
    
    
    public Reports(String Month, int Online, int InPerson, int Phone) {
        this.Month = Month;
        this.Online = Online;
        this.InPerson = InPerson;
        this.Phone = Phone;
    }
    
    public Reports(String type, LocalDateTime start) {
        this.type = type;
        this.start = start;
    }
    
    
//Setters
    private void setMonth(String Month) {
        this.Month = Month;
    }
    
    private void setOnline(int Online) {
        this.Online = Online;
    }
    
    private void setInPerson(int InPerson) {
        this.InPerson = InPerson;
    }
    
    private void setPhone(int Phone) {
        this.Phone = Phone;
    }
    
    private void setType(String type) {
        this.type = type;
    }
    
    private void setStart(LocalDateTime start) {
        this.start = start;
    }
    
    
//Getters
    public String getMonth() {
        return Month;
    }
    
    public int getOnline() {
        return Online;
    }
    
    public int getInPerson() {
        return InPerson;
    }
    
    public int getPhone() {
        return Phone;
    }
    
    public String getType() {
        return type;
    }
    
    public LocalDateTime getStart() {
        return start;
    }
    
}
