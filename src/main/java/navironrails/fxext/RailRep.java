package navironrails.fxext;

import java.util.ArrayList;
import javafx.beans.binding.Bindings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author owner
 */
public class RailRep extends Circle implements GFXRep {
    ArrayList<CouplingRep> couplings;
    Boolean isCoupled;
    
    public RailRep(double x, double y){
        this.setCenterX(x);
        this.setCenterY(y);
        
        this.setRadius(8);
        this.setStroke(Color.BLACK);
        this.setFill(Color.LIGHTGRAY);
        
        this.couplings = new ArrayList<>();
    }
    
    @Override
    public boolean isCoupledAnchor(){
        return false;
    }
    
    @Override
    public void coupleToTrack(CouplingRep cpl){
        couplings.add(cpl);
        
        Bindings.bindBidirectional(cpl.couplingPropertyX, centerXProperty());
        Bindings.bindBidirectional(cpl.couplingPropertyY, centerYProperty());
    }

    @Override
    public CouplingRep unCouple(){
        int i = couplings.size() -1;
        if(i != 0){
            CouplingRep tmp = couplings.remove(i);
            
            if((i-1) == 0){
                setIsCoupled(false);
            }
            
            tmp.couplingPropertyX.unbind();
            tmp.couplingPropertyY.unbind();
            return tmp;
        } return null;
    }
    
    @Override
    public void setIsCoupled(boolean data){ isCoupled = data; }
}

