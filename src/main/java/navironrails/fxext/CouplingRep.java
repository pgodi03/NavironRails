package navironrails.fxext;

import javafx.beans.property.DoubleProperty;
import navironrails.civilsystems.railwaysystem.Track;

/**
 *
 * @author owner
 */
public class CouplingRep {
    DoubleProperty couplingPropertyX;
    DoubleProperty couplingPropertyY;
    
    public CouplingRep(DoubleProperty x, DoubleProperty y){
        couplingPropertyX = x;
        couplingPropertyY = y;
    }
    
    Track coupling;
}

