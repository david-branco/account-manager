package Communication;

/**
 * Created by db on 02/05/16.
 */
public class OpHistory  extends Operation {

    private int user_id;
    private int quantity;

    public OpHistory(int operation_id, int user_id, int quantity) {
        super(operation_id);
        this.user_id = user_id;
        this.quantity = quantity;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
