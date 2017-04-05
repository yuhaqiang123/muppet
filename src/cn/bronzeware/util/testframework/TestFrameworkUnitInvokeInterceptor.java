package cn.bronzeware.util.testframework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import cn.bronzeware.core.ioc.ApplicationContext;
import cn.bronzeware.core.ioc.annotation.After;
import cn.bronzeware.core.ioc.annotation.Around;
import cn.bronzeware.core.ioc.annotation.Before;
import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.core.ioc.annotation.Interceptor;
import cn.bronzeware.core.ioc.aop.PointCut;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.FileUtil;
import cn.bronzeware.muppet.util.IOUtil;
import cn.bronzeware.muppet.util.StringUtil;
import cn.bronzeware.muppet.util.Utils;
import cn.bronzeware.util.reflect.ReflectUtil;
import net.sf.cglib.proxy.MethodProxy;

@Interceptor
@Component
public class TestFrameworkUnitInvokeInterceptor {
	
	private TestFrameworkApplicationContext testFrameworkApplicationContext;
	private Map<String, TestUnitMetaData> testUnits = null;
	
	private TestFramework testFramework = null;
	public TestFrameworkUnitInvokeInterceptor(TestFrameworkApplicationContext testFrameworkApplicationContext){
		this.testFrameworkApplicationContext = testFrameworkApplicationContext;
	}
	
	public void getTestUnits(){
		if(testFramework == null){
			this.testFramework = testFrameworkApplicationContext.getBean(TestFramework.class);
		}
		this.testUnits = testFramework.getTestUnits();
	}
	
	PrintStream out = System.out;
	PrintStream err = System.err;
	private final String prefix = String.format("bin/test/%s/"
			, new SimpleDateFormat("yyyy-MM-dd",Locale.SIMPLIFIED_CHINESE).format(new Date(System.currentTimeMillis())));
	private final String postfix = String.format("%d", System.currentTimeMillis());
	@Before(annotation=Test.class)
	public void beforeTestUnitInvoke(PointCut pointCut){
		File outFile = new File(String.format("%s/out.%s.%s", prefix
				, ReflectUtil.getMethodFullName(pointCut.getTargetMethod())
				, postfix));
		FileUtil.createFile(outFile);
		
		File errFile = new File(String.format("%s/err.%s.%s", prefix
				,ReflectUtil.getMethodFullName(pointCut.getTargetMethod())
				, postfix));
		FileUtil.createFile(errFile);
		
		try {
			System.setOut(new PrintStream(new FileOutputStream(outFile, true)));
			System.setErr(new PrintStream(new FileOutputStream(errFile, true)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@After(annotation=Test.class)
	public void afterTestUnitInvoke(PointCut pointCut){
		System.setOut(out);
		System.setErr(err);
		File outFile = new File(String.format("%s/out.%s.%s"
				, prefix
				, ReflectUtil.getMethodFullName(pointCut.getTargetMethod())
				, postfix));
		File errFile = new File(String.format("%s/err.%s.%s", prefix
				, ReflectUtil.getMethodFullName(pointCut.getTargetMethod())
				, postfix));
		//System.out.println("你好："+pointCut.getReturnValue());
		getTestUnits();
		TestUnitMetaData testUnitMetaData = testUnits.get(ReflectUtil.getMethodFullName(pointCut.getTargetMethod()));
		//将输出放进map中
		testUnitMetaData.setReturnValue(pointCut.getReturnValue());
		testUnitMetaData.setCmdOutput(IOUtil.getContent(outFile));
		testUnitMetaData.setCmdErr(IOUtil.getContent(errFile));
		//System.out.println(StringUtil.string(FileUtil.read(file.getAbsolutePath()), "UTF-8"));
	}
	
	@Around(annotation=Test.class)
	public Object aroundTestUnitInvoke(PointCut pointCut){
		try {
			Object result = pointCut.invoke();
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
