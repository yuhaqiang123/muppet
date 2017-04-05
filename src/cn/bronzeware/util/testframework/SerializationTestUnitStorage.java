package cn.bronzeware.util.testframework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import cn.bronzeware.muppet.util.FileUtil;

public class SerializationTestUnitStorage implements TestUnitStorage{

	
	private String path;
	public SerializationTestUnitStorage(String path) {
		this.path = path;
	}
	
	
	@Override
	public Map<String, TestUnitMetaData> resolve() {
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fileInputStream);
			Map<String, TestUnitMetaData> map = (Map<String, TestUnitMetaData>) ois.readObject();
			return map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Object store(Map<String, TestUnitMetaData> testUnits) {
		File file = new File(path);
		FileUtil.createFile(file);
		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream);
			oos.writeObject(testUnits);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
