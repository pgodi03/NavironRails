package navironrails.civilsystems.railwaysystem;

import java.util.ArrayList;
import navironrails.civilsystems.entitycomponentarchitecture.Subscriber;
import navironrails.civilsystems.entitycomponentarchitecture.Monitor;

public class RailComponent implements RailInterface{
    int id;
    
    ArrayList<Subscriber> subscriptions;
    
    /**
     *
     */
    public RailComponent(){
        subscriptions = new ArrayList<>();
    }

    @Override
    public void subscribe(Monitor systemMonitor) {
        subscriptions.add(systemMonitor.fetchSubscription());
    }
    
    /**
     *
     * @return
     */
 
    
}
