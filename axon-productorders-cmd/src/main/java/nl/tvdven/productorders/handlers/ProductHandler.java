package nl.tvdven.productorders.handlers;

import nl.tvdven.productorders.aggregates.ProductAggregate;
import nl.tvdven.productorders.commands.ReserveProductCommand;
import nl.tvdven.productorders.commands.RollbackReserveCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.Repository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Ton van de ven on 2020/05/03.
 */
@Component
public class ProductHandler {

    private static final Logger LOGGER = getLogger(ProductHandler.class);

    @Autowired
    private Repository<ProductAggregate> repository;

    @CommandHandler
    public void handle(ReserveProductCommand command) {
        Aggregate<ProductAggregate> aggregate = repository.load(command.getProductId());
        aggregate.execute(aggregateRoot->aggregateRoot.reserve(command.getOrderId(), command.getNumber()));
    }

    @CommandHandler
    public void handle(RollbackReserveCommand command) {
        Aggregate<ProductAggregate> aggregate = repository.load(command.getProductId());
        aggregate.execute(aggregateRoot->aggregateRoot.reserve(command.getOrderId(), command.getNumber()));
    }
}
