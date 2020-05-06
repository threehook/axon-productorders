package nl.tvdven.productorders.config;

import nl.tvdven.productorders.aggregates.OrderAggregate;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Edison Xu on 2017/3/14.
 */
@Configuration
public class OrderConfig {

//    @Bean
//    @Scope("prototype")
//    public OrderAggregate orderAggregate(){
//        return new OrderAggregate();
//    }

    @Bean
    public AggregateFactory<OrderAggregate> orderAggregateAggregateFactory(){
        SpringPrototypeAggregateFactory<OrderAggregate> aggregateFactory = new SpringPrototypeAggregateFactory<>("orderAggregate");
        return aggregateFactory;
    }

    @Bean
    EventSourcingRepository<OrderAggregate> orderAggregateRepository(EventStore eventStore){
        EventSourcingRepository<OrderAggregate> repository = EventSourcingRepository
                .builder(OrderAggregate.class)
                .eventStore(eventStore).build();
        return repository;
    }
}
