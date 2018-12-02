package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by db on 30/04/16.
 */
public class Client {

    private static String menu;

    private static boolean validPositiveValue(String value) {
        try {
            int aux = Integer.parseInt(value);
            if(aux <= 0) {
                System.out.println("Value must be positive.");
                System.out.print(menu);
                return false;
            }
        }
        catch(NumberFormatException nfe) {
            System.out.println("Invalid value format.");
            System.out.print(menu);
            return false;
        }
        return true;
    }

    private static boolean validValue(String value) {
        try {
            Integer.parseInt(value);
        }
        catch(NumberFormatException nfe) {
            System.out.println("Invalid value format.");
            System.out.print(menu);
            return false;
        }
        return true;
    }

    private static boolean validID(String amount) {
        try {
            int aux = Integer.parseInt(amount);
            if(aux <= 0) {
                System.out.println("Invalid account id.");
                System.out.print(menu);
                return false;
            }
        }
        catch(NumberFormatException nfe) {
            System.out.println("Invalid format of inserted destiny account id.");
            System.out.print(menu);
            return false;
        }
        return true;
    }

    private static boolean validArgumentsNumber(String[] args, int number) {
        if(args.length <= number) {
            System.out.println("Invalid Argument Number.");
            System.out.print(menu);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

        if(args.length < 1) {
            System.out.println("Invalid argument number.\n Please insert the Group Name.");
        }
        else {
            BufferedReader brConsole = null;
            String groupName = args[0];
            Stub account_manager = new Stub(groupName);
            try {
                brConsole = new BufferedReader(new InputStreamReader(System.in));

                boolean shutdown = false;
                menu = account_manager.printMenu();
                System.out.print(menu);

                while (!shutdown) {
                    String option = brConsole.readLine().trim();
                    String[] parts = option.split(" ");

                    switch (parts[0]) {
                        case "LOGIN":
                            if (validArgumentsNumber(parts, 1)) {
                                if (validID(parts[1])) {
                                    account_manager.login(Integer.parseInt(parts[1]));
                                }
                            }
                            break;

                        case "CREATE":
                            if (validArgumentsNumber(parts, 1)) {
                                if (validPositiveValue(parts[1])) {
                                    account_manager.createAccount(Integer.parseInt(parts[1]));
                                }
                            }
                            break;

                        case "MOVE":
                            if (validArgumentsNumber(parts, 1)) {
                                if (validValue(parts[1])) {
                                    account_manager.movement(Integer.parseInt(parts[1]));
                                }
                            }
                            break;

                        case "TRANSF":
                            if (validArgumentsNumber(parts, 2)) {
                                if (validID(parts[1]) && validPositiveValue(parts[2])) {
                                    account_manager.transference(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                                }
                            }
                            break;

                        case "BAL":
                            account_manager.balance();
                            break;

                        case "HIST":
                            if (validArgumentsNumber(parts, 1)) {
                                if (validPositiveValue(parts[1])) {
                                    account_manager.history(Integer.parseInt(parts[1]));
                                }
                            }
                            break;

                        case "LOGOUT":
                            account_manager.logout();
                            break;

                        case "EXIT":
                            account_manager.exit();
                            shutdown = true;
                            System.out.println("Goodbye !!");
                            break;

                        case "HELP":
                            System.out.println("Write in console the keywords described on Menu (Uppercase Words)");
                            System.out.println("Examples:");
                            System.out.println("\tCREATE 100");
                            System.out.println("\tMOVE -30");
                            System.out.println("\tTRANSF 1 50");
                            System.out.println("\tBAL\n");
                            System.out.println("\tEXIT\n");
                            System.out.print(menu);
                            break;

                        default:
                            System.out.println("Invalid Option Inserted");
                            System.out.print(menu);
                            break;
                    }
                }


                brConsole.close();

            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    brConsole.close();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
