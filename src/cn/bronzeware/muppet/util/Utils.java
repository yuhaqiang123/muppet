package cn.bronzeware.muppet.util;

import java.io.File;

/**
 * Created by zhangtao on 17/2/19.
 */
public class Utils {

    public static boolean empty(Object object){
        if(object == null){
            return true;
        }
        return false;
    }

    public static  boolean notEmpty(Object object){
        return !empty(object);
    }


    public static boolean empty(String string){
        if(empty(string) == true){
            return true;
        }else{
            if(string.equals("") == true){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public static boolean notEmpty(String string){
        return !notEmpty(string);
    }
    
    
    public static String pkgNameToDirName(String pkgName){
    	
    	return pkgName.replace(".", File.separator);
    }

}
