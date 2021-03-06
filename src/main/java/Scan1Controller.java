import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;

import static org.opencv.imgproc.Imgproc.boundingRect;

public class Scan1Controller {
    @FXML
    private ImageView currentFrame;

    private ScheduledExecutorService timer;
    private VideoCapture capture;
    static String[] dataToScene2;
    static String url;

    private  int countGreenBox = 0;
    private String outpath = "out/cvt.jpg";
    private String outpathBW = "out/bw.jpg";
    private String graypath = "out/gray.jpg";
    private int theadholdstartY = 100;
    private int theadholdRectArea = 100;
    private int diffY = 30;
    private int rangeDuplicateX = 5;
    private int rangeDuplicateY = 5;
    private int rangeY = 7;
    BufferedImage[] aMk = new BufferedImage[12];
    boolean isRunning = true;

    final static String[][] arrMenu = HelloFX.arrMenu;

    public void initialize() {
        this.capture = new VideoCapture();
        this.capture.open(0);

        // is the video stream available?
        if (this.capture.isOpened()) {
            // grab a frame every 33 ms (30 frames/sec)
            Runnable frameGrabber = new Runnable() {
                @Override
                public void run() {
                    System.out.println("Status isRunning from Scan1 : " + isRunning);

                    // effectively grab and process a single frame
                    Mat frame = grabFrame();
                    // convert and show the frame
                    Image imageToShow = Utils.mat2Image(frame);
                    updateImageView(currentFrame, imageToShow);
                }
            };
            this.timer = Executors.newSingleThreadScheduledExecutor();
            this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
        }
        else
        {
            // log the error
            System.err.println("Impossible to open the camera connection...");
        }
    }

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

