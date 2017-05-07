package cn.bronzeware.muppet.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

public class TestArray {

	public static void main(String[] args){
		List<String> list= new ArrayList<>();
		list.add("fewa");
		list.add("fewa");
		list.add("fewa");
		list.add("fewa");
		list.toArray();
		//Collections.
		
		//e.
		ArrayUtil.println(list.toArray(new String[list.size()]));
	}
}
