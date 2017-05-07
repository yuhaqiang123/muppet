package cn.bronzeware.muppet.util.autogenerate;

public class Field {

	private String p = Permission.PRIVATE;// 标记域的访问权限 默认为私有
	private boolean isStatic = false;// 是否为静态域 默认为false
	private boolean isFinal = false;// 是否为私有域 默认为false
	private String type;// 域类型
	private String fieldName;// 域的名称
	private boolean needGetAndSet = false;//是否提供get set方法 默认关闭此功能  

	public Field(String type, String fieldName) {
		super();
		this.type = type;
		this.fieldName = fieldName;
	}

	public boolean isNeedGetAndSet() {
		return needGetAndSet;
	}

	public void setNeedGetAndSet(boolean needGetAndSet) {
		this.needGetAndSet = needGetAndSet;
	}

	public Field(String p, boolean isStatic, boolean isFinal, String type, String fieldName) {
		super();
		this.p = p;
		this.isStatic = isStatic;
		this.isFinal = isFinal;
		this.type = type;
		this.fieldName = fieldName;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	//重写equals方法和hashCode方法
	//用于判断两个两个对象是否相等	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return this.hashCode()==(((Field) obj).hashCode());
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getFieldName().hashCode();
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
		sb.append(this.type).append(" ").append(this.fieldName).append(";");
		return sb.toString();
	}

}
