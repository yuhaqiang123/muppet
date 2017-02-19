package cn.bronzeware.muppet.util;

/**
 * Created by zhangtao on 17/2/19.
 */
public class Utils {

    public boolean empty(Object object){
        if(object == null){
            return true;
        }
        return false;
    }

    public boolean notEmpty(Object object){
        return !empty(object);
    }


    public boolean empty(String string){
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

    public boolean notEmpty(String string){
        return !notEmpty(string);
    }

}
