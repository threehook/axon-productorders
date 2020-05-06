package nl.tvdven.productorders.saga;

import nl.tvdven.productorders.commands.ConfirmOrderCommand;
import nl.tvdven.productorders.commands.ReserveProductCommand;
import nl.tvdven.productorders.commands.RollbackOrderCommand;
import nl.tvdven.productorders.commands.RollbackReserveCommand;
import nl.tvdven.productorders.domain.OrderId;
import nl.tvdven.productorders.domain.OrderProduct;
import nl.tvdven.productorders.events.order.OrderCancelledEvent;
import nl.tvdven.productorders.events.order.OrderConfirmedEvent;
import nl.tvdven.productorders.events.order.OrderCreatedEvent;
import nl.tvdven.productorders.events.product.ProductNotEnoughEvent;
import nl.tvdven.productorders.events.product.ProductReservedEvent;
import nl.tvdven.productorders.events.product.ReserveCancelledEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Edison Xu on 2017/3/15.
 */
@Saga
public class OrderSaga {

    private static final Logger LOGGER = getLogger(OrderSaga.class);

    private OrderId orderIdentifier;
    //TODO: use ConcurrentHashMap instead?
    private Map<String, OrderProduct> toReserve;
    private Map<String, OrderProduct> toRollback;
    private int toReserveNumber;
    private boolean needRollback;

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event){
        this.orderIdentifier = event.getOrderId();
        this.toReserve = event.getProducts();
        toRollback = new HashMap<>();
        toReserveNumber = toReserve.size();
        event.getProducts().forEach((id,product)->{
            ReserveProductCommand command = new ReserveProductCommand(orderIdentifier, id, product.getAmount());
            commandGateway.send(command);
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductNotEnoughEvent event){
        LOGGER.info("No enough item to buy");
        toReserveNumber--;
        needRollback=true;
        if(toReserveNumber==0)
            tryFinish();
    }

    private void tryFinish() {
        if(needRollback){
            toReserve.forEach((id, product)->{
                if(!product.isReserved())
                    return;
                toRollback.put(id, product);
                commandGateway.send(new RollbackReserveCommand(orderIdentifier, id, product.getAmount()));
            });
            if(toRollback.isEmpty())
                commandGateway.send(new RollbackOrderCommand(orderIdentifier));
            return;
        }
        commandGateway.send(new ConfirmOrderCommand(orderIdentifier));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ReserveCancelledEvent event){
        toRollback.remove(event.getProductId());
        if(toRollback.isEmpty())
            commandGateway.send(new RollbackOrderCommand(event.getOrderId()));
    }

    @SagaEventHandler(associationProperty = "id", keyName = "orderId")
    @EndSaga
    public void handle(OrderCancelledEvent event) {
        LOGGER.info("Order {} is cancelled", event.getId());
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent event){
        OrderProduct reservedProduct = toReserve.get(event.getProductId());
        reservedProduct.setReserved(true);
        toReserveNumber--;
        //Q: will a concurrent issue raise?
        if(toReserveNumber ==0)
            tryFinish();
    }

    @SagaEventHandler(associationProperty = "id", keyName = "orderId")
    @EndSaga
    public void handle(OrderConfirmedEvent event){
        LOGGER.info("Order {} is confirmed", event.getId());
    }

}
