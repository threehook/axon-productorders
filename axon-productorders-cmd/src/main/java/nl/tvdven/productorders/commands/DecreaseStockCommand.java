package nl.tvdven.productorders.commands;

/**
 * Created by Edison Xu on 2017/4/10.
 */
public class DecreaseStockCommand extends ChangeStockCommand {

    public DecreaseStockCommand() {
    }

    public DecreaseStockCommand(String id, int number) {
        super(id, number);
    }
}
