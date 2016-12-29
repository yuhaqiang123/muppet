package cn.bronzeware.muppet.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.print.attribute.HashAttributeSet;

public class TestHashMap1 {

	public static void main(String[] args){
		int s = Integer.MAX_VALUE>>27;
		System.out.println(s);
		TestHashMap1 map1 = new TestHashMap1();
		map1.test();
		map1.getClass();
		Map<String,String> map = new HashMap<>();
		map.put("23", "fe");
		//map.
		Set<Entry<String, String>> set = map.entrySet();
		Map.Entry<String,String> entry = set.iterator().next();
		System.out.println(entry.getKey());
		set.add(entry);
	}
	void test(){
		
	}
}
