package cn.bronzeware.test1;

class A{
	private final void t(){
		System.out.println(this.getClass().getName());
	}
	
	public void t1(){
		t();
	}
}

public class testpricatethis extends A{
	
   public void t2(){
	   t1();
   }
	
	public static void main(String[] args)
	{
		testpricatethis t = new testpricatethis();
		t.t1();
	}
}
