package com.linyuanbaobao.amqp.listener.decorated;

import org.springframework.core.Ordered;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

/**
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
public interface InvocableHandlerMethodDecorator extends Ordered {
    InvocableHandlerMethod decorate(InvocableHandlerMethod invocableHandlerMethod);

    @Override
    default int getOrder() {
        return 0;
    }
}
