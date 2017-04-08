package cn.bronzeware.test;

public class PrintClass {

    public static void main(String[] args){

        new B();

    }

    PrintClass(String var) {

        System.out.println(var);

    }

}

 

class A {

    static PrintClass pClassA1 = new PrintClass("A. 静态成员的初始化1");

    static {

        System.out.println("A. 静态初始化块1");

    }

    static {

        System.out.println("A. 静态初始化块2");

    }

    static PrintClass pClassA2 = new PrintClass("A. 静态成员的初始化2");

 

    {

        System.out.println("A. 实例初始化块1");

    }

    PrintClass pClassA = new PrintClass("A. 实例成员的初始化");

    {

        System.out.println("A. 实例初始化块2");

    }

 

    public int Avar;

    public A() {

        System.out.println("A. 构造方法");

        doSomething();

    }

 

    protected static  void doSomething() {

//    public void doSomething() {

       
    }

}

 

//class B extends A

class B extends A

{

    public static void main(String[] args){

        new B();
    }

    public int Bvar = 2222;

 

    static {
    	B.doSomething();
        System.out.println("B. 实例初始化块1");

    }

    {

        System.out.println("B. 实例初始化块2");

    }

    PrintClass pClassB = new PrintClass("B. 实例成员的初始化");

 

    static {

        System.out.println("B. 静态初始化块1");

    }

    static PrintClass pClassB1 = new PrintClass("B. 静态成员的初始化1");

    static PrintClass pClassB2 = new PrintClass("B. 静态成员的初始化2");

    static {

        System.out.println("B. 静态初始化块2");

    }

 

    public B() {

        System.out.println("B. 构造方法");

        doSomething();

    }

    public static void doSomething() {

        //System.out.println("Bvar=" + Bvar);

    }

}