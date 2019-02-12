package navironrails.fxext;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public class Anchor extends RailRep {    
    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
        super(x.get(), y.get());
        setFill(color.deriveColor(1, 1, 1, 0.5));
        setStroke(color);
        setStrokeWidth(2);
        setStrokeType(StrokeType.OUTSIDE);
        
        Bindings.bindBidirectional(x, centerXProperty());
        Bindings.bindBidirectional(y, centerYProperty());
        
        isCoupled = false;
    }
    
    @Override
    public boolean isCoupledAnchor(){ 
    	return isCoupled; 
    }
}
