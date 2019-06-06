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

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
//import com.google.zxing.Result;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.ResultPoint;



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

    @Override
    public void start(Stage stage) throws IOException {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/testFXML.fxml"));
//        stage.setTitle("QR Order");
//        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World");


        System.out.println("======================= Connect camera ============================");
        HelloFX hel = new HelloFX();
        hel.testCAM();

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





        launch();
    }

}