package main.navironrails;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavironRailsMain extends Application{
	GUIObject gui;
	/**
     * Initialize the JavaFX engine and graphical context before entering the continuous event processing mode.
     * @param primaryStage JavaFX Window Graphical Context
     */
    @Override
    public void start(Stage primaryStage) {
		
        gui = new GUIObject();
        Scene scene = gui.initializeGUI();
                
        primaryStage.setTitle("Naviron Rails v0.3a");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Stops and releases all resources used in application.
     */
    @Override
    public void stop() {
      
      if (gui.mapView != null) {
    	  gui.mapView.dispose();
      }
    }

    /**
     * The main entry point into the application.
     * @param args CommandLine Arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
