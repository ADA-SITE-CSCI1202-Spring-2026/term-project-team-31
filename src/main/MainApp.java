package main;

import gui.Dashboard;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Dashboard dashboard = new Dashboard();

        Scene scene = new Scene(dashboard, 900, 620);
        primaryStage.setTitle("Program intialized");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(550);

        primaryStage.setOnCloseRequest(e -> dashboard.stopClock());

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
