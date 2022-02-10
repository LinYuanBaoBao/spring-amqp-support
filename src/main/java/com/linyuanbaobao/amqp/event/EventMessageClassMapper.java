package com.linyuanbaobao.amqp.event;

import com.linyuanbaobao.amqp.event.exception.EventMessageException;
import com.linyuanbaobao.amqp.event.util.EventMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
@Slf4j
public class EventMessageClassMapper implements ClassMapper {
    public static final String EVENT_CODE_HEADER = "_EVENT_CODE_";

    private ClassMapper delegate;
    String eventCodeHeader;
    private EventMessageTypeMapping mapping;

    public EventMessageClassMapper(String eventCodeHeader, EventMessageTypeMapping mapping) {
        Objects.requireNonNull(eventCodeHeader, "eventCodeHeader");
        Objects.requireNonNull(mapping, "mapping");
        this.eventCodeHeader = eventCodeHeader;
        this.mapping = mapping;
        this.delegate = new DefaultJackson2JavaTypeMapper();
    }

    public EventMessageClassMapper(EventMessageTypeMapping mapping) {
        this(EVENT_CODE_HEADER, mapping);
    }

    public void setDelegateMapper(ClassMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public void fromClass(Class<?> clazz, MessageProperties messageProperties) {
        String event = EventMessageUtils.getEventCode(clazz);
        if (StringUtils.isEmpty(event)) {
            if (delegate == null) {
                throw new UnsupportedOperationException(String.format("Should add annotation %s to class %s or specify delegate ClassMapper.", EventMessage.class, clazz));
            }
            log.debug("Class {} is not an event message. fallback to delegate {}.", clazz, this.delegate);
            this.delegate.fromClass(clazz, messageProperties);
        } else {
            messageProperties.getHeaders().put(this.eventCodeHeader, event);
        }
    }

    @Override
    public Class<?> toClass(MessageProperties messageProperties) {
        String exchange = messageProperties.getReceivedExchange();
        String event = (String) messageProperties.getHeaders().get(this.eventCodeHeader);
        if (StringUtils.isEmpty(event)) {
            log.debug("event code not found. fallback to delegate {}.", this.delegate);
            return this.delegate.toClass(messageProperties);
        } else {
            Class<?> clazz = this.mapping.getMapping(exchange, event);
            if (clazz == null) {
                throw new EventMessageException(String.format("class that mapping to %s of exchange %s is not exists.", event, exchange));
            }
            return clazz;
        }
    }
}
