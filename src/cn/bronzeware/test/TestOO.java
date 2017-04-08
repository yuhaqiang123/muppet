package cn.bronzeware.test;

public class TestOO {

	public static void main(String[] args){
		BB aa = new BB();
		//aa.printA();
	}
}

class AA{
	
	public String a = "1";
	public AA(){
		this.test();
	}
	
	public void printA(){
		System.out.println(a);
		test();
	}
	
	private  void test(){
		printA();
	}
}

class BB extends AA{
	
	public String a = "2";
	
	
	private void test(){
		System.out.println("BB");
	}
	
	public void printA(){
		System.out.println("BB");
	}
	public BB(){
		
	}
	
}