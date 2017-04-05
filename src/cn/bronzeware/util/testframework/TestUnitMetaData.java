package cn.bronzeware.util.testframework;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import cn.bronzeware.util.reflect.ReflectUtil;

public class TestUnitMetaData implements Serializable{



	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestUnitMetaData [returnValue=").append(returnValue).append(", returnType=").append(returnType)
				.append(", targetClass=").append(targetClass).append(", targetObject=").append(targetObject)
				.append(", targetParams=").append(Arrays.toString(targetParams)).append(", targetMethod=")
				.append(targetMethod).append(", annotationTypes=").append(Arrays.toString(annotationTypes))
				.append(", cmdOutput=").append(cmdOutput).append(", cmdErr=").append(cmdErr).append("]");
		return builder.toString();
	}

	public boolean equal(Object s1, Object s2){
		if(s1 == null && s2!=null){
			return false;
		}
		if(s2 == null && s1!=null){
			return false;
		}
		if(s1==s2){
			return true;
		}
		if(s1.toString().equals(s2.toString())){
			return true;
		}else{
			return false;
		}
		
	}
	
	public boolean equal(TestUnitMetaData metaData){
		if(!equal(metaData.getCmdErr(), this.getCmdErr())){
			//System.out.println("err:"+this.getCmdErr());
			return false;
		}
		
		if(!equal(metaData.getCmdOutput(), this.getCmdOutput())){
			//System.out.println("out:"+this.getCmdOutput());
			return false;
		}
		
		if(!equal(metaData.getMethodName(), this.getMethodName())){
			//System.out.println("method:"+this.getMethodName());
			return false;
		}
		
		if(!equal(metaData.getReturnValue(), this.getReturnValue())){
			if(metaData.getReturnValue().equals(this.getReturnValue())){
				System.out.println("haha");
			}
			return false;
		}
		return true;
		
	}
	
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	private Object returnValue;
	
	private Class returnType;
	
	private Class targetClass;
	
	private transient Object targetObject;
	
	private transient Object[] targetParams;
	
	private transient Method targetMethod;
	
	private String methodName ;
	
	private Annotation[] annotationTypes;

	private String cmdOutput;
	
	private String cmdErr;
	
	public String getCmdOutput() {
		return cmdOutput;
	}

	public void setCmdOutput(String cmdOutput) {
		this.cmdOutput = cmdOutput;
	}

	public String getCmdErr() {
		return cmdErr;
	}

	public void setCmdErr(String cmdErr) {
		this.cmdErr = cmdErr;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public Class getReturnType() {
		return returnType;
	}

	public void setReturnType(Class returnType) {
		this.returnType = returnType;
	}

	public Class getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(Class targetClass) {
		this.targetClass = targetClass;
	}

	public Object getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(Object targetObject) {
		this.targetObject = targetObject;
	}

	public Object[] getTargetParams() {
		return targetParams;
	}

	public void setTargetParams(Object[] targetParams) {
		this.targetParams = targetParams;
	}

	public Method getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(Method targetMethod) {
		this.methodName = ReflectUtil.getMethodFullName(targetMethod);
		this.targetMethod = targetMethod;
	}

	public Annotation[] getAnnotationTypes() {
		return annotationTypes;
	}

	public void setAnnotationTypes(Annotation[] annotationTypes) {
		this.annotationTypes = annotationTypes;
	}
}
