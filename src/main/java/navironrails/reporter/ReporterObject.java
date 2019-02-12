package navironrails.reporter;

import javafx.scene.control.TextArea;

public class ReporterObject {
	TextArea reportBar;
	   class materialsData{
	            // This object should handle a connection to a MariaDB Database
	            // backend where the materials data is stored.
	            
	            // This object supports querying the remote database for general
	            // information on the parts. This data, once found, may then be
	            // processed record by record into a local table and dispatched
	            // to the remote database as an order form. The remote database
	            // then updates vendor data to reflect the order throughput to
	            // each vendor, and changing vendor status(if the vendor is still
	            // accepting order, etc) if necessary.
	        }
	   
	    /**
	     *
	     * @param reportBar
	     */
	    public ReporterObject(TextArea reportBar){
	       this.reportBar = reportBar;
	   }
	   
	    /**
	     *
	     */
	    public void generateReport(){
	            // Base report sections should be by track section type. The report
	            // generates a new section for the following track types:
	            // 1. Station point
	            // 2. Unraised track
	            // 3. Raised track
	            // 4. Banked track
	            // 5. track over bridge
	            // 6. track within tunnel
	            // 7. junction point
	            
	            // The reporter wihin the text pane also needs to provide this
	            // information.
	            
	            // Each section should display a list of components(material types) needed,
	            // the amount needed for each component, the price per unit offered per vendor,
	            // and available/required shipping methods per vendor.
	            
	            // Please note: This method only generates a paper report. It does so by
	            // loading a latex template, filling out the latex template, and then
	            // using the OS facilities to process the template and convert to a .pdf file. 
	        }
	        
	    /**
	     *
	     */
	    public void displayStats(){
	            // display some base statistics in the status box part of the window:
	            // Statistics should be:
	            //
	            // 1. Number of stations
	            // 2. Number of junction points
	            // 3. Total Amount of track laid.
	            // 4. Total amount of track laid for each track type.
	            // 5. Number of bridges, tunnels.
	            // 6. Rail Type(maglev, diesel).
	        } 

}
