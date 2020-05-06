package nl.tvdven.productorders.handlers;

import nl.tvdven.productorders.aggregates.OrderAggregate;
import nl.tvdven.productorders.aggregates.ProductAggregate;
import nl.tvdven.productorders.commands.ConfirmOrderCommand;
import nl.tvdven.productorders.commands.CreateOrderCommand;
import nl.tvdven.productorders.commands.RollbackOrderCommand;
import nl.tvdven.productorders.domain.OrderProduct;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.Repository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

//import nl.tvdven.productorders.repository.OrderAggregateRepository;
//import nl.tvdven.productorders.repository.ProductAggregateRepository;

/**
 * Created by Edison on 2017/3/9.
 */
@Component
public class OrderHandler {

    private static final Logger LOGGER = getLogger(OrderHandler.class);

    @Autowired
    private Repository<OrderAggregate> repository;

    @Autowired
    private Repository<ProductAggregate> productRepository;

    @CommandHandler
    public void handle(CreateOrderCommand command) throws Exception {
        Map<String, OrderProduct> products = new HashMap<>();
        command.getProducts().forEach((productId,number)->{
            LOGGER.debug("Loading product information with productId: {}",productId);
            Aggregate<ProductAggregate> aggregate = productRepository.load(productId);
            products.put(productId,
                    new OrderProduct(productId,
                            aggregate.invoke(ProductAggregate::getName),
                            aggregate.invoke(ProductAggregate::getPrice),
                            number));
        });
        repository.newInstance(() -> new OrderAggregate(command.getOrderId(), command.getUsername(), products));
    }

    @CommandHandler
    public void handle(RollbackOrderCommand command){
        Aggregate<OrderAggregate> aggregate = repository.load(command.getOrderId().getIdentifier());
        aggregate.execute(OrderAggregate::delete);
    }

    @CommandHandler
    public void handle(ConfirmOrderCommand command){
        Aggregate<OrderAggregate> aggregate = repository.load(command.getId().getIdentifier());
        aggregate.execute(OrderAggregate::confirm);
    }

}
