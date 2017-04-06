package cn.bronzeware.test;

import cn.bronzeware.muppet.entities.Note;

public class A extends TestHashMap1 /*implements Interface1*/{
	public String a;

	/**
	 * 实例化一个对象
	 */
	public <T> T newObject(T t) throws InstantiationException, IllegalAccessException{
		T f = (T) t.getClass().newInstance();
		return f;
	}
	
	public String toString(){
		return "A";
	}
	
	/**
	 * 实例化对象，但是你获取到这个对象后，还得强转
	 * @param o
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public  Object newObject2(Object o) throws InstantiationException, IllegalAccessException{
		Object object = o.getClass().newInstance();
		return object;
	}
	
	public static void main(String [] args) {
		A a =new A();
		Note note = new Note();
		try {
			Note note1 = a.newObject(note);//这个不用强转
			Note note2 = (Note) a.newObject2(note);//必须强转
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	//@Override
	public String Interface1(String name) {
		return "Interface1";
	}
}
class B{
	
}