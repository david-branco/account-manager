package Communication;

/**
 * Created by db on 02/05/16.
 */
public class ResMovement extends Operation {

    private boolean response;

    public ResMovement(int operation_id, boolean response) {
        super(operation_id);
        this.response = response;
    }

    public boolean getResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
