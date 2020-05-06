package nl.tvdven.productorders.config;

import nl.tvdven.productorders.aggregates.ProductAggregate;
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
public class ProductConfig {

//    @Bean
//    @Scope("prototype")
//    public ProductAggregate productAggregate(){
//        return new ProductAggregate();
//    }

    @Bean
    public AggregateFactory<ProductAggregate> productAggregateAggregateFactory(){
        SpringPrototypeAggregateFactory<ProductAggregate> aggregateFactory = new SpringPrototypeAggregateFactory<>("productAggregate");
        return aggregateFactory;
    }

    @Bean
    EventSourcingRepository<ProductAggregate> productAggregateRepository(EventStore eventStore){
        EventSourcingRepository<ProductAggregate> repository = EventSourcingRepository.builder(ProductAggregate.class).eventStore(eventStore).build();
        return repository;
    }
}
