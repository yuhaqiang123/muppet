package cn.bronzeware.muppet.resource;

public interface Container<K,V> {

	public V get(K name);
	public void set(K name,V info);
	public boolean contains(K name);
	
	public V get(K name, K name2);
	
}
