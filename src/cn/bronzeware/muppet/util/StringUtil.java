package cn.bronzeware.muppet.util;

public class StringUtil {




	public static boolean equals(String[] string,String[] string2){
		
		/**
		 * 判断是否同时为null,是返回true
		 */
		if(string==string2&&string==null){
			return true;
		}
		
		/**
		 * 不同时为null,如果相同，说明引用同一个对象，返回true
		 */
		if(string==string2){
			return true;
		}
		
		/**
		 * 这种情况下，他俩不会同时为空，所以一个为null,必然不会相同，返回false
		 */
		if(string ==null||string2==null){
			return false;
		}
		
		/**
		 * 下面的情况，肯定必然不能为null,如果长度不同，必然 不相等，
		 * 如果长度相同，那么如果其中一个不相同，则必然不相同
		 */
		if(string.length!=string2.length){
			return false;
		}else{
			for(int i = 0;i<string.length;i++){
				if(!string[i].equals(string2[i])){
					return false;
				}
			}
			return true;
		}
		
		
		
	}

	
	/**
	 * 首字母大写
	 */
	public static String upperCaseFirst(String s) {
		// 偏移首字母的ASCII码 实现首字母大写
		char[] cs = s.toCharArray();
		if(cs[0]<='Z'&&cs[0]>='A')
		{
			return s;
		}
		cs[0] -= 32;
		return String.valueOf(cs);
	}
	
	public static String lowerCaseFirst(String s) {
		// 偏移首字母的ASCII码 实现首字母大写
		char[] cs = s.toCharArray();
		if(cs[0]<='z'&&cs[0]>='a')
		{
			return s;
		}
		cs[0] += 32;
		return String.valueOf(cs);
	}
	
	
}
