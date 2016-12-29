package cn.bronzeware.muppet.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Listeners implements Listened{

	private  HashMap<EventType,List<Listener>> allListeners = new HashMap<>();
	//private ListenerFactory factory = new ListenerFactory();
	
	
	private static final int INIT_SIZE = 5;
	public Listeners(){
		EventType[] types = EventType.values();
		for(EventType type:types){
			allListeners.put( type, new ArrayList<Listener>(INIT_SIZE));
		}
		
	}
	
	public void test(){
		
	}

	@Override
	public synchronized void addListener(EventType type, Listener listener) 
	throws UnSupportedEventTypeException{
		if(!allListeners.containsKey(type)){
			throw new UnSupportedEventTypeException(type==null?"":type.toString());
		}
		allListeners.get(type).add(listener);
	}
	
	
	public void event(EventType type,Event event){
		List<Listener> list = allListeners.get(type);
		for(Listener listener:list){
			listener.event(type, event);
		}
	}
	
	
}
