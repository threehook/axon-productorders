package nl.tvdven.productorders.events.product;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Created by Edison Xu on 2017/4/10.
 */
public class IncreaseStockEvent {

    @TargetAggregateIdentifier
    protected String id;
    protected int number;

    public IncreaseStockEvent() {
    }

    public IncreaseStockEvent(String id, int number) {
        this.id = id;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }
}
