package Account;

import Backup.AccountManagerBackup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by db on 26/04/16.
 */
public class AccountManager implements Serializable {

    private int id_counter;
    private String id;
    private HashMap<Integer, Account> accounts;
    private AccountManagerBackup amb;


    public AccountManager() {
        this.id_counter = 0;
        this.id = new java.rmi.dgc.VMID().toString();
        this.accounts = new HashMap<>();
    }


    public AccountManager(int id_counter, HashMap<Integer, Account> accounts) {
        this.id_counter = id_counter;
        this.id = new java.rmi.dgc.VMID().toString();
        this.accounts = new HashMap<>();
        for(Account account : accounts.values()) {
            this.accounts.put(account.getId(), account.clone());
        }
    }


    public AccountManager(AccountManager am) {
        this.id_counter = am.getId_counter();
        this.id = new java.rmi.dgc.VMID().toString();
        this.accounts = new HashMap<>();
        for(Account account: am.getAccounts().values()) {
            this.accounts.put(account.getId(), account.clone());
        }
    }

    public AccountManager clone() { return new AccountManager(this); }

    public HashMap<Integer, Account> getAccounts() {
        HashMap<Integer, Account> aux = new HashMap<>();
        for(Account account: this.accounts.values()) {
            aux.put(account.getId(), account.clone());
        }
        return aux;
    }

    public void startAccountManagerBackup() {
        if(id_counter == 0) {
            this.amb = new AccountManagerBackup("../backups/"+id);
        }
        else { this.amb = new AccountManagerBackup("../backups/"+id, this); }
    }


    public int addAccount(int amount) {
        id_counter++;
        if(amount >= 0) {
            Account account = new Account(id_counter, amount);
            String historyUpdate = "Account " + id_counter + " created \t\t\t\t\t\t\t(balance: " + amount + ")";
            account.updateHistory(historyUpdate);
            accounts.put(id_counter, account);
            amb.addAccount(id_counter, amount, historyUpdate);
            return id_counter;
        }
        else {
            return -1;
        }
    }

    public boolean addAccount(int account_id, int balance, ArrayList<String> history) {
        if(accounts.get(account_id) == null) {
            Account account = new Account(account_id, balance, history);
            accounts.put(account_id, account);
        }
        return true;
    }

    public int isAccount(int account_id) {
        if(accounts.get(account_id) != null) {
            return account_id;
        }
        else { return -1; }
    }


    public int getId_counter() { return this.id_counter; }

    public void setId_counter(int id_counter) { this.id_counter = id_counter; }

    public int getBalance(int account_id) {
        return accounts.get(account_id).getBalance();
    }

    public boolean movement(int account_id, int amount) {
        String historyUpdate;
        boolean result = accounts.get(account_id).movement(amount);
        if(result) {
            int balance = accounts.get(account_id).getBalance();
            historyUpdate = "Movement of " + amount + " concluded \t\t\t\t\t\t(balance: " + balance +")";
            accounts.get(account_id).updateHistory(historyUpdate);
            amb.movement(account_id, balance, historyUpdate);
        }
        return result;
    }

    public boolean transfer(int source, int destiny, int amount) {
        if(accounts.get(source) == null || accounts.get(destiny) == null || amount <= 0) {
            // System.out.println("Cannot transfer an negative ammount or inexistent destiny (" + destiny + ") or source (" + source +").");
            return false;
        }
        else if(accounts.get(source).movement(-amount)) {
            accounts.get(destiny).movement(amount);
            int sourceBalance = accounts.get(source).getBalance();
            String historyUpdateSource = "Transference of " + amount + " to account " + destiny + " completed \t\t\t\t(balance: " + sourceBalance +")";
            accounts.get(source).updateHistory(historyUpdateSource);
            amb.movement(source, sourceBalance, historyUpdateSource);

            int destinyBalance = accounts.get(destiny).getBalance();
            String historyUpdateDestiny = "Transference of " + amount + " from account " + source + " completed \t\t\t(balance: " + destinyBalance +")";
            accounts.get(destiny).updateHistory(historyUpdateDestiny);
            amb.movement(destiny, destinyBalance, historyUpdateDestiny);
            return true;
        }

        return false;
    }

    public String accountHistory(int account_id, int quantity) {
        return accounts.get(account_id).showHistory(quantity);
    }
}
