package navironrails.fxext;

import navironrails.civilsystems.railwaysystem.Track;

public class NormalTrackRep extends TrackRep {
    Track track;
    
    public NormalTrackRep(double sx, double sy, double x, double y){
        super(sx, sy, x, y);
        track = new Track();
    }
}
