package Communication;

/**
 * Created by db on 02/05/16.
 */
public class OpCreate extends Operation {

    private int amount;

    public OpCreate(int operation_id, int amount) {
        super(operation_id);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
