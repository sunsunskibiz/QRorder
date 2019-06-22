import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Hashtable;

import org.opencv.core.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.*;
import com.google.zxing.client.j2se.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.PageSize;


public class HelloFX extends Application {

    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    final static String[][] arrMenu = {
            {"PINKLEMONADE", "39", "x", "y"}, {"MATCHA_FRAPPE", "49", "x", "y"}, {"WHITE_CHOC_MACCHIATO", "49", "x", "y"}, {"LYNCHEE_JUICE", "29", "x", "y"},
            {"STRAWBERRY_MILLE_CREPR", "69", "x", "y"}, {"WARM_CHOCOLATE_CHIP_PANOOKIE", "79", "x", "y"}, {"MATHCA_MILLE_CREPE", "169", "x", "y"}, {"DARK_CHOCOLATE_PRAPPE", "49", "x", "y"},
            {"HONEY_TOAST", "109", "x", "y"}, {"FIGGY_PUDDING", "89", "x", "y"}, {"CHOCOLATE_MUD_BROWNIE", "79", "x", "y"}, {"TWO-TONE_KAKIGORI", "139", "x", "y"}};

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

    static void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                System.out.println(fileEntry.getName());
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        stage.setTitle("QR Order");
        stage.setScene(new Scene(root, 800, 600));

        stage.show();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World");

//        System.out.println("====================== Read RDF ===============================");
//        String destPath = "out/testRDF.ttl";
//        Rdf rdfRead = new Rdf();
//        rdfRead.listMenuStatus(destPath);
//        System.out.println("Read finished");

//        System.out.println("======================= Create QRnote ============================");
//        HelloFX helloFXNOTE = new HelloFX();
//        helloFXNOTE.createQRnote();
//        System.out.println("Create QRnote finished");

        launch();
    }
}