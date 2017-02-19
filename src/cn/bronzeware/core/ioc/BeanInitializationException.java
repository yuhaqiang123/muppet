package cn.bronzeware.core.ioc;

/**
 * Created by zhangtao on 17/2/19.
 */
public class BeanInitializationException extends InitializeException{

    private String msg="";
    public BeanInitializationException(String msg){
        super(msg);
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return this.msg;
    }
}
