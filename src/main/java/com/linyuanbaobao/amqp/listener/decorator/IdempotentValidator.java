package com.linyuanbaobao.amqp.listener.decorator;

import org.springframework.messaging.Message;

/**
 * 幂等校验器接口
 *
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
public interface IdempotentValidator {
    boolean isRepeated(Message<?> message);

    class Dummy implements IdempotentValidator {
        @Override
        public boolean isRepeated(Message<?> message) {
            // do nothing
            return false;
        }
    }
}
