package cn.bronzeware.core.ioc;

/**
 * Created by yuhaiqiang on 17/2/12.
 */
public class InitializeException extends RuntimeException{

    private String msg = "";
    public InitializeException(String msg){
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
