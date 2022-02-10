package com.linyuanbaobao.amqp.event;

import org.reflections.Reflections;

import java.util.Objects;
import java.util.Set;

/**
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
public class EventMessageScanner {
    private String pkg;

    public EventMessageScanner(String pkg) {
        Objects.requireNonNull(pkg, "pkg");
        this.pkg = pkg;
    }

    public Set<Class<?>> scan() {
        Reflections reflections = new Reflections(this.pkg);
        return reflections.getTypesAnnotatedWith(EventMessage.class);
    }
}