    private Mat grabFrame() {
        // init everything
        BufferedImage img, img2;
        java.awt.Point[] qrRct = null;
        ResultPoint[] aResPnt = null;
        Point aPnt1, aPnt2, aPnt3, aPnt4, aPnt5;
        BufferedImage[] aMk = new BufferedImage[12];
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened())
        {
            try
            {
                // read the current frame
                this.capture.read(frame);

                // if the frame is not empty, Scan QRorder
                if (!frame.empty())
                {
                    img = mat2Img(frame);

                    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(img)));
                    Result qr = new MultiFormatReader().decode(binaryBitmap);
                    if (qr != null) {
                        url = qr.getText();
                        System.out.println("<++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++>");
                        System.out.println("Scan QRorder from " + url);
//                        if (countGreenBox == 12) {
//                            stopAcquisition();
//                        }
                        aResPnt = qr.getResultPoints();
                        qrRct = null;
                        if(aResPnt.length>=3) {
                            qrRct = new java.awt.Point[aResPnt.length];
                            for (int i = 0; i < aResPnt.length; i++) {
                                ResultPoint pnt = aResPnt[i];
                                qrRct[i] = new java.awt.Point((int) pnt.getX(), (int) pnt.getY());
                            }

                            Mat m = frame;
                            //Imgproc.cvtColor(mat, gray, CV_BGR2GRAY);
                            //threshold( gray, thr, 100,255,THRESH_BINARY );

                            int wd = 400, hg = 500;
                            double m1 = 7.2;
                            double m2 = 9.0;

                            double dx = (aResPnt[2].getX() - aResPnt[1].getX());
                            double dy2 = (aResPnt[0].getY() - aResPnt[1].getY());
                            double ax = (aResPnt[2].getY() - aResPnt[1].getY()) / dx;
                            double ax2 = (aResPnt[0].getX() - aResPnt[1].getX()) / dy2;
                            //ax2 *= 1.5;
                            aPnt1 = new Point(aResPnt[1].getX(), aResPnt[1].getY());
                            aPnt1.x -= dx / 2;
                            aPnt1.y -= dx / 2 * ax + dx / 2;
                            dx *= m1;
                            dy2 *= m2;
                            double dy = dx * ax;
                            double dx2 = dy2 * ax2;
                            aPnt2 = new Point(aPnt1.x + dx, aPnt1.y + dy);
                            aPnt3 = new Point(aPnt1.x + dx2, aPnt1.y + dy2);
                            aPnt4 = new Point(aPnt3.x + dx, aPnt3.y + dy);
                            aPnt5 = new Point(aPnt2.x + dx2, aPnt2.y + dy2);

                            Point[] src1 = new Point[4];
                            src1[0] = new Point(aPnt1.x, aPnt1.y);
                            src1[1] = new Point(aPnt3.x, aPnt3.y);
                            src1[2] = new Point(aPnt2.x, aPnt2.y);
                            src1[3] = new Point(aPnt4.x, aPnt4.y);
                            MatOfPoint2f src = new MatOfPoint2f(src1[0], src1[1], src1[2], src1[3]);

                            Point[] src2 = new Point[4];
                            src2[0] = new Point(0, 0);
                            src2[1] = new Point(0, hg - 1);
                            src2[2] = new Point(wd - 1, 0);
                            src2[3] = new Point(wd - 1, hg);
                            MatOfPoint2f dst = new MatOfPoint2f(src2[0], src2[1], src2[2], src2[3]);

                            Mat warpMat = Imgproc.getPerspectiveTransform(src, dst);
                            Mat destImage = new Mat();
                            Imgproc.warpPerspective(m, destImage, warpMat, new Size(wd, hg));


                            Rect roi;
                            Mat mk;

                            Mat gray = new Mat();
                            Imgproc.cvtColor(destImage, gray, Imgproc.COLOR_RGB2GRAY);
                            Mat bwim = new Mat();
                            Imgproc.threshold(gray, bwim, 127, 255, Imgproc.THRESH_BINARY);
                            bwim = gray;
                            img2 = mat2Img(bwim);
                            File fJpg = new File("out/002.jpg");
                            ImageIO.write(img2, "jpg", fJpg);

                            Imgcodecs imageCodecs = new Imgcodecs();
                            Mat in = imageCodecs.imread("out/002.jpg");
                            Mat bwim2 = new Mat();
                            Mat gray2 = in;
                            ArrayList<String> arrLineOrderedMenu = new ArrayList<String>();
                            String message = "";
                            int total = 0;

//                            Imgproc.cvtColor(in, gray2, Imgproc.COLOR_RGB2GRAY);
                            Imgproc.threshold(gray2, bwim2, 127, 255, Imgproc.THRESH_BINARY);
                            BufferedImage aa = mat2Img(gray2);
                            ImageIO.write(aa, "jpg", new File(graypath));
//
                            Mat cannyOutput = new Mat();
                            Imgproc.Canny(gray2, cannyOutput, 100, 100 * 2);
                            List<MatOfPoint> contours = new ArrayList<>();
                            Mat hierarchy = new Mat();
                            Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
                            int count = 1;
                            int oldStartX = 0;
                            int oldStartY = 0;
                            int nextRangeY = 0;
                            boolean startinLoop = false;

                            for (int i = 0; i < contours.size(); i++) {
                                Rect rect = boundingRect(contours.get(i));
                                double k = (rect.height+0.0)/rect.width;
                                Scalar color = new Scalar(0, 0, 255);

                                if (1.5<k && k<2.5 && rect.area()>theadholdRectArea) {
                                    Imgproc.drawContours(in, contours, i, color, 1, Core.LINE_8, hierarchy, 0, new Point());
                                    int startX = rect.x + 20;
                                    int startY = rect.y;
                                    boolean checkDuplicateStartX;
                                    boolean checkInRangeY;
                                    if (startinLoop) {
                                        checkDuplicateStartX = (ValueRange.of(oldStartX-rangeDuplicateX, oldStartX+rangeDuplicateX).isValidIntValue(startX));
                                        checkInRangeY = (ValueRange.of(nextRangeY-rangeY, nextRangeY+rangeY).isValidIntValue(startY));
                                    } else {
                                        checkDuplicateStartX = true;
                                        checkInRangeY = true;
                                    }

                                    boolean checkDuplicateStartY = (ValueRange.of(oldStartY-rangeDuplicateY, oldStartY+rangeDuplicateY).isValidIntValue(startY));
                                    if (startY > theadholdstartY && !checkDuplicateStartY && checkDuplicateStartX && checkInRangeY) {
//                                        System.out.println(count + ": " + rect.x + "," + rect.y);
                                        Imgproc.rectangle (in, new Point(startX, startY), new Point(startX + (rect.width * 2), startY + rect.height), new Scalar(0, 255, 0),1);
                                        countGreenBox++;
                                        if ((oldStartY - startY) < 0) {
                                            nextRangeY = startY - diffY;
                                        } else {
                                            nextRangeY = startY - (oldStartY - startY);
                                        }
                                        oldStartY = startY;
                                        oldStartX = startX;
                                        startinLoop = true;

                                        // Crop minibox
                                        Rect rectCrop = new Rect(new Point(startX, startY), new Point(startX+ (rect.width * 2), startY+ rect.height));
                                        Mat imageCrop = bwim2.submat(rectCrop);

                                        // Detect order
                                        int allPixels = imageCrop.rows() * imageCrop.cols();
//                                        System.out.println("NO. all pixels => " + allPixels);

                                        // Valid
                                        // Get value pixel
                                        int black = 0;
                                        for (int row = 0; row < imageCrop.rows(); row++) {
                                            for (int col = 0; col < imageCrop.cols(); col++) {
                                                double[] check = imageCrop.get(row, col);
                                                int value = (int) check[0];
                                                // count NO. black pixel
//                                        System.out.println("value[" + row + "," + col + "] = " + value);
                                                if (value < 127) {
                                                    black++;
                                                }
                                            }
                                        }
//                                        System.out.println("NO. black pixels => " + black);


                                        // Order that menu or Not (There are black pixels more than 30% of all pixels of Image)
                                        float percentBlack = (float) black / (float) allPixels * 100;
//                                        System.out.println(count + "_Percent of Black pixel => " + percentBlack);
//
                                        if (percentBlack > 25) {
                                            String itOrder = "MenuOrdered => " + arrMenu[count-1][1] + ", " + arrMenu[count-1][0];
                                            System.out.println(itOrder);
                                            arrLineOrderedMenu.add(arrMenu[count-1][0]);
                                            message += itOrder + "\n";
                                            total += Integer.parseInt(arrMenu[count-1][1]);
                                        }
                                        // Print mini box
                                        aMk[count-1] = mat2Img(imageCrop);
                                        String pathname = "out/minibox2/minibox_" + count;
                                        File miniboxPath = new File(pathname + ".jpg");
                                        ImageIO.write(aMk[count-1], "jpg", miniboxPath);

                                        count++;

                                    }
                                }
                            }
                            if (countGreenBox == 12) {
                                stopAcquisition();
                            }
                            countGreenBox = 0;
                            Collections.reverse(arrLineOrderedMenu);
                            dataToScene2 = arrLineOrderedMenu.toArray(new String[arrLineOrderedMenu.size()]);
                            BufferedImage bb = mat2Img(in);
                            ImageIO.write(bb, "jpg", new File(outpath));
                            BufferedImage bw = mat2Img(bwim2);
                            ImageIO.write(bw, "jpg", new File(outpathBW));

//                            double x0 = 20, y0 = 110, ysp = 27;
//                            int mkwd = 40, mkhg = 40;
//                            for (int j = 0; j < 12; j++) {
//                                roi = new Rect((int) x0, (int) (y0 + ysp * (double) j), mkwd, mkhg);
//                                mk = new Mat(bwim, roi);
//                                // Detace order
//                                int allPixels = mk.rows() * mk.cols();
////                                System.out.println("NO. all pixels => " + allPixels);
//
//                                // Valid
//                                // Get value pixel
//                                int black = 0;
//                                for (int row = 0; row < mk.rows(); row++) {
//                                    for (int col = 0; col < mk.cols(); col++) {
//                                        double[] check = mk.get(row, col);
//                                        int value = (int) check[0];
//                                        // count NO. black pixel
////                                        System.out.println("value[" + row + "," + col + "] = " + value);
//                                        if (value < 150) {
//                                            black++;
//                                        }
//                                    }
//                                }
////                                System.out.println("NO. black pixels => " + black);
//
//
//                                // Order that menu or Not (There are black pixels more than 10% of all pixels of Image)
//                                float percentBlack = (float) black / (float) allPixels * 100;
////                                System.out.println(j + "_Percent of Black pixel => " + percentBlack);
//
//                                if (percentBlack > 10) {
//                                    String itOrder = "MenuOrdered => " + arrMenu[11 - j][1] + ", " + arrMenu[11 - j][0];
//                                    System.out.println(itOrder);
//                                    arrLineOrderedMenu.add(arrMenu[11 - j][0]);
//                                    message += itOrder + "\n";
//                                    total += Integer.parseInt(arrMenu[11 - j][1]);
//                                }
//
//                                // Print mini box
//                                aMk[j] = mat2Img(mk);
//                                String pathname = "out/minibox/minibox_" + j;
//                                File fJpg = new File(pathname + ".jpg");
//                                ImageIO.write(aMk[j], "jpg", fJpg);
//
//                            }
//                            dataToScene2 = arrLineOrderedMenu.toArray(new String[arrLineOrderedMenu.size()]);
//                            File fJpg = new File("out/002.jpg");
//                            ImageIO.write(img2, "jpg", fJpg);
//                                String txt = ""+qr+"\n" + message + "\n Total price : " + total;
                        }
                    }
                }

            } catch(com.google.zxing.NotFoundException a) {
            } catch (Exception e)
            {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    private void stopAcquisition() {
        if (this.timer!=null && !this.timer.isShutdown())
        {
            try
            {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e)
            {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened())
        {
            // release the camera
            this.capture.release();
        }
    }

    @FXML
    public void changeToScan2(ActionEvent e) throws IOException {
        Parent scan2SceneParent = FXMLLoader.load(getClass().getResource("scan2.fxml"));
        Scene scan2Scene = new Scene(scan2SceneParent, 800, 600);

        // get Stage information
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();

        window.setScene(scan2Scene);
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
    public void refresh(ActionEvent e) throws IOException {
        Parent scan1SceneParent = FXMLLoader.load(getClass().getResource("scan1.fxml"));
        Scene scan1Scene = new Scene(scan1SceneParent, 800, 600);

        // get Stage information
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();

        window.setScene(scan1Scene);
        window.show();
    }

    public void dispose() {
        isRunning = false;
//        runnable.terminate();
        timer.shutdown();
    }
}
