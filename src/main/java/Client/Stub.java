package Client;

import Communication.*;
import net.sf.jgcs.*;
import net.sf.jgcs.jgroups.JGroupsGroup;
import net.sf.jgcs.jgroups.JGroupsProtocolFactory;
import net.sf.jgcs.jgroups.JGroupsService;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by db on 01/05/16.
 */
public class Stub implements MessageListener {

    private int user_id;
    private static DataSession ds;
    private int operation_id;
    private String menu;

    public Stub(String ipGroup) {
        try {
            this.user_id = -1;
            this.operation_id = 1;
            this.menu = printMenu();
            JGroupsProtocolFactory pf = new JGroupsProtocolFactory();
            JGroupsGroup gg = new JGroupsGroup(ipGroup);
            Protocol p = pf.createProtocol();
            ds = p.openDataSession(gg);
            ds.setMessageListener(this);

            ControlSession cs = p.openControlSession(gg);
            cs.join();
        } catch (IOException ex) {
            Logger.getLogger(Stub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String printMenu() {
        StringBuilder s = new StringBuilder();
        s.append("\n----------------------------------- MENU -----------------------------------\n");
        s.append("|LOGIN id|\t\t\t\tLogin into id Account.\n");
        s.append("|CREATE amount|\t\t\t\tCreate Account with a certain amount.\n");
        s.append("|MOVE amount|\t\t\t\tDeposit/Withdraw certain amount.\n");
        s.append("|TRANSF destiny amount|\t\t\tTransfer to destiny some amount.\n");
        s.append("|BAL|\t\t\t\t\tShow account balance.\n");
        s.append("|HIST quantity|\t\t\t\tShow last quantity history entries.\n");
        s.append("|LOGOUT|\t\t\t\tLogout from Account.\n");
        s.append("|EXIT|\t\t\t\t\tLeave Account Manager.\n");
        s.append("|HELP|\t\t\t\t\tExamples of how to use this Menu.\n");
        s.append(">> ");
        return s.toString();
    }

    private synchronized void incOperationID() {
        this.operation_id++;
    }

    private synchronized int getOperationID() {
        return this.operation_id;
    }

    public void login(int user_id) {
        if(verifyLogout()) {
            Operation operation = new OpLogin(operation_id, user_id);
            sendMessage(operation);
        }
    }

    public void logout() {
        if(verifyLogin()) {
            this.user_id = -1;
            System.out.println("Logout Completed");
            System.out.print(menu);
        }
    }

    public void createAccount(int amount) {
        if(verifyLogout()) {
            Operation operation = new OpCreate(operation_id, amount);
            sendMessage(operation);
        }
    }

    public void movement(int amount) {
        if(verifyLogin()) {
            Operation operation = new OpMovement(operation_id, user_id, amount);
            sendMessage(operation);
        }
    }

    public void transference(int destinyID, int amount) {
        if(verifyLogin()) {
            if (user_id != destinyID) {
                Operation operation = new OpTransference(operation_id, user_id, destinyID, amount);
                sendMessage(operation);
            } else {
                System.out.println("Destiny account id cannot be equal to user id.");
                System.out.print(menu);
            }
        }
    }

    public void balance() {
        if(verifyLogin()) {
            Operation operation = new OpBalance(operation_id, user_id);
            sendMessage(operation);
        }
    }

    public void history(int value) {
        if(verifyLogin()) {
            Operation operation = new OpHistory(operation_id, user_id, value);
            sendMessage(operation);
        }
    }

    public void exit() {
        System.exit(1);
    }

    private boolean verifyLogout() {
        if(this.user_id != -1) {
            System.out.println("You must first logout user " + user_id + ".");
            System.out.print(menu);
            return false;
        }
        return true;
    }

    private boolean verifyLogin() {
        if (this.user_id == -1) {
            System.out.println("You must first create an account or login.");
            System.out.print(menu);
            return false;
        }
        return true;
    }

    public Object onMessage(Message message) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(message.getPayload());
            ois = new ObjectInputStream(bais);

            Operation operation = (Operation) ois.readObject();
            int operation_id = operation.getOperation_id();

           // synchronized (this) {
                if (operation_id == getOperationID()) {
                    if (operation instanceof ResCreate || operation instanceof ResMovement || operation instanceof ResTransference || operation instanceof ResBalance || operation instanceof ResLogin || operation instanceof ResHistory) {

                        incOperationID();
                        if (operation instanceof ResCreate) {
                            this.user_id = ((ResCreate) operation).getUser_id();
                            System.out.println("Account " + user_id + " created !\n");
                        } else if (operation instanceof ResMovement) {
                            boolean response = ((ResMovement) operation).getResponse();
                            if (response) {
                                System.out.println("Movement Complete !\n");
                            } else {
                                System.out.println("Movement Unavailable !\n");
                            }
                        } else if (operation instanceof ResTransference) {
                            boolean response = ((ResTransference) operation).getResponse();
                            if (response) {
                                System.out.println("Transference Complete !\n");
                            } else {
                                System.out.println("Transference Unavailable (Invalid Destiny id or Amount Unavailable)!\n");
                            }
                        } else if (operation instanceof ResBalance) {
                            int balance = ((ResBalance) operation).getAmount();
                            System.out.println("Account Balance: " + balance + "\n");
                        } else if (operation instanceof ResHistory) {
                            String history = ((ResHistory) operation).getHistory();
                            System.out.println(history);
                        } else if (operation instanceof ResLogin) {
                            this.user_id = ((ResLogin) operation).getUser_id();
                            if (this.user_id != -1) {
                                System.out.println("Login into account " + this.user_id + " completed!\n");
                            } else {
                                System.out.println("Account not found!\n");
                            }
                        }

                        System.out.print(menu);
                    } else {
                        //System.out.println("[CLIENT] Can't handle " + operation.getClass() + " from " + message.getSenderAddress());
                    }
                } else {
                    //System.out.println("[CLIENT] Ignored Message " + operation_id + " " + operation.getClass() + " from " + message.getSenderAddress());
                }
            //}

            ois.close();
            bais.close();

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try {
                ois.close();
                bais.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    private void sendMessage(Operation operation) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(operation);
            oos.flush();
            byte[] data = baos.toByteArray();

            Message m = ds.createMessage();
            m.setPayload(data);
            ds.multicast(m, new JGroupsService(), null);

            oos.close();
            baos.close();

        } catch (IOException ex) {
            Logger.getLogger(Stub.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
                baos.close();
            } catch (IOException ex) {
                Logger.getLogger(Stub.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
