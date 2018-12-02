package Backup;


import Account.Account;
import Account.AccountManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by db on 13/05/16.
 */
public class AccountManagerBackup implements Serializable{

    private String filepath;

    public AccountManagerBackup(String filepath) {
        this.filepath = filepath;
        createFolder();
        initialize();
    }

    public AccountManagerBackup(String filepath, AccountManager am) {
        this.filepath = filepath;
        createFolder();
        writeAccountManager(am);
    }

    private void createFolder() {
        File theDir = new File("../backups");
        if (!theDir.exists()) {
            try{
                theDir.mkdir();
            }
            catch(SecurityException se){
                System.out.println("Couldn't create backups folder!!");
                Logger.getLogger(AccountManagerBackup.class.getName()).log(Level.SEVERE, null, se);
            }
        }
    }

    public void initialize() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element accountManager = doc.createElement("AccountManager");
            doc.appendChild(accountManager);

            Element id_counter = doc.createElement("id_counter");
            id_counter.appendChild(doc.createTextNode(String.valueOf(0)));
            accountManager.appendChild(id_counter);

            Element accounts = doc.createElement("Accounts");
            accountManager.appendChild(accounts);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));

            transformer.transform(source, result);

        } catch(ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(AccountManagerBackup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addAccount(int account_id, int balance, String historyUpdate) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            Node id_counter = doc.getDocumentElement().getFirstChild();
            id_counter.setTextContent(String.valueOf(account_id));

            NodeList accounts = doc.getElementsByTagName("Accounts");

            Element account = doc.createElement("Account");
            account.setAttribute("id", String.valueOf(account_id));
            accounts.item(0).appendChild(account);

            Element balanceElement = doc.createElement("Balance");
            balanceElement.appendChild(doc.createTextNode(String.valueOf(balance)));
            account.appendChild(balanceElement);

            Element historyElement = doc.createElement("History");
            account.appendChild(historyElement);

            Element historyEntry = doc.createElement("H0");
            historyEntry.appendChild(doc.createTextNode(historyUpdate));
            historyElement.appendChild(historyEntry);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

        } catch(ParserConfigurationException | TransformerException | SAXException | IOException ex) {
            Logger.getLogger(AccountManagerBackup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void movement(int account_id, int balance, String historyUpdate) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            NodeList accounts = doc.getElementsByTagName("Account");
            Node account = accounts.item(account_id-1);
            if (account.getNodeType() == Node.ELEMENT_NODE) {
                Element accountElement = (Element) account;
                accountElement.getElementsByTagName("Balance").item(0).setTextContent(String.valueOf(balance));

                Node historyNode = accountElement.getElementsByTagName("History").item(0);
                Element history = doc.createElement("H"+historyNode.getChildNodes().getLength());
                history.appendChild(doc.createTextNode(historyUpdate));
                historyNode.appendChild(history);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

        } catch(ParserConfigurationException | TransformerException | SAXException | IOException ex) {
            Logger.getLogger(AccountManagerBackup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void writeAccountManager(AccountManager am) {

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element accountManager = doc.createElement("AccountManager");
            doc.appendChild(accountManager);

            Element id_counter = doc.createElement("id_counter");
            id_counter.appendChild(doc.createTextNode(String.valueOf(am.getId_counter())));
            accountManager.appendChild(id_counter);

            Element accounts = doc.createElement("Accounts");
            accountManager.appendChild(accounts);

            for(Account acc : am.getAccounts().values()) {
                Element account = doc.createElement("Account");
                account.setAttribute("id", String.valueOf(acc.getId()));
                accounts.appendChild(account);

                Element balance = doc.createElement("Balance");
                balance.appendChild(doc.createTextNode(String.valueOf(acc.getBalance())));
                account.appendChild(balance);

                int i = 0;
                Element history = doc.createElement("History");
                account.appendChild(history);
                for(String hist: acc.getHistory()) {
                    Element h = doc.createElement("H"+i);
                    h.appendChild(doc.createTextNode(hist));
                    history.appendChild(h);
                    i++;
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));

            transformer.transform(source, result);

        } catch(ParserConfigurationException | TransformerException ex) {
            Logger.getLogger(AccountManagerBackup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AccountManager readAccountManager() {
        AccountManager am = new AccountManager();
        try {
            File fXmlFile = new File(filepath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            Node id_counter = doc.getDocumentElement().getFirstChild();
            am.setId_counter(Integer.parseInt(id_counter.getTextContent()));

            NodeList accounts = doc.getElementsByTagName("Account");
            for(int n = 0; n < accounts.getLength(); n++) {
                Node accountNode = accounts.item(n);

                if (accountNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element accountElement = (Element) accountNode;
                    int account_id = Integer.parseInt(accountElement.getAttribute("id"));
                    int balance = Integer.parseInt(accountElement.getElementsByTagName("Balance").item(0).getTextContent());

                    NodeList history = (NodeList) accountElement.getElementsByTagName("History").item(0);
                    ArrayList<String> historyAux = new ArrayList<>();

                    for(int nh = 0; nh < history.getLength(); nh++) {
                        Node historyNode = history.item(nh);
                        if (historyNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element hist = (Element) historyNode;
                            historyAux.add(hist.getTextContent());
                        }
                    }
                    am.addAccount(account_id, balance, historyAux);
                }
            }


        } catch(ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(AccountManagerBackup.class.getName()).log(Level.SEVERE, null, ex);
        }

        return am;
    }
}
