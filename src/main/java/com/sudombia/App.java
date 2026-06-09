package com.sudombia;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/sudombia/fxml/menu.fxml")
        );

        Scene scene = new Scene(loader.load(), 900, 600);

        stage.setTitle("SUDOMBIA");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}