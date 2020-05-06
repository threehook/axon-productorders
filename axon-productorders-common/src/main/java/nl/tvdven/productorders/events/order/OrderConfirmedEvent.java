package nl.tvdven.productorders.events.order;

import nl.tvdven.productorders.domain.OrderId;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Created by Edison Xu on 2017/3/15.
 */
public class OrderConfirmedEvent {
    @TargetAggregateIdentifier
    private OrderId id;

    public OrderConfirmedEvent() {
    }

    public OrderConfirmedEvent(OrderId id) {
        this.id = id;
    }

    public OrderId getId() {
        return id;
    }
}
