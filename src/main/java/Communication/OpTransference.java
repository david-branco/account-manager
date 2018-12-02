package Communication;

/**
 * Created by db on 02/05/16.
 */
public class OpTransference extends Operation {

    private int user_id;
    private int destiny_id;
    private int amount;

    public OpTransference(int operation_id, int user_id, int destiny_id, int amount) {
        super(operation_id);
        this.user_id = user_id;
        this.destiny_id = destiny_id;
        this.amount = amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getDestiny_id() {
        return destiny_id;
    }

    public void setDestiny_id(int destiny_id) {
        this.destiny_id = destiny_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
