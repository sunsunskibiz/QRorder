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
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController implements Initializable {
    @FXML
    private TableView<Table> mytable;

    private ArrayList<String> order;
    String[] arrOrdered;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final File folder = new File("D:\\newProject\\out\\now");
        order = new ArrayList<>();
        TableColumn table = new TableColumn("Table");
        TableColumn time = new TableColumn("Time");
        mytable.getColumns().addAll(table, time);
        table.setCellValueFactory(new PropertyValueFactory<OrderTable,String>("table"));
        time.setCellValueFactory(new PropertyValueFactory<OrderTable,String>("time"));

        // Fetch email every 5 second
        Runnable runnable = new Runnable() {
            public void run() {
                // task to run goes here
                listFilesForFolder(folder);
                ReceiveEmail("smtp.gmail.com", 993, "cafeone.official@gmail.com", "Cafeone2019");
                fillTable();
            }
        };
        ScheduledExecutorService service = Executors
                .newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);
    }

    void fillTable() {
        ArrayList<Table> ot = new ArrayList<>();
        for (int i =0; i<order.size(); i++) {
            String tableNO = order.get(i).substring(1,3);
            String tme = order.get(i).substring( order.get(i).length()-10, order.get(i).length()-4);
            String t = tme.substring(0, tme.length()-4) + " : " + tme.substring(2, tme.length()-2) + " : " + tme.substring(4);
            ot.add(new Table(tableNO, t));
        }
        ObservableList<Table> data = FXCollections.observableArrayList(ot);


        mytable.setItems(data);
    }
    @FXML
    public void changeToScan1(ActionEvent e) throws IOException {
        Parent scan1SceneParent = FXMLLoader.load(getClass().getResource("scan1.fxml"));
        Scene scan1Scene = new Scene(scan1SceneParent, 800, 600);

        // get Stage information
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();

        window.setScene(scan1Scene);
        window.show();
    }

    public void listFilesForFolder(final File folder) {
        order.clear();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                order.add(fileEntry.getName());
            }
        }
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

//            // Move to folder test
//            Folder recieved = store.getFolder("recieved");
//            recieved.open(Folder.READ_WRITE);
//            folder.copyMessages(messages, recieved);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message individualmsg = messages[i];
                System.out.println("==========================From main=============================");
                System.out.println("==========================Print individual messages=============================");
                System.out.println("No# " + (i + 1));
                System.out.println("Email Subejct: " + individualmsg.getSubject());
                System.out.println("Sender: " + individualmsg.getFrom()[0]);

//                // set the DELETE flag to true
//                individualmsg.setFlag(Flags.Flag.DELETED, true);
//                System.out.println("Marked DELETE for message: " + individualmsg.getSubject());
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
