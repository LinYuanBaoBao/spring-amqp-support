package com.linyuanbaobao.amqp.listener.decorator;

import org.springframework.messaging.Message;

/**
 * 在使用消息之前进行身份验证
 *
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
public interface Authenticator {
    void login(Message<?> message);

    void logout(Message<?> message);

    class Dummy implements Authenticator {
        @Override
        public void login(Message<?> message) {
            // do nothing
        }

        @Override
        public void logout(Message<?> message) {
            // do nothing
        }
    }
}
