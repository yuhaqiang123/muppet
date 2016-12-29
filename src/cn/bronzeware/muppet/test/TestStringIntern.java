package cn.bronzeware.muppet.test;

import java.io.UnsupportedEncodingException;

public class TestStringIntern {

	
	
	public static void main(String[] args) throws UnsupportedEncodingException{

		String str1= "软件";
		String str2 = new String("软件");
		System.out.println(str1==str2);
		/*String str1= new StringBuilder("计算机").append("软件").toString();
		String str2= new StringBuilder("计算机").append("软件").toString();
		//System.out.println(str1==str2);
		String str4= new StringBuilder("计算机").toString();
		String str5= new StringBuilder("计算机").toString();
		//System.out.println(str4==str5);
		char a = '余';
		String str6 = String.valueOf(a);
		String str7 = new StringBuilder(str6).toString();
		System.out.println(str6==str7);
		System.out.println(=="余");*/
		//System.out.println(str4==str4.intern());
		//System.out.println(str1.intern()==str1);
		/*System.out.println(str1.intern()==str1);
		String str2= new StringBuilder("ja").append("va").toString();
		System.out.println(str2.intern()==str2);*/
		String str3 = "计算机软件";
		System.out.println(str1.intern()==str1);
	}
}
