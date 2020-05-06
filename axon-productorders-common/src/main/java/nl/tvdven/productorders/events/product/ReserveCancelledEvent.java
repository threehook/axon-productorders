package nl.tvdven.productorders.events.product;

import nl.tvdven.productorders.domain.OrderId;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Created by Edison Xu on 2017/3/15.
 */
public class ReserveCancelledEvent {

    private OrderId orderId;
    @TargetAggregateIdentifier
    private String productId;
    private int amount;

    public ReserveCancelledEvent() {
    }

    public ReserveCancelledEvent(OrderId orderId, String productId, int amount) {
        this.orderId = orderId;
        this.productId = productId;
        this.amount = amount;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public String getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }
}
