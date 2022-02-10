package com.linyuanbaobao.amqp.listener.decorator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;

/**
 * 抽象的幂等校验器
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
@Slf4j
public abstract class AbstractIdempotentValidator implements IdempotentValidator {
    private boolean skipIfMessageIdNull = true;

    public AbstractIdempotentValidator(boolean skipIfMessageIdNull) {
        this.skipIfMessageIdNull = skipIfMessageIdNull;
    }

    @Override
    public boolean isRepeated(Message<?> message) {
        Object id = message.getHeaders().get(AmqpHeaders.MESSAGE_ID);
        if (id == null && skipIfMessageIdNull) {
            return false;
        }
        boolean result = doValid(id);
        if (result) {
            log.warn("message[id: {}] is consumed repeatedly.", id);
        }
        return result;
    }

    protected abstract boolean doValid(Object id);
}
