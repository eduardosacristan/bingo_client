package com.eduardo_sacristan.bingoclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class of the application. This class contains the appropiate methods
 * to launch the application
 */
public class ClientApplication extends Application {
    /**
     * This method launches the JavaFX main scene
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("SUPER JAVAFX BINGO");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main mathod. It launches the start method
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}