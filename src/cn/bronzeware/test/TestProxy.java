package cn.bronzeware.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import cn.bronzeware.util.reflect.ReflectUtil;

public class TestProxy {

	public class Invo1 implements InvocationHandler{

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			System.out.println("before"+proxy.getClass().getName()+
					"methodName:"+method.getName()+args[0]);
			
			Object result = method.invoke(proxy, args);
			System.out.println("after");
			return result;
		}
		
	}
	
	
	public static void main(String[] args) {
		
		
		final String str = "";
		final String str2 = "";
		System.out.println(Runtime.getRuntime().totalMemory()/(1000*1000));
		System.out.println(Runtime.getRuntime().freeMemory()/(1000*1000));
		System.out.println(Runtime.getRuntime().maxMemory()/(1000*1000));
		final List list = new ArrayList<>();
		final Semaphore avail = new Semaphore(2);
		
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
						try {
							avail.acquire();
						} catch (InterruptedException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						for(int i = 0;i<500000;i++){
							list.add(ReflectUtil.getClassProxy(TestProxy.class));
							
						}
						
					System.out.println(Thread.currentThread().getName());
					avail.release();
				}
			}).start();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						avail.acquire();
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					for(int i = 0;i<5000000;i++){
						list.add(ReflectUtil.getClassProxy(TestProxy.class));
						
					}
					System.out.println(Thread.currentThread().getName());
					avail.release();
				}
			}).start();
			
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			try {
				avail.acquire();
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		
		System.out.println(Runtime.getRuntime().totalMemory()/(1000*1000));
		System.out.println(Runtime.getRuntime().freeMemory()/(1000*1000));
		System.out.println(Runtime.getRuntime().maxMemory()/(1000*1000));
	}
}
