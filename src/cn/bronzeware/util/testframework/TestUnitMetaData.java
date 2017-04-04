package cn.bronzeware.util.testframework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestUnitMetaData {



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

	private Object returnValue;
	
	private Class returnType;
	
	private Class targetClass;
	
	private Object targetObject;
	
	private Object[] targetParams;
	
	private Method targetMethod;
	
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
		this.targetMethod = targetMethod;
	}

	public Annotation[] getAnnotationTypes() {
		return annotationTypes;
	}

	public void setAnnotationTypes(Annotation[] annotationTypes) {
		this.annotationTypes = annotationTypes;
	}
}
