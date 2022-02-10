package com.linyuanbaobao.amqp.listener.decorated;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;

/**
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
public abstract class InvocableHandlerMethodDecoration extends InvocableHandlerMethod {
    private InvocableHandlerMethod delegate;

    public InvocableHandlerMethodDecoration(InvocableHandlerMethod delegate) {
        super(delegate.getBean(), delegate.getMethod());
        this.delegate = delegate;
    }

    @Override
    public Object invoke(Message<?> message, Object... providedArgs) throws Exception {
        return this.delegate.invoke(message, providedArgs);
    }
}
