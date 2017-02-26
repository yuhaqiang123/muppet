package cn.bronzeware.core.ioc;



/**
 * 任何实现Capable接口或Capable子接口的类都视为向ApplicationContext声明自己可以提供某一种环境<br/>
 * ＡpplicationContext会调用相关方法，例如 <br/>
 * {@code interface XXXCapable extends Capable }<br/>
 * {@code class A implements XXXCapable } <br/>
 * 那么A类必须提供getXXX方法，向外界提供该环境　<br/>
 * 
 * 
 * @since 1.5
 * @author yuhaiqiang
 * 2017年2月26日上午11:36:19
 */
public interface Capable {
}
