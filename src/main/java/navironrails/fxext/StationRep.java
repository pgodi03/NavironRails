package navironrails.fxext;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import navironrails.civilsystems.railwaysystem.Station;

public class StationRep  extends RailRep implements GFXRep {
    static final File STATION_IMG_FILE = new File("src/resources/icons/stationImg.png");
    static Image img = new Image(STATION_IMG_FILE.toURI().toString());
    
    Station station;
    
    public StationRep(double x, double y){
        super(x, y);
        
        this.setStroke(Color.BLACK);
        this.setFill(new ImagePattern(img));
        
        station = new Station();
    }
}
