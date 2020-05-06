package nl.tvdven.productorders.config;

import com.rabbitmq.client.Channel;
import nl.tvdven.productorders.handlers.ProductEventHandler;
import org.axonframework.extensions.amqp.eventhandling.AMQPMessageConverter;
import org.axonframework.extensions.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.slf4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component("orderQueueMessageSource")
public class OrderQueueMessageSource extends SpringAMQPMessageSource {

    private static final Logger LOGGER = getLogger(ProductEventHandler.class);

    @Autowired
    public OrderQueueMessageSource(final AMQPMessageConverter messageConverter) {
        super(messageConverter);
    }

    @RabbitListener(queues = "order")
    @Override
    public void onMessage(final Message message, final Channel channel) {
        LOGGER.debug("Order message received: "+message.toString());
        super.onMessage(message, channel);
    }
}

//    @Bean
//    public SpringAMQPMessageSource orderQueueMessageSource(Serializer serializer){
//        return new SpringAMQPMessageSource(serializer){
//            @RabbitListener(queues = "order")
//            @Override
//            public void onMessage(Message message, Channel channel) {
//                LOGGER.debug("Order message received: "+message.toString());
//                super.onMessage(message, channel);
//            }
//        };
//    }
