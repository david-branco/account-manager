package Account;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by db on 26/04/16.
 */
public class Account implements Serializable {

    private final int id;
    private int balance;
    private ArrayList<String> history;

    public Account (int id, int balance) {
        this.id  = id;
        this.balance = balance;
        this.history = new ArrayList<>();
    }

    public Account (int id, int balance, ArrayList<String> history) {
        this.id  = id;
        this.balance = balance;
        this.history = new ArrayList<>();
        for(String h: history) {
            this.history.add(h);
        }
    }

    public Account(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
        this.history = new ArrayList<>();
        for(String h : account.getHistory()) {
            this.history.add(h);
        }
    }

    public Account clone() {
        return new Account(this);
    }


    public int getId() { return this.id; }

    public int getBalance() { return this.balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public ArrayList<String> getHistory() {
        ArrayList<String> aux = new ArrayList<>();
        for(String h : this.history) {
            aux.add(h);
        }
        return aux;
    }

    public void setHistory(ArrayList<String> history) {
        for(String h: history) {
            this.history.add(h);
        }
    }

    public boolean movement(int amount) {
        if((this.balance + amount) > 0) {
            this.balance += amount;
            return true;
        }
        return false;
    }

    public void updateHistory(String message) {
        history.add(message);
    }

    public String showHistory(int quantity) {
        StringBuilder s = new StringBuilder();
        s.append("-------------------------- Account "+ this.id + " History --------------------------\n");

        if(quantity > history.size()) {
            for (String h : history) {
                s.append(h+"\n");
            }
        }
        else {
            for(int i = history.size()-quantity; i < history.size(); i++) {
                s.append(history.get(i)+"\n");
            }
        }

        s.append("\n-------------------------- Balance: "+balance+"--------------------------------\n");
        return s.toString();
    }


}
