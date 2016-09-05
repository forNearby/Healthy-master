package android.microanswer.healthy.exception;

/**
 * Created by Micro on 2016/6/23.
 */

public class JavaBeanDataLoadException extends Exception {
    /**
     * 访问出错的网址
     */
    public String errorUrl;

    /**
     * 错误详细内容
     */
    public Throwable e;

    /**
     * 错误简介
     */
    public String description;

    public JavaBeanDataLoadException(Throwable throwable, String errorUrl, String description) {
        super(throwable);
        this.errorUrl = errorUrl;
        this.e = throwable;
        this.description = description;
    }
}
