package nl.tvdven.productorders.commands;

import nl.tvdven.productorders.domain.OrderId;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Created by Edison Xu on 2017/3/15.
 */
public class ConfirmOrderCommand {

    @TargetAggregateIdentifier
    private OrderId id;

    public ConfirmOrderCommand() {
    }

    public ConfirmOrderCommand(OrderId id) {
        this.id = id;
    }

    public OrderId getId() {
        return id;
    }

}
