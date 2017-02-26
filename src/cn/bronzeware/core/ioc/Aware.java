package cn.bronzeware.core.ioc;

/**
 * Created by yuhaiqiang on 17/2/12.<br/>
 * <h2>任何实现Aware接口或者其子接口的类，都视为向{@link ApplicationContext} 表明，自己需要某种环境，某种依</h2>
 * <h2>赖，需要ApplicationContext提供给自己这种依赖，例如　</h2>
 * {@code 
 * public interface XXXAware extends Aware {
 * 
 * }   
 * public class A implements XXXAware｛
 * 
 * ｝
 * 
 * <br/>}
 * <br/>
 * <h3>
 * 那么这种情况下，A必须实现setXXX方法，具体方法参数不限，ApplicationContext会自动调用该方法，向该对象注入需要的依赖
 * </h3>
 */
public interface Aware {


}
