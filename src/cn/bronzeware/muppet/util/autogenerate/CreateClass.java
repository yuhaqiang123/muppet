package cn.bronzeware.muppet.util.autogenerate;

/**
 * 创建类的工具类
 * @author thomas.xu
 * 2016.08.30
 * @see ICreateClass
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateClass implements ICreateClass {

	private Set<Field> fields = null;
	private Set<Method> methods = null;
	private String className;
	private String packName;
	private String fileLoc;

	public CreateClass(String className, String packName) {
		super();
		this.className = className;
		this.packName = packName;
	}

	public void create() {
		String pName = "src/" + this.packName.replace(".", "/");
		createPackage(pName);
		fileLoc = pName + "/" + this.className + ".java";
		createClass(fileLoc);

		writeClass();

	};

	public boolean addField(Field field) {
		if (fields == null) {
			fields = new HashSet<>();
		}
		if (field.isNeedGetAndSet()) {
			addGetAndSetMothod(field);
		}
		return fields.add(field);
	};

	public boolean addMethod(Method method) {
		if (methods == null) {
			methods = new HashSet<>();
		}
		return methods.add(method);
	};

	// 根据属性提供get和set方法
	private void addGetAndSetMothod(Field field) {
		String name = upperCaseFirst(field.getFieldName().toLowerCase());
		List<String> params = new ArrayList<>();

		params.add(field.getType());
		String setMethodBody = "this." + field.getFieldName() + "=obj1;";
		Method setMethod = new Method(Permission.PUBLIC, false, false, "void", "set" + name, params, setMethodBody);

		String getMethodBody = "return this." + field.getFieldName() + ";";
		Method getMethod = new Method(Permission.PUBLIC, false, false, field.getType(), "get" + name, null,
				getMethodBody);
		if (methods == null) {
			methods = new HashSet<>();
		}
		this.methods.add(getMethod);
		this.methods.add(setMethod);
	};

	private String upperCaseFirst(String s) {
		// 偏移首字母的ASCII码 实现首字母大写
		char[] cs = s.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);
	}

	private void createPackage(String loc) {
		File f = new File(loc);
		// 包文件夹是否存在，不存在则创建
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	private void createClass(String loc) {
		File f = new File(loc);
		// 包文件夹是否存在，不存在则创建
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void writeClass() {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(fileLoc, true));
			// 写入包名
			bw.append("package ").append(packName).append(";\n\n");
			bw.append("public class ").append(this.className).append("{\n\n");
			for (Field f : fields) {
				bw.append(f.toString());
				bw.newLine();
			}
			bw.newLine();
			if (methods != null) {
				for (Method m : methods) {
					bw.append(m.toString());
					bw.newLine();
				}
			}
			bw.newLine();

			bw.append("}");

			bw.flush();
			bw.close();

		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}
