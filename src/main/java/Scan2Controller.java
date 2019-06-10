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
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class Scan2Controller implements Initializable {
//public class Scan2Controller {
    @FXML
    private TableView<OrderTable> table;

    ObservableList<OrderTable> data;
    String[] arrOrdered;
    String fileName;

    private void loaddataFromScan1() {
        FXMLLoader loader = new FXMLLoader();
        Scan1Controller scan1Controller = loader.getController();
        arrOrdered = scan1Controller.dataToScene2;
        ArrayList<OrderTable> ot = new ArrayList<>();
        for (String i : arrOrdered) {
            ot.add(new OrderTable(i));
        }

        data = FXCollections.observableArrayList(ot);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn order = new TableColumn("Order");
        table.getColumns().addAll(order);
        loaddataFromScan1();

        order.setCellValueFactory(new PropertyValueFactory<OrderTable,String>("order"));
        table.setItems(data);
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

    @FXML
    public void ok(ActionEvent e) throws IOException {
        // Write to rdf file
        // Find file name
        FXMLLoader loader = new FXMLLoader();
        Scan1Controller scan1Controller = loader.getController();
        String url = scan1Controller.url;
        String tableNO = url.substring(url.indexOf("T"));
        Date date= new Date();
        long time = date.getTime();
        fileName = tableNO + "_" + time;
        String pathName = "out/" + fileName;
        String predicate = "http://cafeone.com#";
        String fullURL = "http://cafeone.com/" + tableNO + "/" + time;

        // contain to RDF file
        // Object mean
        //      - Ordered => that Menu is ordered.
        //      - None => that Menu is not ordered.
        //      - Paid => that Menu is paid Or this RDF file is billed.
        Rdf table_no = new Rdf();
        table_no.createModel();
        for (int j=0; j<arrOrdered.length; j++) {
            table_no.addStatement(fullURL, predicate + arrOrdered[j], "Ordered");
        }

        table_no.writeRDF(pathName);
        System.out.println("------------- Write RDF file ----------------");

        SendEmail("smtp.gmail.com", 587, "cafeone.official@gmail.com", "cafeOne2019" );
        System.out.println("------------- Email send ----------------");
    }

    void SendEmail(String host, int port, String username, String password) {
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
            message.setFrom(new InternetAddress("chantapat.sun@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("cafeone.official@gmail.com"));
            message.setSubject(fileName);

            String msg = "";
            for (int i=0; i<arrOrdered.length; i++) {
                msg += arrOrdered[i] + "\n";
            }
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
