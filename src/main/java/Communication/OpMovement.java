package Communication;

/**
 * Created by db on 02/05/16.
 */
public class OpMovement extends Operation {

    private int user_id;
    private int amount;

    public OpMovement(int operation_id, int user_id, int amount) {
        super(operation_id);
        this.user_id = user_id;
        this.amount = amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}

