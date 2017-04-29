package cn.bronzeware.util.testframework;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.Unmodifiable;

import cn.bronzeware.muppet.util.FileUtil;
import cn.bronzeware.muppet.util.TimeUtil;
import wiki.diff2.Diff;
import wiki.diff2.Difference;

public class HtmlTestUnitStorage {

	private String path;
	public HtmlTestUnitStorage(String path){
		this.path = path;
	}
	
	
	
	public Map<String, File> initialize(Map<String, Map<String, TestUnitMetaData>> unitMaps,Map<String, TestUnitMetaData> unit){
		File file = new File(path);
		FileUtil.createFile(file, true);
		File[] files = file.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		});
		if(files != null){
			for(File f:files){
				f.delete();
			}
		}
		
		Map<String, File> fileMap = new TreeMap<>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if(Long.parseLong(o1) < Long.parseLong(o2)){
					return 1;
				}else{
					return -1;
				}
			}
			
		});
		int i = 0;
		for(Map.Entry<String, Map<String, TestUnitMetaData>> entry : unitMaps.entrySet()){
			Map<String, TestUnitMetaData> map = entry.getValue();
			String time = entry.getKey();
			fileMap.put(time, initializeHtml(map, unit, time));
		}
		String time = String.valueOf(System.currentTimeMillis());
		fileMap.put(time, initializeHtml(unit, unit, time));
		initializeIndex(fileMap);
		
		return fileMap;
	}
	
	public File initializeIndex(Map<String, File> fileMap){
		PrintStream ps  = null;
		File file = null;
		try {
			file = new File(String.format("%s/index.html", path));
			FileUtil.createFile(file);
			FileOutputStream fos = new FileOutputStream(file);
			ps = new PrintStream(fos);
		} catch (FileNotFoundException e) {
			
		}
		
		ps.println("<html>");
		ps.println("<meta charset='utf-8'/>");
		ps.println("<body>");
		long curr = System.currentTimeMillis();
		ps.println(String.format("<h2>本次测试时间：%s</h2>", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(curr))));
		for(Map.Entry<String, File> entry:fileMap.entrySet()){
			
			long old = Long.parseLong(entry.getKey());
			ps.println(String.format("<a href='%s'>与%s : %s</a>", entry.getValue().getName()
					, TimeUtil.interval(old, curr)+"之前的结果比对　"
					, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(old))));
			ps.println("<br/><br/><br/>");
		}
		ps.println("</body>");
		ps.println("</html>");
		ps.close();
		return file;
	}
	
	public File initializeHtml(Map<String, TestUnitMetaData> map, Map<String, TestUnitMetaData> unit, String time){
		
		PrintStream ps  = null;
		File file = null;
		try {
			file = new File(String.format("%s/%s.html", path,
					new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")
					.format(new Date(Long.parseLong(time)))));
			FileUtil.createFile(file);
			FileOutputStream fos = new FileOutputStream(file);
			ps = new PrintStream(fos);
		} catch (FileNotFoundException e) {
			
		}
		
		ps.println("<html>");
		ps.println("<meta charset='utf-8'/>");
		ps.println("<body>");
		List<TestUnitMetaData> untracked = new ArrayList<TestUnitMetaData>();
		List<TestUnitMetaData> notModified = new ArrayList<>();
		List<TestUnitMetaData> modified = new ArrayList<>();
		List<TestUnitMetaData> deleted = new ArrayList<>();
		for(Map.Entry<String, TestUnitMetaData> entry:unit.entrySet()){
			String key = entry.getKey();
			TestUnitMetaData metaData = entry.getValue();
			if(! map.containsKey(key)){
				untracked.add(metaData);
				continue;
			}
			
			if(map.containsKey(key)){
				TestUnitMetaData m = map.get(key);
				if(m.equal(metaData)){
					notModified.add(metaData);
				}else{
					modified.add(metaData);
					modified.add(m);
				}
			}
			
		}
		
		for(Map.Entry<String, TestUnitMetaData> entry:map.entrySet()){
			String key = entry.getKey();
			TestUnitMetaData value = entry.getValue();
			if(!unit.containsKey(key)){
				deleted.add(value);
			}
		}
		
		ps.println("<h2>untracked</h2>");
		for(TestUnitMetaData m:untracked){
			initializePs(ps, m);
		}
		ps.println("<br/><br/><br/>");
		
		ps.println("<h2>deleted</h2>");
		for(TestUnitMetaData m:deleted){
			initializePs(ps, m);
		}
		ps.println("<br/><br/><br/>");
		
		ps.println("<h2>modified</h2>");
		for(int i = 0;i < modified.size();i +=2){
			initializeModifiedPs(ps, modified.get(i), modified.get(i+1));
			ps.println("<br/>");
		}
		ps.println("<br/><br/><br/>");
		
		ps.println("<h2>unmodified</h2>");
		for(TestUnitMetaData m:notModified){
			initializePs(ps, m);
		}
		ps.println("<br/><br/><br/>");
		
		ps.println("</body>");
		ps.println("</html>");
		ps.close();
		return file;
	}
	
	private void initializeModifiedPs(PrintStream ps, TestUnitMetaData curr, TestUnitMetaData old){
		ps.println("<table border=1>");
		ps.println("<tr>");
		ps.println(String.format("<td> className: %s </td>", curr.getTargetClass().getName()));
		ps.println(String.format("<td> methodName : %s</td>", curr.getMethodName()));
		ps.println("</tr>");
		ps.println("<br/>");
		

		ps.println("<tr>");
		if(TestUnitMetaData.equal(curr.getReturnValue(), old.getReturnValue())){
			ps.println("<td><h2>returnValue</h2></td>");
			ps.println(String.format("<td>%s</td>", curr.getReturnValue()));
		}else{
			ps.println("<td><h2>ReturnValue New</h2></td>");
			ps.println(String.format("<td><font color='red'>%s</font></td>", curr.getReturnValue()));
			ps.println("</tr><tr>");
			ps.println("<td>ReturnValue Old</td>");
			ps.println(String.format("<td><font color='red'>%s</font></td>", old.getReturnValue()));
		}
		ps.println("</tr>");
		
		ps.println("<tr>");
		if(TestUnitMetaData.equal(curr.getCmdErr(), old.getCmdErr())){
			ps.println("<td><h2>CmdErr</h2></td>");
			ps.println(String.format("<td>%s</td>", replaceBackSpace2Html(curr.getCmdErr().replace("\n", "<br/>"))));
		}else{
			String cmdErr = diffLineShow(old.getCmdErr(), curr.getCmdErr());
			ps.println("<td><h2>CmdErr</h2></td>");
			ps.println(String.format("<td>%s</td>", replaceBackSpace2Html(cmdErr.replace("\n", "<br/>"))));
			ps.println("</tr><tr>");
			
		}
		ps.println("</tr>");
		
		ps.println("<tr>");
		if(TestUnitMetaData.equal(curr.getCmdOutput(), old.getCmdOutput())){
			ps.println("<td><h2>CmdOut</h2></td>");
			ps.println(String.format("<td>%s</td>", replaceBackSpace2Html(curr.getCmdOutput().replace("\n", "<br/>"))));
		}else{
			String cmdOut = diffLineShow(old.getCmdOutput(), curr.getCmdOutput());
			ps.println("<td><h2>CmdOut</h2></td>");
			ps.println(String.format("<td>%s</td>", replaceBackSpace2Html(cmdOut.replace("\n", "<br/>"))));
		}
		ps.println("</tr>");
		
		
		ps.println("<br/>");
		
		ps.println("</table>");
	}
	
	
	private void initializePs(PrintStream ps, TestUnitMetaData m){
		ps.println("<table border=1>");
		ps.println("<tr>");
		ps.println(String.format("<td> className: %s </td>", m.getTargetClass().getName()));
		ps.println(String.format("<td> methodName : %s</td>", m.getMethodName()));
		ps.println("</tr>");
		ps.println("<br/>");
		
		ps.println("<tr>");
		ps.println("<td>returnValue</td>");
		ps.println(String.format("<td>%s</td>", m.getReturnValue()));
		ps.println("</tr>");
		ps.println("<br/>");
		
		ps.println("<tr>");
		ps.println(String.format("<td>CmdOut:</td>"));
		ps.println(String.format("<td>%s</td>", replaceBackSpace2Html(m.getCmdOutput().replace("\n", "<br/>"))));
		ps.println("</tr>");
		ps.println("<br/>");
		
		ps.println("<tr>");
		ps.println(String.format("<td>CmdErr:</td>"));
		ps.println(String.format("<td>%s</td>", replaceBackSpace2Html(m.getCmdErr().replace("\n", "<br/>"))));
		ps.println("</tr>");
		ps.println("<br/>");
		ps.println("</table>");
	}
	
	private String diffLineShow(String old, String curr){
		String[] oldLines = old.split("\n");
		String[] currLines = curr.split("\n");
		List<Difference> diffs  = (new Diff(oldLines, currLines)).diff();
		int i = 0;
		StringBuffer buffer = new StringBuffer();
		for(Difference diff: diffs){
			int addStart =diff.getAddedStart();
			int addEnd = diff.getAddedEnd();
			int delStart = diff.getDeletedStart();
			int delEnd = diff.getDeletedEnd();
			int a = i;
			int d = i;
			if(addEnd > -1){
				for(;i < addStart;i++){
					buffer.append(currLines[i]+ "\n");
				}
				a = i;
				for(; a <= addEnd;a++){
					buffer.append(String.format("+<b><font color='green'>%s</font>\n", currLines[a] ));
				}
			}
			
			if(delEnd > -1){
				for(; i < delStart;i++){
					buffer.append(oldLines[i] + "\n");
				}
				d = i;
				for(; d <= delEnd; d++){
					buffer.append(String.format("- <font color='red'>%s</font>\n", oldLines[d]));
				}
			}
			i = Math.max(addEnd, delEnd) + 1;
		}
		for(; i < currLines.length; i++){
			buffer.append(currLines[i] + "\n");
		}
		return buffer.toString();
	}
	
	private static String replaceBackSpace2Html(String string){
		return string.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").replace(" ", "&nbsp;&nbsp;");
	}
	
}
