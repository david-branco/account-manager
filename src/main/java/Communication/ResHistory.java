package Communication;

/**
 * Created by db on 02/05/16.
 */
public class ResHistory extends Operation {

    private String history;

    public ResHistory(int operation_id, String history) {
        super(operation_id);
        this.history = history;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}

