package model;

import helper.JDBC;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main class of the program.<br>
 * 
 * <b>The README.txt and login_activity.txt are located in the main folder of the project.</b><br>
 * <b>The JavaDocs are located in the dist folder of the project file.</b>
 * 
 * @author Daniel Reeve
 */
public class DBClientApp extends Application {

    /**
     * Gets the the users default Locale to set the language of the login stage
     * and creates the login stage
     *
     * @param loginStage the login stage for the application
     * @throws IOException ignore
     */
    @Override
    public void start(Stage loginStage) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle("properties/lang", Locale.getDefault());
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"), rb);
        Scene scene = new Scene(root);
        loginStage.setScene(scene);
        loginStage.show();
    }

    /**
     * Opens and closes the database connection. Also, launches the stage for
     * the application.
     *
     * @param args
     * @throws ClassNotFoundException ignore
     */
    public static void main(String[] args) throws ClassNotFoundException {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
