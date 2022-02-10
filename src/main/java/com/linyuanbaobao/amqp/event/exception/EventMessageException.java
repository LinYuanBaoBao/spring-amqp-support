package com.linyuanbaobao.amqp.event.exception;

/**
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
public class EventMessageException extends RuntimeException {
    public EventMessageException(String message) {
        super(message);
    }

    public EventMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
