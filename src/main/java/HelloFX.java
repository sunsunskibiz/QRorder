import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Hashtable;

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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.ResultPoint;
import com.google.zxing.*;
import com.google.zxing.common.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.*;
import com.google.zxing.qrcode.decoder.*;
import com.google.zxing.client.j2se.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfStamper;


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
    /////////////////////////////// Part 1.5 //////////////////////////////////////
    void crop() throws NotFoundException, IOException {
        ArrayList<String> arrLineOrderedMenu = new ArrayList<String>();

        // Create Menu
        Menu[] menu = new Menu[10];
        menu[0] = new Menu(1, "HONEY TOAST", 109);
        menu[1] = new Menu(2, "FIGGY PUDDING", 89);
        menu[2] = new Menu(3, "STRAWBERRY MILLE CREPE", 69);
        menu[3] = new Menu(4, "WARM CHOCOLATE CHIP PANOOKIE", 79);
        menu[4] = new Menu(5, "CHOCOLATE MUD BROWNIE", 79);
        menu[5] = new Menu(6, "TWO-TONE KAKIGORI", 139);
        menu[6] = new Menu(7, "MATCHA MILLE CREPE", 169);
        menu[7] = new Menu(8, "DARK CHOCOLATE FRAPPE", 49);
        menu[8] = new Menu(9, "PINK LEMONADE", 39);
        menu[9] = new Menu(10, "MATCHA FRAPPE", 49);

        String pathImage = "cafeOne2.jpg";
        // Detect QRcode
        File fImg = new File(pathImage);
        System.out.println("FILE: " + fImg + " : " + fImg.exists());
        BufferedImageLuminanceSource image = new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(fImg)));
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(image));


        String tmp = "1234567890";
        int width = 250;
        String nameFile = "number1";
//        QRcode.create(tmp,width,nameFile);


        int imgWitdth = image.getWidth();
        int imgHeight = image.getHeight();


        Result qr = new MultiFormatReader().decode(binaryBitmap);
        String url = qr.getText();
        ResultPoint[] pts = qr.getResultPoints();
        System.out.println("TEXT: " + url);

        // Extract conner QRcode
        float xBottomLeft = pts[0].getX();
        float yBottomLeft = pts[0].getY();
        float xTopLeft = pts[1].getX();
        float yTopLeft = pts[1].getY();
        float xTopRight = pts[2].getX();

        for (ResultPoint pt : pts) {
            System.out.println(" " + pt.getX() + "," + pt.getY());
        }

        // 1 cent = how many pixels?
        float widthQRcode = xTopRight - xBottomLeft;
        float pixelCent = widthQRcode / 2;
        float centerToEdge = widthQRcode / 4;
        float xImageToEdgeQRcode = xBottomLeft - centerToEdge;
        float yMenu9 = yBottomLeft - centerToEdge;

        System.out.println("xBottomLeft : " + xBottomLeft);
        System.out.println("yBottomLeft : " + yBottomLeft);
        System.out.println("xTopLeft : " + xTopLeft);
        System.out.println("yTopLeft : " + yTopLeft);
        System.out.println("xTopRight : " + xTopRight);
        System.out.println("widthOqcode : " + widthQRcode);
        System.out.println("pixelCent : " + pixelCent);

        Mat imageOriginal;
        imageOriginal = Imgcodecs.imread(pathImage);

        // Crop Image
        Mat imageCrop[] = new Mat[10];
        int xIndexMenu = 0;
        int yIndexMenu = 0;

        // Crop and show 10 menu
        for (int i = 0; i < imageCrop.length; i++) {
            if (i % 2 == 0) {
                xIndexMenu = 13;
                if (i != 0) {
                    yIndexMenu++;
                }
            } else {
                xIndexMenu = 5;
            }
            Rect rectCrop = new Rect((int) (xImageToEdgeQRcode + (pixelCent * xIndexMenu)), (int) (yMenu9 - (yIndexMenu * (pixelCent * 3))), (int) pixelCent, (int) pixelCent);
            imageCrop[i] = imageOriginal.submat(rectCrop);


            // Converting the image to gray scale and saving it in the dst matrix
            // Creating the empty destination matrix
            Mat grayPicture = new Mat();
            Imgproc.cvtColor(imageCrop[i], grayPicture, Imgproc.COLOR_RGB2GRAY);
            Mat binaryPicture = new Mat();
            Imgproc.threshold(grayPicture, binaryPicture, 127, 255, Imgproc.THRESH_BINARY);

            System.out.println("Image Loaded : " + (10 - i));
            int allPixels = binaryPicture.rows() * binaryPicture.cols();
            System.out.println("NO. all pixels => " + allPixels);

            // Valid
            // Get value pixel
            int black = 0;
            for (int row = 0; row < binaryPicture.rows(); row++) {
                for (int col = 0; col < binaryPicture.cols(); col++) {
                    double[] check = binaryPicture.get(row, col);
                    int value = (int) check[0];
                    // count NO. black pixel
                    if (value == 0) {
                        black++;
                    }
                }
            }
            System.out.println("NO. black pixels => " + black);

            // Order that menu or Not (There are black pixels more than 10% of all pixels of Image)
            float percentBlack = (float) black / (float) allPixels * 100;
            System.out.println("Percent of Black pixel => " + percentBlack);
            if (percentBlack > 10) {
                System.out.println("!!!!!This menu is ordered.!!!!!");
                String menuOrdered = "Menu_NO." + (10 - i);
                System.out.println("MenuOrdered => " + menuOrdered);
                arrLineOrderedMenu.add(menuOrdered);
            }

            // Show Image
            printImage(binaryPicture);
            System.out.println();

        }
