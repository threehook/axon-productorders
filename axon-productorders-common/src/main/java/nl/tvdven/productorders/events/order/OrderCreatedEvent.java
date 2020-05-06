package nl.tvdven.productorders.events.order;

import nl.tvdven.productorders.domain.OrderId;
import nl.tvdven.productorders.domain.OrderProduct;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Map;

/**
 * Created by Edison Xu on 2017/3/7.
 */
public class OrderCreatedEvent {

    @TargetAggregateIdentifier
    private OrderId orderId;
    private String username;
    @AggregateMember
    private Map<String, OrderProduct> products;

    public OrderCreatedEvent() {
    }

    public OrderCreatedEvent(OrderId orderId, String username, Map<String, OrderProduct> products) {
        this.orderId = orderId;
        this.username = username;
        this.products = products;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    public Map<String, OrderProduct> getProducts() {
        return products;
    }
}
