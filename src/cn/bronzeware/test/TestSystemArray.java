package cn.bronzeware.test;

import java.util.logging.Logger;

public class TestSystemArray {

	
	public static void main(String[] args){
		int[] array1 = new int[4];
		System.arraycopy(array1, 0, array1, 0, 3);
		System.out.println(array1.length);
		//Logger.
	}
}
