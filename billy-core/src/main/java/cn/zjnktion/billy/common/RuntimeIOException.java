package cn.zjnktion.billy.common;

/**
 * Created by zhengjn on 2016/4/7.
 */
public class RuntimeIOException extends RuntimeException {

    public RuntimeIOException() {

    }

    public RuntimeIOException(String message) {
        super(message);
    }

    public RuntimeIOException(Throwable cause) {
        super(cause);
    }

    public RuntimeIOException(String message, Throwable cause) {
        super(message, cause);
    }

}
