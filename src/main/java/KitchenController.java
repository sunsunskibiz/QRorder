import com.sun.mail.imap.IMAPFolder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
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
//        ReceiveEmail("smtp.gmail.com", 993, "cafeone.kitchen7@gmail.com", "cafeone2019");
        ReceiveEmail("smtp.gmail.com", 993, "cafeone.kitchen@gmail.com", "cafeone2019");
        Runnable runnable = new Runnable() {
            public void run() {
                String s = Platform.isFxApplicationThread() ? "UI Thread" : "Background Thread";
                System.out.println("1:In " + s);
                fillPrepareTable();
                fillServeTable();
            }
        };
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 8, TimeUnit.SECONDS);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("===========================NEW Email=================================");
                String s = Platform.isFxApplicationThread() ? "UI Thread" : "Background Thread";
                System.out.println("2:In " + s);
                monitor("smtp.gmail.com", 993, "cafeone.kitchen@gmail.com", "cafeone2019", 5000);
//                monitor("smtp.gmail.com", 993, "cafeone.kitchen7@gmail.com", "cafeone2019", 5000);
            }
        };
        new Thread(task).start();

    }

    void fillServeTable() {
        ArrayList<KitchenTable> sv = new ArrayList<>();
        for (String i : serve) {
            String table = i.substring(0, 2);
            String menu = i.substring(2);
            sv.add(new KitchenTable(table, menu));
        }
        ObservableList<KitchenTable> data = FXCollections.observableArrayList(sv);
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
//                System.out.println("Sender: " + individualmsg.getFrom()[0]);

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
//                System.out.println("Content: " + messageContent);

                // Add in ArrayList prepare
                String tableNO = individualmsg.getSubject().substring(1, 3);
                String[] arr = messageContent.split("\n");
                for (String s : arr) {
                    System.out.println(s);
                    String ss = s.substring(0, s.length()-1);
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

    @FXML
    public void servePressed(ActionEvent e) {
        if (prepareTable.getSelectionModel().getSelectedItem() != null) {
            String s = prepareTable.getSelectionModel().getSelectedItem().getTableNO() + prepareTable.getSelectionModel().getSelectedItem().getOrder();
            serve.add(s);
//            SendEmail("smtp.gmail.com", 587, "cafeone.kitchen2@gmail.com", "Cafeone2019", "cafeone.official3@gmail.com", "served_" + s);
            SendEmail("smtp.gmail.com", 587, "cafeone.kitchen@gmail.com", "Cafeone2019", "cafeone.official@gmail.com", "served_" + s);
            prepare.remove(prepare.indexOf(s));
            System.out.println("Email Send");
        }
    }

    void SendEmail(String host, int port, String username, String password, String to, String content) {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(content);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(content, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backToMain(ActionEvent e) throws IOException {
        Parent mainSceneParent = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene mainScene = new Scene(mainSceneParent, 800, 600);

        // get Stage information
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();

        window.setScene(mainScene);
        window.show();
    }

    public void monitor(String host, int port, String user, String password, int freq) {
        System.out.println("\nTesting monitor\n");
        try {

            Properties prop = new Properties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", host);
            prop.put("mail.smtp.port", port);
            prop.put("mail.smtp.ssl.trust", host);
            Session session = Session.getDefaultInstance(prop);

            Store store = session.getStore("imaps");
            store.connect(host, user, password);

//            // Create folder
//            Folder folder = store.getFolder("INBOX");
//            folder.open(Folder.READ_WRITE);
            // Open a Folder
            Folder folder = store.getFolder("INBOX");
            if (folder == null || !folder.exists()) {
                System.out.println("Invalid folder");
                System.exit(1);
            } else {
                System.out.println("Folder exist");
            }

            folder.open(Folder.READ_WRITE);

            // Add messageCountListener to listen for new messages
            folder.addMessageCountListener(new MessageCountAdapter() {
                public void messagesAdded(MessageCountEvent ev) {
                    Message[] msgs = ev.getMessages();
                    System.out.println("Got " + msgs.length + " new messages");

                    // Just dump out the new messages
                    for (int i = 0; i < msgs.length; i++) {
                        try {
                            System.out.println("-----");
                            System.out.println("Message " + msgs[i].getMessageNumber() + ":");
                            Message individualmsg = msgs[i];

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
                                String ss = s.substring(0, s.length()-1);
                                String sss = tableNO + ss;
                                if (prepare.indexOf(sss) == -1) {
                                    prepare.add(sss);
                                }
                            }

//                            // set the DELETE flag to true
//                            individualmsg.setFlag(Flags.Flag.DELETED, true);
//                            System.out.println("Marked DELETE for message: " + individualmsg.getSubject());
                        } catch (MessagingException mex) {
                            mex.printStackTrace();
                        } catch (IOException e) {

                        }
                    }
                }
            });

            // Check mail once in "freq" MILLIseconds
            boolean supportsIdle = false;
            try {
                if (folder instanceof IMAPFolder) {
                    IMAPFolder f = (IMAPFolder)folder;
                    f.idle();
                    supportsIdle = true;
                }
            } catch (FolderClosedException fex) {
                throw fex;
            } catch (MessagingException mex) {
                supportsIdle = false;
            }
            for (;;) {
                if (supportsIdle && folder instanceof IMAPFolder) {
                    IMAPFolder f = (IMAPFolder)folder;
                    f.idle();
                    System.out.println("IDLE done");
                } else {
                    Thread.sleep(freq); // sleep for freq milliseconds

                    // This is to force the IMAP server to send us
                    // EXISTS notifications.
                    folder.getMessageCount();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
