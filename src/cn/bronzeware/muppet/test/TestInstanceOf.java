package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.resource.ResourceInfo;
import cn.bronzeware.muppet.resource.TableInfo;

public class TestInstanceOf {

	
	
	public static void main(String[] args){
		//System.out.println();
		ResourceInfo resourceInfo = null;
		if(resourceInfo instanceof TableInfo){
			System.out.println("hihie");
		}
	}
}
