# spring-amqp-support

一个 Spring-AMQP 扩展组件，便于快速发送、接收 MQ 事件消息，并将消息自动序列化为事件对象。

## 信息
基于 **spring-boot-starter-amqp：2.6.3** 构建

by：林同学（765371578@qq.com）

## Getting Started

引入依赖：

```xml
<dependency>
    <groupId>com.github.LinYuanBaoBao</groupId>
    <artifactId>spring-amqp-support</artifactId>
    <version>1.0.0-RELEASE</version>
</dependency>
```

定义 AMQP 配置类：

```java
@Configuration
public class AmqpConfiguration {
    @Bean
    public MessageConverter messageConverter(EventMessageClassMapper classMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper);      // specify class mapper
        return converter;
    }

    @Bean
    public EventMessageClassMapper eventMessageClassMapper(EventMessageTypeMapping eventMessageTypeMapping) {
        return new EventMessageClassMapper(eventMessageTypeMapping);
    }

    @Bean
    public EventMessageTypeMapping eventMessageTypeMapping() {
        return new EventMessageTypeMapping.Builder()
                .pkg("my.package")
                .build();
    }
}
```

使用 @EventMessage 注解定义事件：
```java
// my.package.UserRegisterEvent
@EventMessage(exchange = "user", code = "user-register")
public class UserRegisterEvent {
    private Integer userId;
    private Map<String, Object> userInfo;
    // ...
}
```

发送事件消息：
```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void sendMessage() {
    UserRegisterEvent event = new UserRegisterEvent();
    event.setUserId(1001);
    Map<String, Object> userInfo = new HashMap<>();
    userInfo.put("username", "linyuan");
    event.setUserInfo(userInfo);

    rabbitTemplate.convertAndSend(EventMessageUtils.getEventExchange(event.getClass()), "default", event, message -> {
        MessageProperties properties = message.getMessageProperties();
        properties.setMessageId(UUID.randomUUID().toString());
        properties.setTimestamp(new Date());
        return message;
    });
}
```

消息格式如下：
```
Properties	
    timestamp:	1644401093
    message_id:	dc7d0646-a6e6-4183-b755-79ffeefc1159
    headers: _EVENT_CODE_: user-register
    content_encoding:	UTF-8
    content_type:	application/json
Payload
    {"userId":1001,"userInfo":{"username":"linyuan"}}

```

接收事件消息：
```java
@RabbitHandler
public void listen(UserRegisterEvent message, @Headers Map headers) {
    // do something here...
}
```

或采用下面方式，但**该方式消息不会经过 InvocableHandlerMethodDecorator 处理链**
```java
@Autowired
private RabbitTemplate rabbitTemplate;

public void recMessage(){
    UserRegisterEvent msg = (UserRegisterEvent) rabbitTemplate.receiveAndConvert("user.event");
}
```

## InvocableHandlerMethodDecorator

可以注入多个 **InvocableHandlerMethodDecorator** Bean，形成消息处理链，对消息做其它操作：

```java
@Bean
public InvocableHandlerMethodDecorator invocableHandlerMethodDecorator() {
    return invocableHandlerMethod -> new InvocableHandlerMethodDecoration(invocableHandlerMethod) {
        @Override
        public Object invoke(Message<?> message, Object... providedArgs) throws Exception {
            // do something here...
            return super.invoke(message, providedArgs);
        }
    };
}
```

### IdempotentValidator

注入 **IdempotentValidator** Bean 对消息做幂等校验：

```java
@Bean
public IdempotentValidator idempotentValidator() {
    return new IdempotentValidator() {
        @Override
        public void valid(Message<?> message) {
            // do valid here
        }
    };
}
```

### Authenticator

注入 **Authenticator** Bean 在消费信息之前进行登录，在消费信息之后进行注销：

```java
@Bean
public Authenticator authenticator() {
    return new Authenticator() {
        @Override
        public void login(Message<?> message) {
            // do login here
        }

        @Override
        public void logout(Message<?> message) {
            // do logout here
        }
    };
}
```

### 记录消息信息 - MessageRecorder

注入 **MessageRecorder** Bean 来记录消息信息：

```java
@Bean
public MessageRecorder messageRecorder() {
    return new MessageRecorder() {
        @Override
        public void record(Message<?> message, boolean success, Object result, Exception error) {
            // do record here
        }
    }
}
```

