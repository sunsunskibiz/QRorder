import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KitchenController implements Initializable {
    @FXML
    private TableView<KitchenTable> prepareTable;
    @FXML
    private TableView<KitchenTable> serveTable;
    private ArrayList<String> prepare;
    private ArrayList<String> serve;

    public void initialize(URL url, ResourceBundle rb) {
        // Prepare table
        prepare = new ArrayList<>();
        TableColumn tableNO = new TableColumn("TABLE NO.");
        TableColumn order = new TableColumn("ORDER");
        prepareTable.getColumns().addAll(tableNO, order);
        tableNO.setCellValueFactory(new PropertyValueFactory<KitchenTable,String>("tableNO"));
        order.setCellValueFactory(new PropertyValueFactory<KitchenTable,String>("order"));

        // Served table
        serve = new ArrayList<>();
        TableColumn tableNOSrv = new TableColumn("TABLE NO.");
        TableColumn orderSrv = new TableColumn("ORDER");
        serveTable.getColumns().addAll(tableNOSrv, orderSrv);
        tableNOSrv.setCellValueFactory(new PropertyValueFactory<KitchenTable,String>("tableNO"));
        orderSrv.setCellValueFactory(new PropertyValueFactory<KitchenTable,String>("order"));
        // Fetch email every 5 second
        Runnable runnable = new Runnable() {
            public void run() {
                // task to run goes here
                ReceiveEmail("smtp.gmail.com", 993, "cafeone.kitchen@gmail.com", "Cafeone2019");
                System.out.println("===========================NEW=================================");
                fillPrepareTable();
                fillServeTable();
//                prepare.clear();
            }
        };
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);
    }

    void fillServeTable() {
        final ObservableList<KitchenTable> data = FXCollections.observableArrayList(
                new KitchenTable("01", "CHOCOLATE MUD BROWNIE")
        );
        serveTable.setItems(data);
    }

    void fillPrepareTable() {
        ArrayList<KitchenTable> pp = new ArrayList<>();
        for (String i : prepare) {
            String table = i.substring(0, 2);
            String menu = i.substring(2);
            pp.add(new KitchenTable(table, menu));
        }
        ObservableList<KitchenTable> data = FXCollections.observableArrayList(pp);
        prepareTable.setItems(data);
    }

    void ReceiveEmail(String host, int port, String username, String password) {
        try {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", host);
            prop.put("mail.smtp.port", port);
            prop.put("mail.smtp.ssl.trust", host);
            Session session = Session.getDefaultInstance(prop);

            Store store = session.getStore("imaps");
            store.connect(host, username, password);

            // Create folder
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);

            // Fetch massage from folder
            Message[] messages = folder.getMessages();

            // Move to folder test
            Folder recieved = store.getFolder("recieved");
            recieved.open(Folder.READ_WRITE);
            folder.copyMessages(messages, recieved);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message individualmsg = messages[i];
                System.out.println("==========================Print individual messages=============================");
                System.out.println("No# " + (i + 1));
                System.out.println("Email Subejct: " + individualmsg.getSubject());
                System.out.println("Sender: " + individualmsg.getFrom()[0]);

                String contentType = individualmsg.getContentType();
                String messageContent = "";

                if (contentType.contains("multipart")) {
                    Multipart multiPart = (Multipart) individualmsg.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        messageContent = part.getContent().toString();
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = individualmsg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                } else if (contentType.contains("image/jpeg")) {
                    System.out.println("--------> image/jpeg");
                }
                System.out.println("Content: " + messageContent);

                // Add in ArrayList prepare
                String tableNO = individualmsg.getSubject().substring(1, 3);
                String[] arr = messageContent.split("\n");
                for (String s : arr) {
                    System.out.println(s);
                    String ss = s.substring(0, s.length()-2);
                    String sss = tableNO + ss;
                    if (prepare.indexOf(sss) == -1) {
                        prepare.add(sss);
                    }
                }

                // set the DELETE flag to true
                individualmsg.setFlag(Flags.Flag.DELETED, true);
                System.out.println("Marked DELETE for message: " + individualmsg.getSubject());
            }
            // Close all the objects
            folder.close(false);
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
