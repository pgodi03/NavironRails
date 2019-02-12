package navironrails.fxext;

import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import main.navironrails.GUIObject;
import navironrails.civilsystems.railwaysystem.ModelObject;
import navironrails.surveyor.MapObject;
/**
 * @brief Handles visual representations of rail objects within a pane.
 * 
 * This object provides a convenient and useful place for us to store and manipulate visual
 * representations of Rail components within the Railway civil system.
 * 
 * @author owner
 */
public class RailPane extends Pane {    
    public enum RailComponentType{
        STATION,
        JUNCTION,
        TERMINAL,
        TRACK,
        RAISED_TRACK,
        TUNNEL_TRACK,
        BRIDGE_TRACK,
        BANKED_TRACK,
    } 
    
    public enum TrackComponentType{
        NORMAL,
        RAISED,
        BANKED,
        TUNNEL,
        BRIDGE;
    }
    
    GFXRep activeRep;
    TrackRep activeTrackRep;
    
    ModelObject model;
    MapObject mapOb;
    ToggleGroup tlbxData;
    
    final Delta dragDelta = new RailPane.Delta();
    
    double oldX;
    double oldY;
    
    public RailPane(ModelObject object, ToggleGroup tlbx, MapObject map){
        model = object;
        tlbxData = tlbx;
        mapOb = map;
        
        this.setOnMouseDragged(this::handleMouseDragged);
        this.setOnMousePressed(this::handleMousePressed);
    }
    
    public double getOldX(){ return oldX; }
    public double getOldY(){ return oldY; }
    
    public void setOldXY(double x, double y){
        oldX = x;
        oldY = y;
    }
    
    public void setActiveRailRep(GFXRep rep){
        activeRep = rep;
    }
    
    public void setActiveTrackRep(TrackRep rep){
        activeTrackRep = rep;
    }
    
    public GFXRep getActiveRailRep(){
        return activeRep;
    }
    
    public TrackRep getActiveTrackRep(){
        return activeTrackRep;
    }
    
    public double getDeltaX(){ return dragDelta.x; }
    public double getDetlaY(){ return dragDelta.y; }
    
    public void setDragDelta(double x, double y){
        dragDelta.x = x;
        dragDelta.y = y;
    }
    
    public void registerRailComponent(RailComponentType rcType,
            double x, double y){
        
        RailRep rc = null;
        
        switch(rcType){
            case STATION:
                rc = new StationRep(x, y);
                this.getChildren().add(rc);
                setActiveRailRep(rc);
                break;
            case JUNCTION:
                rc = new JunctionRep(x, y);
                this.getChildren().add(rc);
                setActiveRailRep(rc);
                break;
            case TERMINAL:
                rc = new TerminalRep(x, y);
                this.getChildren().add(rc);
                setActiveRailRep(rc);
                break;
            case TRACK:
                double sourceX = ((RailRep) activeRep).getCenterX();
                double sourceY = ((RailRep) activeRep).getCenterY();
                
                RailRep currentActive = (RailRep) activeRep;
                CouplingRep cpRep = null;
                
                if(currentActive != null){
                    DoubleProperty xc = ((RailRep)activeRep).centerXProperty();
                    DoubleProperty yc = ((RailRep)activeRep).centerYProperty();
                    
                    cpRep = new CouplingRep(xc, yc);
                }
                
                TrackRep track = new NormalTrackRep(sourceX, sourceY, x, y);
                
                rc = track.getStartAnchor();
                this.attachHandlers(rc);
                
                rc = track.getEndAnchor();
                this.setActiveRailRep(rc);
                this.attachHandlers(rc);
                
                rc = track.getC1Anchor();
                this.attachHandlers(rc);
                
                rc = track.getC2Anchor();
                this.attachHandlers(rc);
                
                rc = null;
                
                track.initializeCoupling(cpRep);
                track.addChildrenAndSetActiveRep(this);
                
                break;
            case RAISED_TRACK:
            case TUNNEL_TRACK:
            case BRIDGE_TRACK:
            case BANKED_TRACK:
        }
        
        if(rc != null){
            this.attachHandlers(rc);
            this.setActiveRailRep(rc);
        }
    }
    
