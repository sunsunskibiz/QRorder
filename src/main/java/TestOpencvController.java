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
import java.util.Random;
import java.util.ResourceBundle;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;

import static org.opencv.imgproc.Imgproc.boundingRect;


public class TestOpencvController implements Initializable {
    @FXML
    private ImageView leftFrame;
    @FXML
    private ImageView rightFrame;

    private String inpath = "out/106.jpg";
//    private String inpath = "out/testQRRR20.jpg";
    private String outpath = "out/cvt.jpg";
    private String graypath = "out/gray.jpg";
    private int theadholdstartY = 100;
    private int theadholdRectArea = 100;
    private int diffY = 30;
    private int rangeDuplicateX = 5;
    private int rangeDuplicateY = 5;
    private int rangeY = 7;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            testDetect();

            FileInputStream inputstream = new FileInputStream(graypath);
            Image image = new Image(inputstream);
            leftFrame.setImage(image);

            FileInputStream inputstream2 = new FileInputStream(outpath);
            Image image2 = new Image(inputstream2);
            rightFrame.setImage(image2);


        } catch (IOException e) {}
    }

    public void testDetect() throws IOException {
        File fJpg = new File(outpath);

        Imgcodecs imageCodecs = new Imgcodecs();
        Mat in = imageCodecs.imread(inpath);
//        Mat gray = imageCodecs.imread(inpath,0);
        Mat bwim = new Mat();

        Mat gray = new Mat();

        Imgproc.cvtColor(in, gray, Imgproc.COLOR_RGB2GRAY);
//        Mat filter = new Mat();
//        Imgproc.bilateralFilter(gray, filter, 9, 75, 75);
//        Mat bw = new Mat();
//        Imgproc.threshold(gray, bwim, 127, 255, Imgproc.THRESH_BINARY);
        BufferedImage aa = mat2Img(gray);
        ImageIO.write(aa, "jpg", new File(graypath));


        Mat cannyOutput = new Mat();
        Imgproc.Canny(gray, cannyOutput, 100, 100 * 2);
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
//            Imgproc.drawContours(in, contours, i, color, 1, Core.LINE_8, hierarchy, 0, new Point());

            if (1.5<k && k<2.5 && rect.area()>theadholdRectArea) {
//                Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
                Imgproc.drawContours(in, contours, i, color, 1, Core.LINE_8, hierarchy, 0, new Point());
                int startX = rect.x + 20;
                int startY = rect.y;
                boolean checkDuplicateStartX;
                boolean checkInRangeY;
                if (startinLoop) {
                    checkDuplicateStartX = (ValueRange.of(oldStartX-rangeDuplicateX, oldStartX+rangeDuplicateX).isValidIntValue(startX));
                    checkInRangeY = (ValueRange.of(nextRangeY-rangeY, nextRangeY+rangeY).isValidIntValue(startY));
//                    System.out.println("nextRangeY : " + nextRangeY);
//                    System.out.println("startY : " + startY);
                } else {
                    checkDuplicateStartX = true;
                    checkInRangeY = true;
                }

                boolean checkDuplicateStartY = (ValueRange.of(oldStartY-rangeDuplicateY, oldStartY+rangeDuplicateY).isValidIntValue(startY));
//                System.out.println("!checkDuplicateStartY : " + !checkDuplicateStartY);
//                System.out.println("checkDuplicateStartX : " + checkDuplicateStartX);
//                System.out.println("checkInRangeY : " + checkInRangeY);
                if (startY > theadholdstartY && !checkDuplicateStartY && checkDuplicateStartX && checkInRangeY) {
//                if (startY > theadholdstartY && !checkDuplicateStartY && checkDuplicateStartX) {
                    System.out.println(count++ + ": " + rect.x + "," + rect.y);
                    Imgproc.rectangle (in, new Point(startX, startY), new Point(startX + (rect.width * 2), startY + rect.height), new Scalar(0, 255, 0),1);
//                    System.out.println("Diff y : " + (oldStartY - startY));
                    if ((oldStartY - startY) < 0) {
                        nextRangeY = startY - diffY;
                    } else {
                        nextRangeY = startY - (oldStartY - startY);
                    }
                    System.out.println("Next y : " + nextRangeY);
                    oldStartY = startY;
                    oldStartX = startX;
                    startinLoop = true;
                }
            }
        }
        BufferedImage bb = mat2Img(in);
//        BufferedImage bb = mat2Img(drawing);
//        BufferedImage bb = mat2Img(cannyOutput);
//        BufferedImage bb = mat2Img(filter);
//        BufferedImage bb = mat2Img(bw);
        ImageIO.write(bb, "jpg", fJpg);

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
}
