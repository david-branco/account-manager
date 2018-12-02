package Server;

import Account.AccountManager;
import Communication.*;
import net.sf.jgcs.*;
import net.sf.jgcs.annotation.PointToPoint;
import net.sf.jgcs.jgroups.JGroupsGroup;
import net.sf.jgcs.jgroups.JGroupsService;

import java.io.*;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by db on 30/04/16.
 */
public class ClientHandler implements MessageListener {

    private AccountManager am;
    private DataSession ds;

    private ServerState state;

    public enum ServerState {
        STARTING, INITIALIZING, READY
    }

    public ClientHandler(AccountManager am, JGroupsGroup gg, Protocol p, ServerState state) {
        try {
            this.am = am;
            this.state = state;

            this.ds = p.openDataSession(gg);
            this.ds.setMessageListener(this);

            ControlSession cs = p.openControlSession(gg);
            cs.join();
        } catch (GroupException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeAccountManager() {
        am.addAccount(100);
        am.movement(1, 30);
        am.movement(1, -10);
        am.movement(1, -1000); //Fail
        am.getBalance(1);
        am.movement(1, 20);
        am.movement(1, -40);
        am.accountHistory(1, 3);

        am.addAccount(300);
        am.movement(2, 200);
        am.movement(2, -10);
        am.movement(2, -400);
        am.getBalance(2);
        am.transfer(2, 1, 1000); //Fail
        am.transfer(2, 1, -1000); //Fail
        am.transfer(2, 1, 50);
        am.getBalance(2);
        am.accountHistory(2, 10);

        am.addAccount(10);
        am.movement(3, 200);
        am.movement(3, 300);
        am.movement(3, 400);
        am.transfer(3, 3, 2000); //Fail
        am.transfer(3, 2, 200);
        am.accountHistory(3, 5);
        System.out.println("[SERVER] Account Manager initialized with success !");
    }

    public void start() {
        if(this.state == ServerState.STARTING) {
            Operation operation = new OpInit();
            sendMessage(operation, null);
        }
        else {
            am.startAccountManagerBackup();
            initializeAccountManager();
        }
    }

    @Override
    public Object onMessage(Message message) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;

       // synchronized (this) {
            try {
                bais = new ByteArrayInputStream(message.getPayload());
                ois = new ObjectInputStream(bais);

                boolean result;
                Operation response, operation = (Operation) ois.readObject();
                int operation_id = operation.getOperation_id();

                if (operation instanceof OpInit) {
                    if (this.state == ServerState.STARTING) {
                        this.state = ServerState.INITIALIZING;
                    } else {
                        System.out.println("[SERVER] New Server wants to join !!");
                        response = new ResInit(this.am);
                        sendMessage(response, message.getSenderAddress());
                    }
                } else if (operation instanceof ResInit) {
                    if (this.state == ServerState.INITIALIZING) {
                        this.state = ServerState.READY;
                        this.am = ((ResInit) operation).getAccountManager();
                        this.am.startAccountManagerBackup();
                        System.out.println("[SERVER] Replica Ready (Total Accounts " + am.getId_counter() + ")");
                    }
                } else if (this.state == ServerState.READY) {
                    if (operation instanceof OpLogin) {
                        int user_id = am.isAccount(((OpLogin) operation).getUser_id());
                        response = new ResLogin(operation_id, user_id);

                        sendMessage(response, message.getSenderAddress());
                        if (user_id != -1) {
                            System.out.println("[SERVER] User " + user_id + " logged.");
                        }
                    } else if (operation instanceof OpCreate) {
                        int user_id = am.addAccount(((OpCreate) operation).getAmount());
                        response = new ResCreate(operation_id, user_id);

                        sendMessage(response, message.getSenderAddress());
                        if (user_id != -1) {
                            System.out.println("[SERVER] Account " + user_id + " created.");
                        }
                    } else if (operation instanceof OpMovement) {
                        int user_id = ((OpMovement) operation).getUser_id();
                        int amount = ((OpMovement) operation).getAmount();
                        result = am.movement(user_id, amount);
                        response = new ResMovement(operation_id, result);

                        sendMessage(response, message.getSenderAddress());
                        if (result) {
                            System.out.println("[SERVER] Movement account " + user_id + " of " + amount + " completed.");
                        } else {
                            System.out.println("[SERVER] Movement account " + user_id + " of " + amount + " canceled.");
                        }
                    } else if (operation instanceof OpTransference) {
                        int user_id = ((OpTransference) operation).getUser_id();
                        int destiny_id = ((OpTransference) operation).getDestiny_id();
                        int amount = ((OpTransference) operation).getAmount();
                        result = am.transfer(user_id, destiny_id, amount);
                        response = new ResTransference(operation_id, result);

                        sendMessage(response, message.getSenderAddress());
                        if (result) {
                            System.out.println("[SERVER] Transference from user " + user_id + " to user " + destiny_id + " of " + amount + " completed.");
                        } else {
                            System.out.println("[SERVER] Transference from user " + user_id + " to user " + destiny_id + " of " + amount + " canceled.");
                        }
                    } else if (operation instanceof OpBalance) {
                        int user_id = ((OpBalance) operation).getUser_id();
                        int balance = am.getBalance(user_id);
                        response = new ResBalance(operation_id, balance);

                        sendMessage(response, message.getSenderAddress());
                        System.out.println("[SERVER] Balance account " + user_id + " of " + balance + " sent.");
                    } else if (operation instanceof OpHistory) {
                        int user_id = ((OpHistory) operation).getUser_id();
                        int quantity = ((OpHistory) operation).getQuantity();
                        String history = am.accountHistory(user_id, quantity);
                        response = new ResHistory(operation_id, history);
                        sendMessage(response, message.getSenderAddress());
                        System.out.println("[SERVER] History account " + user_id + " sent.");
                    } else {
                        System.out.println("[SERVER] Can't handle " + operation.getClass() + " from " + message.getSenderAddress());
                    }
                } else {
                    System.out.println("[SERVER] Can't handle " + operation.getClass() + " from " + message.getSenderAddress());
                }

                ois.close();
                bais.close();

            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    ois.close();
                    bais.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        //}
        return null;
    }

    private void sendMessage(Operation response, SocketAddress senderAddress) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(response);
            oos.flush();
            byte[] data = baos.toByteArray();

            Message msg = this.ds.createMessage();
            msg.setPayload(data);
            if (senderAddress == null) {
                this.ds.multicast(msg, new JGroupsService(), null);
            } else {
                this.ds.multicast(msg, new JGroupsService(), null, new PointToPoint(senderAddress));
            }

            oos.close();
            baos.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
                baos.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
