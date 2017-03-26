package cn.bronzeware.core.ioc;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class StandardApplicationConfig implements ApplicationConfig{


    private Map<String,Object> props = new HashMap<String, Object>();


    public StandardApplicationConfig(){
    	
        props.put(AUTO_SCAN_PACKAGE_KEY, "cn.bronzeware.core.ioc.test1");
        //props.put("", arg1)
    }
    public Object getProperty(String key){
        if(props.containsKey(key)){
            return props.get(key);
        }else{
            return null;
        }
    }
}
