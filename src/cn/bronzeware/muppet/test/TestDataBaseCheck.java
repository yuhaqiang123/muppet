package cn.bronzeware.muppet.test;

import cn.bronzeware.muppet.core.DataBaseCheck;

public class TestDataBaseCheck {

	public static void main(String[] args){
		DataBaseCheck check = new DataBaseCheck();
		DataBaseCheck.TableCheck tableCheck = check.createTableCheck("t_n1");
		DataBaseCheck.ColumnCheck columnCheck = check.createColumnCheck("t_test", "pasd");
		System.out.println(columnCheck.isExist());
		System.out.println(columnCheck.isPrimaryKey());
		System.out.println(columnCheck.isNullable());
		System.out.println(columnCheck.isUnique());
		System.out.println(columnCheck.isIndex());
		System.out.println(columnCheck.getDefaultValue());
		System.out.println(columnCheck.getLength());
		System.out.println(columnCheck.getSqlType());
		System.out.println(columnCheck.isForeignKey());
	}
}