    private void attachHandlers(RailRep rc){
        rc.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = rc.getCenterX() - mouseEvent.getX();
                dragDelta.y = rc.getCenterY() - mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);

                if(mouseEvent.isControlDown()){
                    registerRailComponent(RailComponentType.TRACK, rc.getCenterX(), rc.getCenterY());
                    
                    DoubleProperty xP = rc.centerXProperty();
                    DoubleProperty yP = rc.centerYProperty();
                    
                    CouplingRep tmp = new CouplingRep(xP, yP);
                    activeTrackRep.initializeCoupling(tmp);
                } else {
                    setActiveRailRep(rc);
                }
            }
        });
        
        rc.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                getScene().setCursor(Cursor.HAND);
                toFront();
            }
        });
        
        rc.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                double newX = mouseEvent.getX() + dragDelta.x;
                if (newX > 0 && newX < getScene().getWidth()) {
                    rc.setCenterX(newX);
                }
                double newY = mouseEvent.getY() + dragDelta.y;
                if (newY > 0 && newY < getScene().getHeight()) {
                    rc.setCenterY(newY);
                }
            }
        });
        
        rc.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.HAND);
                }
            }
        });
        
        rc.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });
    }
    
   private void handleMouseDragged(MouseEvent event){
        GUIObject.ToolBoxType tlbxt = (GUIObject.ToolBoxType) tlbxData.getSelectedToggle().getUserData();
        
        double distX = event.getX() - getOldX();
        double distY = event.getY() - getOldY();
        
        if(tlbxt == GUIObject.ToolBoxType.PAN){
            getChildren().stream().filter((tmpRep) -> (tmpRep instanceof RailRep)).forEachOrdered((tmpRep) -> {
                RailRep tmp = (RailRep) tmpRep;
                
                if(tmp.isCoupledAnchor()){ return; }
                 
                setDragDelta(tmp.getCenterX() + distX, tmp.getCenterY() + distY);
                
                double newX = tmp.getCenterX() + distX;
                double newY = tmp.getCenterY() + distY;
                
                tmp.setCenterX(newX);
                tmp.setCenterY(newY);
                
                if(newX > 0 && newX < getWidth()
                        && newY > 0 && newY < getWidth()){
                    tmpRep.setVisible(true);
                } else {
                    tmpRep.setVisible(false);
                }
            });
            
            // Getting the map to pan correctly is almost done!
            // However, issues with the javascript code we wrote
            // need to be resolved first with the google api.
            // The Google Maps map created from the API was initialized
            // incorrectly.
            
            // We need to properly initialize and configure a
            // dummy overlay to hold a map projection. This is the
            // only way we can convert a pixel coordinate to the appropriate
            // lattitude and longitude coordinates.
            
            mapOb.panToPoint(-1*distX, -1*distY);
            
            // Alternatively, another option may have presented itself. Instead
            // of dealing with the lag inherent in the previous approach, we could
            // just store the longitude and lattitudes of the center of the map
            // and modify them by a pan amount. We need to get the correspondence
            // and zoom factor to determine how much distance in lattitude and
            // longitude is represented by a single pixel, but this can be done
            // by iterative trial and error. We shall see.
            
            setOldXY(event.getX(), event.getY());
        }
    } 
   
   private void handleMousePressed(MouseEvent event){
        if(event.isControlDown()){
            return;
        }
        
        if(!(event.getTarget() instanceof RailPane)){
            return;
        }
        
        GUIObject.ToolBoxType ttype = (GUIObject.ToolBoxType)tlbxData.getSelectedToggle().getUserData();
        
        double x = event.getX();
        double y = event.getY();
        
        setOldXY(x, y);
        
        switch(ttype){
            case SELECT:
                break;
            case PAN:
                break;
            case STATION:
                registerRailComponent(RailComponentType.STATION, x, y);
                break;
            case JUNCTION:
                registerRailComponent(RailComponentType.JUNCTION, x, y);
                break;
            case TERMINAL:
                registerRailComponent(RailComponentType.TERMINAL, x, y);
                break;
            case TRACK:
                registerRailComponent(RailComponentType.TRACK, x, y);
            case RAISED:
            case TUNNEL:
            case BRIDGE:
            case BANKED:
            default:
                break;
        }
    }
    
    private class Delta { double x, y; }
}

