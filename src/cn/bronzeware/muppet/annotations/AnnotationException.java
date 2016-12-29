package cn.bronzeware.muppet.annotations;
abstract public class AnnotationException extends Exception{

	
	private static final long serialVersionUID = -2982981926558813398L;



	public static void main(String[] args) {

	}

	
	
	abstract public String Message();



	@Override
  	public final String getMessage() {
		
		return "注解出错->"+ Message();
	}

}
