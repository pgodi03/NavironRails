package navironrails.surveyor;

import java.io.File;
import java.math.BigDecimal;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapObject {

	/**
	*Instance data.
	*longitude , latitude and elevation: all will be used in the future for various functionalities
	*in the future.
	*/
    
	private String longitude, latitude, elevation;

        Pane mapPane;
        
	WebView browser = new WebView();
	WebEngine engine = browser.getEngine();
        
        BigDecimal lat;
        BigDecimal lng;

        /**
         * Constructor of this class, that initializes the map object loads up the engine and adds it to the rightpane.
         * @param fd
	 */
        
	public MapObject(File fd, Pane object) {
		engine.load(fd.toURI().toString());
                mapPane = object;
	}
        
    /**
     *
     * @return
     */
    public WebView getBrowser(){ return browser; }
    
    /**
     *This class adds the mouse handler and gets the important data of elevation, longitude and latitude off
     *of the maps.html file.
     * @param pane: the handle is added to the pane in which the map is initially added on to.
     */
    public void addHandle(Pane pane) {
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) -> {
            System.out.println("mouse click detected!");
            engine.setJavaScriptEnabled(true);
            
            //latitude = String.valueOf(engine.executeScript("document.getLat()"));
            latitude = String.valueOf(engine.executeScript("document.latitude"));
            
            
            //	System.out.println("Elevation: " + elevation);
            //	System.out.println("Longitude: " + longitude);
            System.out.println("Latitude: " + latitude);
        });
        
    }
    
    //10/27/2018
    //you need to put a zoom function.
    public int getZoom() {
        int zoom;
        String str;
        str = String.valueOf(engine.executeScript("document.getZoom()"));
        zoom = Integer.valueOf(str);
        return zoom;
    }
    
    public void zoomIn() {
        engine.executeScript("document.zoomIn()");
    }
    
    public void zoomOut() {
        engine.executeScript("document.zoomOut()");
    }
    
    public void scrollUp() {
        engine.executeScript("document.scrollUp()");
    }
    public void scrollDown() {
        engine.executeScript("document.scrollDown()");
    }
    
    public void scrollRight() {
        engine.executeScript("document.scrollRight()");
    }
    public void scrollLeft() {
        engine.executeScript("document.scrollLeft()");
    }
    
//    public void panToPoint(double x, double y){
//        double panXval = 0.00001;
//        double panYval = 0.00001;
//        
//        double tmpx = (double) engine.executeScript("document.getCenterLng()");
//        double tmpy = (double) engine.executeScript("document.getCenterLat()");
//        
//        tmpx += x*panXval;
//        tmpy += y*panYval;
//        
//        lat = BigDecimal.valueOf(tmpy);
//        lng = BigDecimal.valueOf(tmpx);
//        
//        String paramString = "document.panToPoint(" + lat.toString() + ", " + lng.toString() + ")";
//        engine.executeScript(paramString);
//    }
    
    public void panToPoint(double x, double y){
        String paramString = "document.panToPoint(" + Double.toString(x) + "," + Double.toString(y) + ")";
        engine.executeScript(paramString);
    }
    
    
    
    //Getters and Setters.
    public String getLongitude() {
        return longitude;
    }
    
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    public String getLatitude() {
        return latitude;
    }
    
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    
    public String getElevation() {
        return elevation;
    }
    
    public void setElevation(String elevation) {
        this.elevation = elevation;
    }
    
    
    
}
