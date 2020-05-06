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

@Component("productQueueMessageSource")
public class ProductQueueMessageSource extends SpringAMQPMessageSource {

    private static final Logger LOGGER = getLogger(ProductEventHandler.class);

    @Autowired
    public ProductQueueMessageSource(final AMQPMessageConverter messageConverter) {
        super(messageConverter);
    }

    @RabbitListener(queues = "product")
    @Override
    public void onMessage(final Message message, final Channel channel) {
        LOGGER.debug("Product message received: "+message.toString());
        super.onMessage(message, channel);
    }

}

//    @Bean
//    public SpringAMQPMessageSource productQueueMessageSource(Serializer serializer){
//        return new SpringAMQPMessageSource(serializer){
//            @RabbitListener(queues = "product")
//            @Override
//            public void onMessage(Message message, Channel channel) {
//                LOGGER.debug("Product message received: "+message.toString());
//                super.onMessage(message, channel);
//            }
//        };
//    }