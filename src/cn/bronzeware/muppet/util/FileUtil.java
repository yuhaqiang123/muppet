package cn.bronzeware.muppet.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class FileUtil {

	public static String getClassPath(){
		return Thread.class.getResource(File.separator).getPath();
	}
	
	public static void write(byte[] data,String path){
		try {
			FileOutputStream out = new FileOutputStream(new File(path));
			out.write(data);
			out.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public static byte[] read(String path){
		FileInputStream in;
		try {
			in = new FileInputStream(new File(path));
			byte[] result = new byte[in.available()];
			int s = 0;
			byte[] buffer = new byte[1024];
			int num = -1;
			while((num = in.read(buffer))!=-1){
				System.arraycopy(buffer, 0, result, s, num);
				s+=num;
			}
			return result;
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		byte[]  result = FileUtil.read("F://lyyb474871b-a925-4a72-8821-79c8b78fbc85.html");
		System.out.println(new String(result,"UTF-8"));
	}
	
	
}
