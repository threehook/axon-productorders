package nl.tvdven.productorders.commands;

import nl.tvdven.productorders.domain.OrderId;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Created by Edison Xu on 2017/3/15.
 */
public class RollbackOrderCommand {
    @TargetAggregateIdentifier
    private OrderId orderId;

    public RollbackOrderCommand() {
    }

    public RollbackOrderCommand(OrderId orderId) {
        this.orderId = orderId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public void setOrderId(OrderId orderId) {
        this.orderId = orderId;
    }
}
