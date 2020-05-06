package nl.tvdven.productorders.aggregates;

import nl.tvdven.productorders.commands.CreateProductCommand;
import nl.tvdven.productorders.commands.DecreaseStockCommand;
import nl.tvdven.productorders.commands.IncreaseStockCommand;
import nl.tvdven.productorders.domain.OrderId;
import nl.tvdven.productorders.events.product.*;
import nl.tvdven.productorders.exceptions.NoEnoughStockException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Edison Xu on 2017/3/7.
 */
@Aggregate
public class ProductAggregate {

    private static final Logger LOGGER = getLogger(ProductAggregate.class);

    @AggregateIdentifier
    private String id;
    private String name;
    private int stock;
    private long price;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        apply(new ProductCreatedEvent(command.getId(),command.getName(),command.getPrice(),command.getStock()));
    }

    @CommandHandler
    public void handle(IncreaseStockCommand command) throws NoEnoughStockException {
        apply(new IncreaseStockEvent(command.getId(),command.getNumber()));
    }

    @CommandHandler
    public void handle(DecreaseStockCommand command) {
        if(stock>=command.getNumber())
            apply(new DecreaseStockEvent(command.getId(),command.getNumber()));
        else
            throw new NoEnoughStockException("No enough items");
    }

    @EventHandler
    public void on(ProductCreatedEvent event){
        this.id = event.getId();
        this.name = event.getName();
        this.price = event.getPrice();
        this.stock = event.getStock();
        LOGGER.debug("Product [{}] {} {}x{} is created.", id,name,price,stock);
    }

    public void reserve(OrderId orderId, int amount){
        if(stock>=amount) {
            apply(new ProductReservedEvent(orderId, id, amount));

        }else
            apply(new ProductNotEnoughEvent(orderId, id));
    }

    public void cancellReserve(OrderId orderId, int amount){
        apply(new ReserveCancelledEvent(orderId, id, stock));
    }

    @EventHandler
    public void on(ProductReservedEvent event){
        int oriStock = stock;
        stock = stock - event.getAmount();
        LOGGER.info("Product {} stock change {} -> {}", id, oriStock, stock);
    }

    @EventHandler
    public void on(ReserveCancelledEvent event){
        stock +=event.getAmount();
        LOGGER.info("Reservation rollback, product {} stock changed to {}", id, stock);
    }

    @EventHandler
    public void on(IncreaseStockEvent event){
        stock = stock + event.getNumber();
        LOGGER.info("Product {} stock increase {}, current value: {}", id, event.getNumber(), stock);
    }

    @EventHandler
    public void on(DecreaseStockEvent event){
        stock = stock - event.getNumber();
        LOGGER.info("Product {} stock decrease {}, current value: {}", id, event.getNumber(), stock);
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public long getPrice() {
        return price;
    }
}
