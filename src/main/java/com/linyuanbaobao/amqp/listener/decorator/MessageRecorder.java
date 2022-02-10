package com.linyuanbaobao.amqp.listener.decorator;

import org.springframework.messaging.Message;

/**
 * 消息记录接口
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
public interface MessageRecorder {
    void record(Message<?> message, boolean success, Object result, Exception error);

    class Dummy implements MessageRecorder {
        @Override
        public void record(Message<?> message, boolean success, Object result, Exception error) {
            // do nothing
        }
    }
}
