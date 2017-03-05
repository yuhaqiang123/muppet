package cn.bronzeware.util.reflect;

import cn.bronzeware.muppet.test.TestReflect;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;
import java.util.List;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class TestReflectUtil {
    public static void testGetClasses(){
        List list = ReflectUtil.getClasses("cn.bronzeware");
        ArrayUtil.println(list);
    }

    public static void main(String[] args){
        TestReflectUtil.testGetClasses();
    }
}
