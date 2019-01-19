package com.wq.concurrency.active.framework;

/**
 * @Author: wangqiang20995
 * @Date:2019/1/19
 * @Description:
 * @Resource:
 */
public class FrameException extends RuntimeException{

    private String code;

    private String cause;

    private Throwable throwable;

    public static final String BASE_ERROR = "base_error";

    public static final String SERVICE_ERROR = "service_error";

    public FrameException(String code,String cause,Throwable e){
        super(String.format("code->[%s],message->[%s]",code,cause),e);
    }

    public FrameException(String code,Throwable e){
        this(code,e.getMessage(),e);
    }

    public FrameException(Throwable e){
        this(BASE_ERROR,e.getMessage(),e);
    }

    public FrameException(String message){
        this(BASE_ERROR,message);
    }

    public FrameException(String code,String message){
        super(String.format("code->[%s],message->[%s]",code,message));
    }
}
