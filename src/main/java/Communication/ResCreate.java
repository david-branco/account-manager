package Communication;

/**
 * Created by db on 02/05/16.
 */
public class ResCreate extends Operation {

    private int user_id;

    public ResCreate(int operation_id, int user_id) {
        super(operation_id);
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

}
