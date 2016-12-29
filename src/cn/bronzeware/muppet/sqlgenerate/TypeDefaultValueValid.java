package cn.bronzeware.muppet.sqlgenerate;

import java.lang.reflect.Type;
import java.util.Date;


/**
 * 指定类型的默认值 验证，返回该项的默认值
 * 例如String的 默认值为"" Integer的默认值为0
 */
public class TypeDefaultValueValid {

	public static   Object  valid(Type t){
		if(t.equals(Integer.TYPE)){
			int i = 0;
			return i;
		}else if(t.equals(Float.TYPE)){
			return 0.0;
		}
		else if(t.equals(Double.TYPE)){
			return 0.0;
		}
		else if(t.equals(String.class)){
			return "";
		}
		else if(t.equals(Date.class)){
			return new Date();
		}
		else if(t.equals(Long.TYPE)){
			return 0;
		}
		return null;
		
	}

	public static void main(String[] strings){
		int i = (Integer) TypeDefaultValueValid.valid(null);
		System.out.println(i);
	}
}
