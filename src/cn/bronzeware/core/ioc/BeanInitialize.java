package cn.bronzeware.core.ioc;

import java.util.List;

public interface BeanInitialize {


    public <T> T initializeBean(Class<T> clazz);

    public List<Object> initializeBeans(List<Class<?>> clazzList);
}
