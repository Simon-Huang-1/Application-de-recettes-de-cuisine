package fr.tse.fise2.prinfo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point of our application. The Main class must inherit from javafx.Application to be able to launch
 *
 */
public class MainApp extends Application {


    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
 	
        String fxmlFile = "/fxml/GUI1.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(rootNode, 1284, 739);
        scene.getStylesheets().add("/styles/bootstrap3.css");

        stage.setTitle("Test interface PRINFO");
        stage.setScene(scene);
        stage.show();
        
    }
}
