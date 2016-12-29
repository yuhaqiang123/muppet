package cn.bronzeware.muppet.util.autogenerate;

import java.util.List;

public class Method {

	private String p = Permission.PUBLIC;// 标记域的访问权限 默认public
	private boolean isStatic = false;// 是否为静态域 默认为false
	private boolean isFinal = false;// 是否为私有域 默认为false
	private String returnType = "void";// 返回值类型
	private String methodName;// 方法名称
	private String methodBody = null;
	private List<String> params = null;
	
	public String getMethodBody() {
		return methodBody;
	}

	public void setMethodBody(String methodBody) {
		this.methodBody = methodBody;
	}

	public Method(String p, boolean isStatic, boolean isFinal, String returnType, String methodName,
			List<String> params, String methodBody) {
		super();
		this.p = p;
		this.isStatic = isStatic;
		this.isFinal = isFinal;
		this.returnType = returnType;
		this.methodName = methodName;
		this.params = params;
		this.methodBody = methodBody;
	}

	// 不提供无参构造方法，必须指定方法名称

	// 创建一个无方法体无返回值无返回列表的指定方法名的方法
	public Method(String methodName) {
		this.methodName = methodName;
	}

	public String getP() {
		return p;
	}

	public void setP(String p) {
		this.p = p;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();

		sb.append("\t");
		if (this.p != null) {
			sb.append(this.p).append(" ");
		}
		if (this.isStatic) {
			sb.append("static ");
		}
		if (this.isFinal) {
			sb.append("final ");
		}
		sb.append(this.returnType).append(" ");
		sb.append(this.methodName).append("(");
		if (params != null) {
			for (int i = 0; i < params.size(); i++) {
				sb.append(params.get(i)).append(" obj").append(i + 1);
				if (i != params.size() - 1) {
					sb.append(", ");
				}
			}
		}
		sb.append("){\n");
		sb.append("\t\t").append(methodBody).append("\n\t}");

		return sb.toString();
	}

	// 应当重写equals方法 防止在创建的类的时候出现重复的方法定义
	@Override
	public boolean equals(Object obj) {
		Method m = (Method) obj;
		return this.hashCode()==m.hashCode();
	}

	//重写hashCode方法  用于判断两个对象是否相等
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		String s = this.methodName + (this.params == null ? "null" : this.params.toString());
		return s.hashCode();
	}
}
