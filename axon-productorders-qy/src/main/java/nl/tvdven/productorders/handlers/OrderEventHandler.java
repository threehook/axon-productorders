package nl.tvdven.productorders.handlers;

import nl.tvdven.productorders.entries.OrderEntry;
import nl.tvdven.productorders.entries.OrderProductEntry;
import nl.tvdven.productorders.events.order.OrderCancelledEvent;
import nl.tvdven.productorders.events.order.OrderConfirmedEvent;
import nl.tvdven.productorders.events.order.OrderCreatedEvent;
import nl.tvdven.productorders.repository.OrderEntryRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This event handler is used to update the repository data of the query side in the CQRS.
 * Created by Edison Xu on 2017/3/15.
 */
@Component
@ProcessingGroup("order")
public class OrderEventHandler {

    private static final Logger LOGGER = getLogger(OrderEventHandler.class);

    @Autowired
    private OrderEntryRepository repository;

    @EventHandler
    public void on(OrderCreatedEvent event){
        Map<String, OrderProductEntry> map = new HashMap<>();
        event.getProducts().forEach((id, product)->{
            map.put(id,
                    new OrderProductEntry(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getAmount()));
        });
        OrderEntry order = new OrderEntry(event.getOrderId().toString(), event.getUsername(), map);
        repository.save(order);
    }

    @EventHandler
    public void on(OrderConfirmedEvent event){
        String id = event.getId().getIdentifier();
        Optional<OrderEntry> o = repository.findById(id);
        if (!o.isPresent()) {
            LOGGER.error("Cannot find order with id {}", id);
            return;
        }
        OrderEntry order = o.get();
        order.setStatus("confirmed");
        repository.save(order);
    }

    @EventHandler
    public void on(OrderCancelledEvent event){
        String id = event.getId().getIdentifier();
        Optional<OrderEntry> o = repository.findById(id);
        if (!o.isPresent()) {
            LOGGER.error("Cannot find order with id {}", id);
            return;
        }
        OrderEntry order = o.get();
        order.setStatus("canceled");
        repository.save(order);
    }
}
