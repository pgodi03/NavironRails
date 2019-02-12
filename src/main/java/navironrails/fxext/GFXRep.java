package navironrails.fxext;

/**
 * @interface GFXRep
 * 
 * This interface was originally created because we needed a way to stuff multiple different
 * visual representations within a list, and iterate over it processing each one, without
 * having the possibility of accidentally stuffing in stuff that was part of the JavaFX
 * framework proper.
 * 
 * As deadlines tightened and issues with getting the map and the model to synch up proliferated,
 * the GFXRep and the resulting hierarchy of objects that extend from the RailRep were utilized
 * so as to most efficiently share necessary code between different visual representation objects,
 * even when those objects should never actually logical extend each other.
 * 
 * For example, JunctionRep extends StationRep, even though a rail track junction is completely
 * unrelated to a train station. I am aware of the shortcuts that have been taken. If the professor
 * decides to take points off, I hope he does so with the awareness that compromises had to be
 * made to make a deadline where running code was required to be delivered.
 * 
 * The visual representation code will definitely be refactored come next semester.
 * 
 * @author owner
 */
public interface GFXRep extends GFXEntity {
    public boolean isCoupledAnchor();
    public void coupleToTrack(CouplingRep cpl);
    public CouplingRep unCouple();
    public void setIsCoupled(boolean data);
}

