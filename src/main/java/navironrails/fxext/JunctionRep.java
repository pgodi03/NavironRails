package navironrails.fxext;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import navironrails.civilsystems.railwaysystem.Junction;

public class JunctionRep extends RailRep implements GFXRep {
    static final File JUNCTION_IMG_FILE = new File("src/resources/icons/junctionImg.png");
    static Image img = new Image(JUNCTION_IMG_FILE.toURI().toString());
    
    Junction junction;
    
    public JunctionRep(double x, double y){
        super(x, y);
        this.setFill(new ImagePattern(JunctionRep.img));
        junction = new Junction();
    }
}
