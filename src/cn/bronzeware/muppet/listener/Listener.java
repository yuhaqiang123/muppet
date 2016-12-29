package cn.bronzeware.muppet.listener;

public interface Listener {

	public EventType eventType();
	public void event(EventType type,Event event);
}
