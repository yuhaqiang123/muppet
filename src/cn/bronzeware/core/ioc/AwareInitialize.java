package cn.bronzeware.core.ioc;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.Aware;
import cn.bronzeware.core.ioc.InitializeException;
import cn.bronzeware.util.reflect.ReflectUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class AwareInitialize {
    private static final String AWARE_NAME = "Aware";
    private ApplicationContext applicationContext;
    public AwareInitialize(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
    public void initialize(Object instance){
        Class clazz = instance.getClass();
        Class[] interfaces = clazz.getInterfaces();
        for(Class interfaceClazz :interfaces){
            if(Aware.class.isAssignableFrom(interfaceClazz)){
                String interfaceName = interfaceClazz.getName();
                String awareKey = interfaceName.substring(0,interfaceName.length() - AWARE_NAME.length());
                String methodName = "set" + awareKey;
                Method method = null;
                try{
                    method = clazz.getDeclaredMethod(methodName);
                }catch (NoSuchMethodException e){
                    throw new InitializeException("Initialize " + interfaceName + "  dependency, but did not find " + methodName + " method");
                }
                Parameter[] parameters = method.getParameters();
                Object[] params = new Object[parameters.length];
                for(int i = 0 ;i < parameters.length ;i++){
                    Parameter parameter = parameters[i];
                    Class paramClazz = parameter.getType();
                    try{
                        params[i] = applicationContext.getBean(paramClazz);
                    }catch (SuchBeanNotFoundException e){
                        throw new InitializeException("Initialize " + interfaceName + "  dependency, but did not find relevant parameters");
                    }
                }

                try{
                     method.invoke(instance,params);
                }catch (Exception e){
                    //应该不会出事吧？？？？
                    //TODO
                    return ;
                }
            }
        }
    }
}
