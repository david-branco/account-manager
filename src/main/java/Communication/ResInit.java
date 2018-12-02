package Communication;

import Account.AccountManager;

/**
 * Created by db on 12/05/16.
 */
public class ResInit extends Operation {

    private AccountManager am;

    public ResInit(AccountManager am) {
        super();
        this.am = new AccountManager(am);
    }

    public AccountManager getAccountManager() {
        return new AccountManager(am);
    }

}
