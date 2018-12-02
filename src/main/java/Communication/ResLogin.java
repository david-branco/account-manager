package Communication;

/**
 * Created by db on 14/05/16.
 */
public class ResLogin extends Operation {

    private int user_id;

    public ResLogin(int operation_id, int user_id) {
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
