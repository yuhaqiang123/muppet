package cn.bronzeware.core.ioc;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.Capable;
import cn.bronzeware.core.ioc.InitializeException;
import cn.bronzeware.core.ioc.SuchBeanNotFoundException;
import cn.bronzeware.util.reflect.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class CapableInitialize {


    private static final String CAPABLE_NAME = "Capable";
    private ApplicationContext context;
    public CapableInitialize( ApplicationContext context){
        this.context = context;
    }
    public void initialize(Object instance ){
        Class clazz = instance.getClass();
        Class[] interfaces = clazz.getInterfaces();
        for(Class interfaceClazz:interfaces){
            if(Capable.class.isAssignableFrom(interfaceClazz)){
                String interfaceName = interfaceClazz.getSimpleName();
                String capableKey = interfaceName.substring(0,interfaceName.length() - CAPABLE_NAME.length());
                String simpleMethodName = "get" + capableKey;
                String methodName= simpleMethodName;
                Method[] methods = null;

                methods = ReflectUtil.getMethods(clazz, simpleMethodName);
                if(Objects.isNull(methods) || methods.length == 0) {
                    throw new InitializeException("Initialize " + interfaceName + "  dependency, but did not find " + simpleMethodName + " method");
                }

                for(Method method:methods) {
                    Parameter[] parameters = method.getParameters();
                    if (Objects.nonNull(parameters) && parameters.length != 0) {
                        throw new InitializeException("Initialize " + interfaceName + "  dependency, but did not find " + methodName + " method with none parameter");
                    }

                    try {
                        Object result = method.invoke(instance, null);
                        context.registerBean(result);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new BeanInitializationException(
                                String.format("Initialize %s  dependency, but an internal exception occured in method :%s "
                                        , interfaceName, methodName));
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        throw new BeanInitializationException(
                                String.format("Initialize %s  dependency, but an internal exception occured in method :%s "
                                        , interfaceName, methodName));
                    }
                }
            }
        }
    }
}