//        Imgcodecs.imwrite("blank.jpg", binaryPicture);

        System.out.println("===================================================");
    }

    void printImage(Mat image) throws IOException {
        //Encoding the image
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, matOfByte);

        //Storing the encoded Mat in a byte array
        byte[] byteArray = matOfByte.toArray();

        //Preparing the Buffered Image
        InputStream in = new ByteArrayInputStream(byteArray);
        BufferedImage bufImage = ImageIO.read(in);

        //Instantiate JFrame
        JFrame frame = new JFrame();

        //Set Content to the JFrame
        frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
        frame.pack();
        frame.setVisible(true);
    }

    /////////////////////////////// Part 2 //////////////////////////////////////

    void pdfAddText(PdfContentByte cb, int x, int y, String tx, int sz) throws Exception {
        cb.setFontAndSize(bf, sz);
        cb.beginText();
        cb.setTextMatrix(x, y);
        cb.showText(tx);
        cb.endText();
    }

    BaseFont bf;

    com.itextpdf.text.Image readImage(String fImg) throws Exception {
        com.itextpdf.text.Image image = null;
        return image;
    }

    com.itextpdf.text.Image makeQrImage(String qrid) throws Exception {
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
        BitMatrix bitmtx = writer.encode(qrid, BarcodeFormat.QR_CODE, 400, 400, hints );
        BufferedImage img = MatrixToImageWriter.toBufferedImage(bitmtx);
        com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(img, null);
        return image;
    }

    void pdfLine(PdfContentByte cb, int x1, int y1, int x2, int y2) {
        cb.moveTo(x1, y1);
        cb.lineTo(x2, y2);
        cb.stroke();
    }

    void pdfLine(PdfContentByte cb, int x1, int y1, int x2, int y2, float wd) {
        cb.setLineWidth(wd);
        cb.moveTo(x1, y1);
        cb.lineTo(x2, y2);
        cb.stroke();
    }

    @SuppressWarnings({"unchecked","deprecation"})
    public void createQRnote() throws Exception {
        if(bf==null) bf = BaseFont.createFont("thaifont.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        int p = 0;
        Document doc = new Document(PageSize.A4, 0, 0, 0, 0);
        File fNT = new File("out/cafeoneNote.pdf");
        File fQR = new File("out/qrcode.png");
        FileOutputStream fout = new FileOutputStream(fNT);
        PdfWriter pdfwr= PdfWriter.getInstance(doc, fout);
        doc.open();

        PdfContentByte cb= pdfwr.getDirectContent();
        int mxp = 12, mxc = 2, mxr = 2;
        int ox = 2, oy = 2;
        int wd = 209, hg = 295;
        int w1 = wd, h1 = hg;
        hg = w1 * 2;
        wd = h1;

        int[] aPageNo = new int[mxp * mxc * mxr];
        for(p=1; p<=mxp; p++) {
            int pp = p;
            for(int i=0; i<mxc; i++) {
                for(int j=0; j<mxr; j++) {
                    int pn = (p-1) * (mxc*mxr) + j*mxc + i;
                    aPageNo[pn] = pp;
                    pp += mxp;
                }
            }
        }

        for(p=1; p<=mxp; p++) {
            doc.newPage();
            // each page
            for(int i=0; i<mxc; i++) {
                for(int j=0; j<mxr; j++) {
                    int pn = (p-1)*(mxc*mxr) + (1-j)*mxc + i;
                    int x = ox + i*wd + 2;
                    int y = oy + j*hg + hg * 90 / 100;
                    int yf = 5;

                    String qrid = "cafeone";
                    String pgnos = "T"+(aPageNo[pn]-1);
                    com.itextpdf.text.Image image = makeQrImage(qrid + pgnos);
                    cb.addImage(image, 80, 0, 0, 80, x, y-60);
                    int pgno = aPageNo[pn];
                    int picn = pgno;
                    String pg = (picn<10)? "0"+picn : ""+picn;
                    File ani = new File("out/note-cim-anim/Slide"+pg+".jpg");
                    if(ani.exists()) {
                        BufferedImage animg = ImageIO.read(ani);
                        double hpw = (double) animg.getHeight() / (double) animg.getWidth() / 2.0f;
                        animg = animg.getSubimage(0,0, animg.getWidth(), animg.getHeight()/2 - 17);
                        com.itextpdf.text.Image animgx = com.itextpdf.text.Image.getInstance(animg,null);
                        int hg0 = (int) ((double) wd * hpw);
                        cb.addImage(animgx, wd, 0, 0, hg0, x, y-hg+hg0/2);
                    }
                    int lsp = 20, lcn = 20;
                    cb.setLineWidth(0.05f);
                    // BOX
                    for(int r=0; r<=15; r++) {
                        int x1 = ox + i * wd + 1;
                        int y1 = oy + j * hg + r * lsp + lsp - yf;
                        if(r>=3) {
                            if (r == 3 || r ==15) {
                                pdfLine(cb, x1, y1, x1+wd, y1);
                            }
                            if(r>=15) break;
                            cb.rectangle(x1+20, y1-10+12, 15, 15);
                        }
                        if (r < 12) {
                            int le = arrMenu[r][1].length();
                            if (le == 2) {
                                pdfAddText(cb, x1 + wd/2 + 90, y1-16 + 80 + 2, " " + arrMenu[r][1] + " -", 14);
                            } else {
                                pdfAddText(cb, x1 + wd/2 + 90, y1-16 + 80 + 2, arrMenu[r][1] + " -", 14);
                            }
                            pdfAddText(cb, x1+50, y1-16 + 80 + 2, arrMenu[r][0], 14);
                        }
                    }
                    pdfAddText(cb, x+10, y - 315, "Note.", 18);

                    pdfAddText(cb, x+80, y-2-yf+9+3, "CafeOne", 32);
                    pdfAddText(cb, x + wd - 30, y-2-yf+9, pgnos, 18);

                    String txt = "0893457890";


                    pdfAddText(cb, x + 80, y-25-yf+17, txt, 18);

                    String email = "cafeone.offical@gmail.com";
                    String openOff = "Open: 10.00  Close: 22.00 ";

                    pdfAddText(cb, x + 80, y-25-15-yf+15, email, 16);
                    pdfAddText(cb, x + 80, y-25-yf-16, openOff, 16);
                }
            }
            // vertical border lines
            for(int i=0; i<=mxc; i++) {
                for(int j=1; j>=0; j--) {
                    pdfLine(cb, ox + i*wd+1, 0, ox + i*wd+1, 900, 0.2f); } }

            // horizontal border lines
            for(int i=0; i<mxc; i++) { for(int j=2; j>=0; j--) {
                pdfLine(cb, 0, oy + 1+j*hg, 900, oy + 1+j*hg, 0.2f); } }

        }
        doc.close();
        fout.flush();
        fout.close();
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
        Parent root = FXMLLoader.load(getClass().getResource("/FXHelloCV.fxml"));
        stage.setTitle("QR Order");
        stage.setScene(new Scene(root, 800, 600));

        stage.show();
    }

    public static void main(String[] args) throws Exception {
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

//        System.out.println("======================= Recieve Email ============================");
//        HelloFX helloFXEmail = new HelloFX();
//        helloFXEmail.ReceiveEmail("smtp.gmail.com", 993, "cafeone.official@gmail.com", "cafeOne2019");
//        System.out.println("Receive email finished");

//        System.out.println("======================= Create QRnote ============================");
//        HelloFX helloFXNOTE = new HelloFX();
//        helloFXNOTE.createQRnote();
//        System.out.println("Create QRnote finished");

//        System.out.println("======================= Crop QRnote ============================");
//        HelloFX helloFXNOTE = new HelloFX();
//        helloFXNOTE.crop();
//        System.out.println("Crop QRnote finished");

        launch();
    }

}