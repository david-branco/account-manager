package Server;

import Account.AccountManager;
import net.sf.jgcs.Protocol;
import net.sf.jgcs.jgroups.JGroupsGroup;
import net.sf.jgcs.jgroups.JGroupsProtocolFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by db on 30/04/16.
 */
public class Server {

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Invalid argument number .\n Please insert Group Name and the Server State.");
        }
        else {
            try {
                String groupName = args[0];
                String serverState = args[1];
                ClientHandler.ServerState state = null;
                switch (serverState) {
                    case "STARTING":
                    case "starting":
                        state = ClientHandler.ServerState.STARTING;
                        break;

                    case "READY":
                    case "ready":
                        state = ClientHandler.ServerState.READY;
                        break;

                    default:
                        System.out.println("Please insert a valid Server State (STARTING or READY).");
                        System.exit(1);
                }

                JGroupsProtocolFactory gf = new JGroupsProtocolFactory();
                JGroupsGroup gg = new JGroupsGroup(groupName);
                Protocol p = gf.createProtocol();


                AccountManager account_manager = new AccountManager();
                ClientHandler ch = new ClientHandler(account_manager, gg, p, state);

                System.out.println("[SERVER] Ready !");
                ch.start();
                while (true) {
                    Thread.sleep(Long.MAX_VALUE);
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
