package com.linyuanbaobao.amqp.event;

import java.lang.annotation.*;

/**
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EventMessage {
    String exchange();

    String code();
}
