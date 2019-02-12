package main.navironrails;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.esri.arcgisruntime.mapping.view.MapView;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

public class UserFeedback {
	
	private MapView mapView;
	
	public void getFeedback(Layer layer) {
		mapView.setOnMouseClicked(e -> {
			  // was the main button pressed?
			  if (e.getButton() == MouseButton.PRIMARY) {


			    // get the screen point where the user clicked or tapped
			    Point2D screenPoint = new Point2D(e.getX(), e.getY());


			    //Layer layer = null;
				// ...
			    final ListenableFuture<IdentifyLayerResult> identifyFuture = mapView.identifyLayerAsync(layer, screenPoint, 20, false, 25);


			  		// add a listener to the future
			  		identifyFuture.addDoneListener(() -> {
			  		  try {
			  		    // get the identify results from the future - returns when the operation is complete
			  		    IdentifyLayerResult identifyLayerResult = identifyFuture.get();
			  		    // ...
			  		// a reference to the feature layer can be used, for example, to select identified features
			  		    if (identifyLayerResult.getLayerContent() instanceof FeatureLayer) {
			  		      FeatureLayer featureLayer = (FeatureLayer) identifyLayerResult.getLayerContent();
			  		      // select all features that were identified 
			  		      List<Feature> features = identifyLayerResult.getElements().stream().map(f -> (Feature) f).collect(Collectors.toList());
			  		      featureLayer.selectFeatures(features);
			  		    }


			  		  } catch (InterruptedException | ExecutionException ex) {
			  		    // ... must deal with checked exceptions thrown from the async identify operation
			  		  }
			  		});
			  }
			});
	
	}

}
