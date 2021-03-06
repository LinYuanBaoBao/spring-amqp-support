package com.linyuanbaobao.amqp.listener.decorator;

import java.util.HashSet;
import java.util.Set;

/**
 * @author linyuan - 765371578@qq.com
 * @since 2022/1/6
 */
public class SimpleMemoryIdempotentValidator extends AbstractIdempotentValidator {
    private Set<Object> idSet = new HashSet<>();

    /**
     * @param skipIfMessageIdNull indicate whether skip validation if message id null
     */
    public SimpleMemoryIdempotentValidator(boolean skipIfMessageIdNull) {
        super(skipIfMessageIdNull);
    }

    @Override
    protected boolean doValid(Object id) {
        if (idSet.contains(id)) {
            return true;
        } else {
            idSet.add(id);
            return false;
        }
    }
}
