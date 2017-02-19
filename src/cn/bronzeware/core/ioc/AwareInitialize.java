package cn.bronzeware.core.ioc;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.Aware;
import cn.bronzeware.core.ioc.InitializeException;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Log;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.reflect.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class AwareInitialize {
    /**
     * Aware interface's Name
     */
    private static final String AWARE_NAME = "Aware";

    /**
     * 应用上下文
     */
    private ApplicationContext applicationContext;

    public AwareInitialize(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }


    /**
     * @description 获取instance所实现的接口，如果实现Aware接口，那么需要识别他需要哪些依赖
     *
     * @param instance
     */
    public void initialize(Object instance){
        Class clazz = instance.getClass();
        Class[] interfaces = clazz.getInterfaces();
        for(Class interfaceClazz :interfaces){
            if(Aware.class.isAssignableFrom(interfaceClazz)){
                String interfaceName = interfaceClazz.getSimpleName();
                String awareKey = interfaceName.substring(0,interfaceName.length() - AWARE_NAME.length());
                //调用setAwareKey方法
                String simpleMethodName = "set" + awareKey;
                //String methodName = String.format("%s.%s",clazz.getName() , simpleMethodName);
                String methodName= simpleMethodName;
                Method[] methods = null;

                methods = ReflectUtil.getMethods(clazz, simpleMethodName);
                if(Objects.isNull(methods) || methods.length == 0) {
                    throw new InitializeException("Initialize " + interfaceName + "  dependency, but did not find " + simpleMethodName + " method");
                }

                for(Method method:methods){
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
                    }catch (IllegalAccessException e ){
                        e.printStackTrace();
                        throw new BeanInitializationException(
                                String.format("Initialize %s  dependency, but an internal exception occured in method :%s "
                                , interfaceName, methodName));
                    }
                    catch (InvocationTargetException e2){
                        e2.printStackTrace();
                        throw new BeanInitializationException(
                                String.format("Initialize %s  dependency, but an internal exception occured in method :%s "
                                        , interfaceName, methodName));
                    }
                }
            }
        }
    }
}
