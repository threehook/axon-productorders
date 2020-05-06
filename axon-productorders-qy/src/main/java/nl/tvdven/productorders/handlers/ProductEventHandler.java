package nl.tvdven.productorders.handlers;

import nl.tvdven.productorders.entries.ProductEntry;
import nl.tvdven.productorders.events.product.*;
import nl.tvdven.productorders.repository.ProductEntryRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This event handler is used to update the repository data of the query side in the CQRS.
 * Created by Edison Xu on 2017/3/7.
 */
@Component
@ProcessingGroup("product")
public class ProductEventHandler {

    private static final Logger LOGGER = getLogger(ProductEventHandler.class);

    @Autowired
    private ProductEntryRepository repository;

    @EventHandler
    public void on(ProductCreatedEvent event){
        // update the data in the cache or db of the query side
        LOGGER.debug("repository data is updated");
        repository.save(new ProductEntry(event.getId(), event.getName(), event.getPrice(), event.getStock()));
    }

    @EventHandler
    public void on(ProductReservedEvent event){
        String id = event.getProductId();
        Optional<ProductEntry> p = repository.findById(id);
        if (!p.isPresent()) {
            LOGGER.error("Cannot find product with id {}", id);
            return;
        }
        ProductEntry product = p.get();
        product.setStock(event.getAmount());
        repository.save(product);
    }

    @EventHandler
    public void on(ReserveCancelledEvent event){
        String id = event.getProductId();
        Optional<ProductEntry> p = repository.findById(id);
        if (!p.isPresent()) {
            LOGGER.error("Cannot find product with id {}", id);
            return;
        }
        ProductEntry product = p.get();
        product.setStock(event.getAmount());
        repository.save(product);
    }

    @EventHandler
    public void on(IncreaseStockEvent event){
        String id = event.getId();
        Optional<ProductEntry> p = repository.findById(id);
        if (!p.isPresent()) {
            LOGGER.error("Cannot find product with id {}", id);
            return;
        }
        ProductEntry product = p.get();
        product.setStock(product.getStock()+event.getNumber());
        repository.save(product);
    }

    @EventHandler
    public void on(DecreaseStockEvent event){
        String id = event.getId();
        Optional<ProductEntry> p = repository.findById(id);
        if (!p.isPresent()) {
            LOGGER.error("Cannot find product with id {}", id);
            return;
        }
        ProductEntry product = p.get();
        product.setStock(product.getStock()-event.getNumber());
        repository.save(product);
    }
}
