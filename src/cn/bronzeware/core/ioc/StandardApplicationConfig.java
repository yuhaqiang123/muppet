package cn.bronzeware.core.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class StandardApplicationConfig implements ApplicationConfig{


    private Map<String,Object> props = new HashMap<String, Object>();


    public StandardApplicationConfig(){
    	
        props.put(AUTO_SCAN_PACKAGE_KEY, new String[]{"cn.bronzeware.core.ioc.test"});
        //props.put("", arg1)
    }
    public Object getProperty(String key){
        if(props.containsKey(key)){
            return props.get(key);
        }else{
            return null;
        }
    }
    
    public Object setProperty(String key, Object value){
    	 if(props.containsKey(key)){
    		 Object object = props.get(key);
    		 props.put(key, value);
             return object;
         }else{
        	 props.put(key, value);
             return null;
         }
    	
    }
}
