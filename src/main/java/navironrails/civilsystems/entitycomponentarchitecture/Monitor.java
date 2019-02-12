package navironrails.civilsystems.entitycomponentarchitecture;
import java.util.ArrayList;

/**
 * Monitor is the base class of the service side
 * of a simple entity component system. Additional monitors
 * may extend this class and provide their own functionality.
 * 
 * components are represented as "subscribers", which are
 * attached to rail components. The rail components themselves
 * do not need to know anything about the monitors themselves;
 * they simply contain a collection of subscribers in the form
 * of a list of "subscriptions".
 * 
 * The monitors are in charge of defining their own particular
 * subscriber type, which will always extend the Subscriber base
 * class.
 * 
 * There is a common subscriber interface to handle basic
 * coordination between subscribers and monitors, as well as a
 * railComponent interface (e.g. RailInterface) for coordination
 * between components and monitors.
 * 
 * Additional functionality beyond this is handled by specific
 * Monitor and Subscriber subclasses.
 * 
 * Things are handled this way because we really don't know
 * exactly *what* rail systems we will be developing for this
 * application quite yet. An ECS System allows for very fast
 * prototyping and a excellent decoupling, which is a must
 * for the pace of these sorts of courses.
 * @author owner
 */

public class Monitor {
    ArrayList<Subscriber> subscribers;
    
    public Monitor(){
        subscribers = new ArrayList<>();
    }
    
    public Subscriber fetchSubscription(){
        Subscriber tmp = new Subscriber();
        subscribers.add(tmp);
        return tmp;
    }
}

