package main.navironrails;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;

public class MyMapApp extends Application{

	private MapView mapView;
	
	UserFeedback fb = new UserFeedback();

	public void start(Stage stage) throws Exception {

		StackPane stackPane = new StackPane();
		Scene scene = new Scene(stackPane);
		
		stage.setTitle("Display Map Sample");
		stage.setWidth(800);
		stage.setHeight(700);
		stage.setScene(scene);
		stage.show();
		
		ArcGISMap map = new ArcGISMap(Basemap.createImagery());
		//
		mapView = new MapView();
		mapView.setMap(map);
		
		//fb.getFeedback(map);

		stackPane.getChildren().addAll(mapView);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}