package navironrails.civilsystems.railwaysystem;
import java.util.ArrayList;


/**
 * ModelObject manages the internal data of the railroad
 * model itself.
 * 
 * 
 * 
 */
public class ModelObject {
ArrayList<RailComponent> railComponents;
    
    public ModelObject(){
        railComponents = new ArrayList<>();
    }
    
    /**
     *
     */
    public void saveModel(){
        
    }
    
    public Station initStation(){
        return new Station();
    }
    
    public Junction initJunction(){
        return new Junction();
    }
    
    public Terminal initTerminal(){
        return new Terminal();
    }
    
    public Track initTrack(){
        return new Track();
    }
    
    //! Validates integrity of model under current constraints.
    
    //! With these method, we essentially need to check the properties
    //! of the various objects inside the ModelData Class. We need to ensure
    //! that the properties of each track, station, terminal, and junction
    //! fall within certain constraints.
    
    //! These constraints are:
    //! 1. Every track must be connected on each end to either a station, a
    //!    a terminal, a junction, or another track. This means that the endpoints
    //!    of each track segment must have the same position as one of these other
    //!    objects.
    //!
    //! 2. No endpoint of any track may have the same location of 2 or more tracks
    //!    unless there exists a junction at the same location.
    //!
    //! 3. No junction, station, or terminal may have the same location as any other
    //!    junction, station, or terminal.
    //!
    //! 4. Track segments may not intersect. (Intersections are emulated by the use
    //!    of junctions, however.)
    //!
    //! 5. The horizontal curvature of a track must not exceed a certain angle,
    //!    unless it is a banked type track.
    //!
    //! 6. The vertical curvature of a track must not exceed a certain angle, unless
    //!    it is a tunneled track, or a raised track.
    //!
    //! 6.a Tunneled tracks and raised tracks have their own elevation values which
    //!     are set for the track segment. The endpoints of these track segements
    //!     must have the same elevation as any tracks they connect to.
    //!
    //! 6.b The elevation of raised tracks must be higher than the elevation of the
    //!     ground over which they are placed, but must not exceed a certain height.
    //!
    //! 7. A section of track may not cross a body of water unless the track is
    //!    a bridge type track.
    //!
    //! 8. No rail component may exist within an area of a map that does not meet
    //!    acceptable zoning requirements. (Requirements are selected by the user
    //!    from the menubar.)
    public boolean validateModel(){
        System.out.println("Validated!");
        return true;
    }
}
