package cn.bronzeware.muppet.util;

import static java.lang.System.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.nio.charset.Charset;




public class IOUtil {
	
	public static File getClassPathFile(String path){
		File file = new File(String.format("bin%s%s", File.separator, path));
		return file;
	}
	
	public static String getContent(File file){
		try {
			InputStream in =  new FileInputStream(file);
			String content = "";
			byte[] buffer = new byte[1024];
			int length = in.read(buffer);
			Charset charset = Charset.forName("UTF-8");
			while(length != -1){
				content += new String(buffer,0, length, charset);
				length = in.read(buffer);
			}
			return content;
		} catch (FileNotFoundException  e) {
			throw ExceptionUtil.getRuntimeException(e);
		}catch (IOException e) {
			throw ExceptionUtil.getRuntimeException(e);
		}
	}
	
	public static File[] getFiles(String path,FileFilter filter){
		File file = new File(path);
		if(file.exists()){
			if(file.isDirectory()){
				File[] files = file.listFiles(filter);
				
				return files;
			}else{
				return new File[0];
			}
		}else{
			throw ExceptionUtil.getRuntimeException( String.format("%s 该文件或目录不存在", file.getPath()));
		}	
	}
	
	public static File[] getFilesWithOutDirs(String path){
		return getFiles(path, new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.isFile();
			}
		});
	}
	
	public static  File[] getDirs(String path){
		return getFiles(path, new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		});
	}
	
	public static File[] getFiles(String path){
		return getFiles(path, new FileFilter() {
			@Override
			public boolean accept(File arg0) {
				
				return true;
			}
		});
	}
	
	public static void main(String[] args){
		//
		//System.out.p
		//out.println(getContent(getClassPathFile("input.xml")));
		ArrayUtil.println(getFiles(String.format("bin/%s", "cn/bronzeware/muppet/util/log/Msg.class")));
		//PipedInputStream in = new PipedInputStream()；
		
	}
	
}
