package com.security.gateway.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogSupport {
    static Logger logger = LoggerFactory.getLogger(LogSupport.class);
    public static void logError(RuntimeException ex) {
        StackTraceElement stackTraceElement = ex.getStackTrace()[0];
        logger.error(ex.getMessage() + " File name: " + stackTraceElement.getFileName()
                + " line: " + stackTraceElement.getLineNumber());
    }

}
