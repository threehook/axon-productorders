package nl.tvdven.productorders.events.order;

import nl.tvdven.productorders.domain.OrderId;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Created by Edison Xu on 2017/3/15.
 */
public class OrderCancelledEvent {
    @TargetAggregateIdentifier
    private OrderId id;

    public OrderCancelledEvent() {
    }

    public OrderCancelledEvent(OrderId id) {
        this.id = id;
    }

    public OrderId getId() {
        return id;
    }
}
