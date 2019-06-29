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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class Scan2Controller implements Initializable {
    @FXML
    private TableView<OrderTable> table;
    @FXML
    private ImageView currentFrame;

    private ArrayList<String> listFile;
    ObservableList<OrderTable> data;
//    String[] arrOrdered = {"DARK_CHOCOLATE_PRAPPE", "FIGGY_PUDDING", "HONEY_TOAST"};
    String[] arrOrdered;
    String fileName;
    Image img;

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
        listFile = new ArrayList<>();
        TableColumn order = new TableColumn("Order");
        table.getColumns().addAll(order);
        loaddataFromScan1();

        order.setCellValueFactory(new PropertyValueFactory<OrderTable,String>("order"));
        table.setItems(data);

        try {
            FileInputStream inputstream = new FileInputStream("out/002.jpg");
            Image image = new Image(inputstream);
            currentFrame.setImage(image);
        } catch (IOException e) {}
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
    public void backToMain(ActionEvent e) throws IOException {
        Parent mainSceneParent = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene mainScene = new Scene(mainSceneParent, 800, 600);

        // get Stage information
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();

        window.setScene(mainScene);
        window.show();
    }

    @FXML
    public void ok(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Scan1Controller scan1Controller = loader.getController();
        String url = scan1Controller.url;
        String tableNO = url.substring(url.indexOf("T"));
        Date date= new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddhhmmss");
        String time = ft.format(date);
        fileName = tableNO + "_" + time;
        String pathFolder = "out/now/";
        String pathName = pathFolder + fileName;
        String predicate = "http://cafeone.com#";
        String fullURL = "http://cafeone.com/" + tableNO + "/" + time;

        // Send Email
        SendEmail("smtp.gmail.com", 587, "cafeone.official@gmail.com", "Cafeone2019", "cafeone.kitchen@gmail.com" );
        System.out.println("------------- Email send ----------------");


        // contain to RDF file
        // Object mean
        //      - Ordered => that Menu is ordered.
        //      - Served => that Menu  is served.
        //      - Paid => that Menu is paid Or this RDF file is billed.

        //  Find file exist
        String existFile = null;
        listFilesForFolder(new File(pathFolder));
        for (int j = 0; j<listFile.size(); j++) {
            String tbNO = listFile.get(j).substring(1, 3);
            System.out.println("tbNO : " + tbNO);
            if (tbNO.equals(tableNO.substring(1))) {
                existFile = pathFolder + listFile.get(j);
                System.out.println("File Exist");
                break;
            }
        }

        if (existFile != null) {
            Rdf rdf = new Rdf();
            rdf.prepareAdd(existFile, fullURL);
            for (int j=0; j<arrOrdered.length; j++) {
                rdf.addStatement(fullURL, predicate + arrOrdered[j] + ",1", "Ordered");
            }
            if (rdf.deleteRdfFile(existFile)) {
                rdf.writeRDF(pathName);
                System.out.println("------------- Write new RDF file ----------------");
            } else {
                System.out.println("Write new RDF file failed");
            }
        } else {
            Rdf table_no = new Rdf();
            table_no.createModel();
            for (int j=0; j<arrOrdered.length; j++) {
                table_no.addStatement(fullURL, predicate + arrOrdered[j] + ",1", "Ordered");
            }

            table_no.writeRDF(pathName);
            System.out.println("------------- Write RDF file ----------------");
        }

        // Go to main scene
        Parent mainSceneParent = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene mainScene = new Scene(mainSceneParent, 800, 600);
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.setScene(mainScene);
        window.show();
    }

    void SendEmail(String host, int port, String username, String password, String to) {
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

    public void listFilesForFolder(final File folder) {
        listFile.clear();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                listFile.add(fileEntry.getName());
            }
        }
    }

}
