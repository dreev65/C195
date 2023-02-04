package model;

/**
 * Users class used to store the user data
 * @author Daniel Reeve
 */
public class Users {

    private static int userID;
    private static String username;
    private static String password;

//Constructor//

    /**
     * Used for the login page to verify valid information
     * 
     * @param userID the user id
     * @param username the username
     * @param password the password
     */
    public Users(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }
    
    public Users(){
        //ignore
    }
//Setters//

    /**
     *
     * @param userID the user id to set
     */
    public static void setUserID(int userID) {
        Users.userID = userID;
    }

    /**
     *
     * @param username the username to set
     */
    public static void setUsername(String username) {
        Users.username = username;
    }

    /**
     *
     * @param password the password to set
     */
    public static void setPassword(String password) {
        Users.password = password;
    }

//getters

    /**
     *
     * @return the user id
     */
    public static int getUserID() {
        return userID;
    }

    /**
     *
     * @return the username
     */
    public static String getUsername() {
        return username;
    }

    /**
     *
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }
}
