package cn.bronzeware.core.ioc;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public interface ApplicationConfig {
    public Object getProperty(String key);

    static final  String AUTO_SCAN_PACKAGE_KEY = "cn.bronzeware.muppet.core.ioc.auto_scan_package_key";
}
