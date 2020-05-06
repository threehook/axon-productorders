package nl.tvdven.productorders.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * Created by Edison Xu on 2017/4/10.
 */
public class ChangeStockCommand {

    @TargetAggregateIdentifier
    protected String id;
    protected int number;

    public ChangeStockCommand() {
    }

    public ChangeStockCommand(String id, int number) {
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
