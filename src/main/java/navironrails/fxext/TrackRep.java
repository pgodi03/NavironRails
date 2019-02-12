package navironrails.fxext;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;


/**
 *
 * @author owner
 */
public class TrackRep extends Group implements GFXTrack {
    CubicCurve curve;
    Line cl1;
    Line cl2;
    
    Anchor startAnchor;
    Anchor endAnchor;
    
    Anchor cl1Anchor;
    Anchor cl2Anchor;
    
    public TrackRep(double sx, double sy, double x, double y){
        curve = new CubicCurve();
        
        curve.setStartX(sx);
        curve.setStartY(sy);
        
        curve.setControlX1(sx - 10);
        curve.setControlY1(sy + 15);
        curve.setControlX2(sx + 10);
        curve.setControlY2(sy - 15);
        
        curve.setEndX(x);
        curve.setEndY(y);
        
        curve.setFill(Color.TRANSPARENT);
        curve.setStroke(Color.DARKORANGE);
        curve.setStrokeWidth(2);
        
        cl1 = new BoundLine(curve.controlX1Property(), curve.controlY1Property(), curve.startXProperty(), curve.startYProperty());
        cl2 = new BoundLine(curve.controlX2Property(), curve.controlY2Property(), curve.endXProperty(), curve.endYProperty());
        
        startAnchor = new Anchor(Color.TOMATO, curve.startXProperty(), curve.startYProperty());
        endAnchor = new Anchor(Color.TOMATO, curve.endXProperty(), curve.endYProperty());
        
        cl1Anchor = new Anchor(Color.LIGHTGRAY, curve.controlX1Property(), curve.controlY1Property());
        cl2Anchor = new Anchor(Color.LIGHTGRAY, curve.controlX2Property(), curve.controlY2Property());
        
        this.getChildren().addAll(curve, cl1, cl2, startAnchor, endAnchor, cl1Anchor, cl2Anchor);
    }
    
    public void addChildrenAndSetActiveRep(RailPane pane){
        pane.getChildren().addAll(curve, cl1, cl2, startAnchor, endAnchor, cl1Anchor, cl2Anchor);
        pane.setActiveRailRep(endAnchor);
        pane.setActiveTrackRep(this);
    }
    
    public void initializeCoupling(CouplingRep cpRep){
        if(!(startAnchor.isCoupled)){
            startAnchor.coupleToTrack(cpRep);
            startAnchor.setIsCoupled(true); 
            startAnchor.toBack();
            startAnchor.setVisible(false);
        } else if(!(endAnchor.isCoupled)){
            endAnchor.coupleToTrack(cpRep);
            endAnchor.setIsCoupled(true);
            startAnchor.toBack();
            endAnchor.setVisible(false);
        }
    }
    
    public Anchor getEndAnchor(){ return endAnchor; }
    public Anchor getStartAnchor(){ return startAnchor; }
    public Anchor getC1Anchor(){ return cl1Anchor; }
    public Anchor getC2Anchor(){ return cl2Anchor; }
    
    public void register(RailPane pane){
        pane.getChildren().addAll(startAnchor, endAnchor, cl1Anchor, cl2Anchor, cl1, cl2, curve);
    }
}

