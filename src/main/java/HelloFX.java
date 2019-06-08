import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.awt.image.BufferedImage;

import org.apache.jena.atlas.io.IO;
import org.opencv.core.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;


import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.awt.geom.AffineTransform;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Properties;

//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
//import com.google.zxing.Result;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.ResultPoint;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class HelloFX extends Application {

    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    int waitCnt = 0;
    String[][] arrMenu = {
            {"PINKLEMONADE", "39", "x", "y"}, {"MATCHA FRAPPE", "49", "x", "y"}, {"WHITE CHOC MACCHIATO", "49", "x", "y"}, {"LYNCHEE JUICE", "29", "x", "y"},
            {"STRAWBERRY MILLE CREPR", "69", "x", "y"}, {"WARM CHOCOLATE CHIP PANOOKIE", "79", "x", "y"}, {"MATHCA MILLE CREPE", "169", "x", "y"}, {"DARK CHOCOLATE PRAPPE", "49", "x", "y"},
            {"HONEY TOAST", "109", "x", "y"}, {"FIGGY PUDDING", "89", "x", "y"}, {"CHOCOLATE MUD BROWNIE", "79", "x", "y"}, {"TWO-TONE KAKIGORI", "139", "x", "y"}};

    SimpleDateFormat fmfn = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", new Locale("uk","UK"));

    BufferedImage img, img2;
    java.awt.Point[] qrRct = null;
    ResultPoint[] aResPnt = null;
    Point aPnt1, aPnt2, aPnt3, aPnt4, aPnt5;
    BufferedImage[] aMk = new BufferedImage[12];

    BufferedImage mat2Img(Mat m) {
        int wd2 = m.cols();
        int hg2 = m.rows();
        int type = BufferedImage.TYPE_3BYTE_BGR;
        if(m.channels() == 1) type = BufferedImage.TYPE_BYTE_GRAY;
        BufferedImage img2 = new BufferedImage(wd2, hg2, type);
        WritableRaster ras2 = img2.getRaster();
        DataBufferByte buf2 = (DataBufferByte) ras2.getDataBuffer();
        byte[] dat2 = buf2.getData();
        m.get(0, 0, dat2);
        return img2;
    }

    void testCAM() {
        Mat mat = new Mat();
        VideoCapture cam = new VideoCapture(0);
        if (cam.isOpened()) {
            img2 = null;
            if (cam.read(mat)) {
                int w = mat.cols();
                int h = mat.rows();
                qrRct = null;
                aPnt1 = aPnt2 = aPnt3 = null;
                img = mat2Img(mat);
                System.out.println("W : " + mat.cols() + ", H : " + mat.rows());
            }
        } else {
            System.out.println("Not found cam");
        }
        cam.release();

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
        System.out.println("Set Properties and Session");

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("chantapat.sun@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("cafeone.official@gmail.com"));
            message.setSubject("Try send email in helloFX ja");

            String msg = "This is my first email using JavaMailer for use <http://cafeone/03/0812034283> <http://cafeone#cake> \"Order\"";
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email has send.");
        } catch (Exception e) {
            e.printStackTrace();
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
            folder.open(Folder.READ_ONLY);

            // Fetch massage from folder
            Message[] messages = folder.getMessages();

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

    @Override
    public void start(Stage stage) throws IOException {
//        String javaVersion = System.getProperty("java.version");
//        String javafxVersion = System.getProperty("javafx.version");
//        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
//        Scene scene = new Scene(new StackPane(l), 640, 480);
//        stage.setScene(scene);
        Parent root = FXMLLoader.load(getClass().getResource("/page3.fxml"));
        stage.setTitle("QR Order");
        stage.setScene(new Scene(root, 800, 600));

        stage.show();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World");


//        System.out.println("======================= Connect camera ============================");
//        HelloFX hel = new HelloFX();
//        hel.testCAM();

//        System.out.println("======================= Write RDF ============================");
//        String fileName = "testRDF";
//        String pathName = "out/" + fileName;
//
//        String url = "http://cafeone.com/";
//        String predicate = "http://cafeone.com#";
//
//        Rdf rdf = new Rdf();
//        rdf.createModel();
//
//        rdf.addStatement(url, predicate + "Lemon", "Ordered");
//        rdf.writeRDF(pathName);
//        System.out.println("Finished");

//        System.out.println("====================== Read RDF ===============================");
//        String destPath = "out/testRDF.ttl";
//        Rdf rdfRead = new Rdf();
//        rdfRead.listMenuStatus(destPath);
//        System.out.println("Read finished");

//        System.out.println("======================= Send Email ============================");
//        HelloFX helloFXEmail = new HelloFX();
//        helloFXEmail.SendEmail("smtp.gmail.com", 587, "cafeone.official@gmail.com", "cafeOne2019" );
//        System.out.println("Send email success");

        System.out.println("======================= Recieve Email ============================");
        HelloFX helloFXEmail = new HelloFX();
        helloFXEmail.ReceiveEmail("smtp.gmail.com", 993, "cafeone.official@gmail.com", "cafeOne2019");
        System.out.println("Receive email finished");

        launch();
    }

}