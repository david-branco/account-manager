package Communication;

import java.io.Serializable;

/**
 * Created by db on 01/05/16.
 */
public class Operation implements Serializable {

    private int operation_id;

    public Operation() {
        this.operation_id = -1;
    }

    public Operation(int operation_id) {
        this.operation_id = operation_id;
    }

    public int getOperation_id() {
        return operation_id;
    }
}


