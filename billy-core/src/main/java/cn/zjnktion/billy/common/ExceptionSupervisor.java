package cn.zjnktion.billy.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhengjn on 2016/4/8.
 */
public class ExceptionSupervisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionSupervisor.class);

    private static final ExceptionSupervisor EXCEPTION_SUPERVISOR = new ExceptionSupervisor();

    private ExceptionSupervisor() {

    }

    public static ExceptionSupervisor getInstance() {
        return EXCEPTION_SUPERVISOR;
    }

    public void exceptionCaught(Throwable cause) {
        if (cause instanceof Error) {
            throw (Error) cause;
        }

        LOGGER.warn("Unexpected exception.", cause);
    }

}
