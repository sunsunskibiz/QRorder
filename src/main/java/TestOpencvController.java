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

    private String inpath = "out/testQR442.jpg";
    private String outpath = "out/cvt.jpg";


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            testDetect();

            FileInputStream inputstream = new FileInputStream(inpath);
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
        Mat gray = new Mat();

        Imgproc.cvtColor(in, gray, Imgproc.COLOR_RGB2GRAY);
//        Mat filter = new Mat();
//        Imgproc.bilateralFilter(gray, filter, 9, 75, 75);
//        Mat bw = new Mat();
//        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 115, 4);

        Mat cannyOutput = new Mat();
        Imgproc.Canny(gray, cannyOutput, 100, 100 * 2);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        int count = 1;
        int oldStartX = 0;
        int oldStartY = 0;
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = boundingRect(contours.get(i));
            double k = (rect.height+0.0)/rect.width;
            if (0.8<k && k<1.2 && rect.area()>200) {
                Scalar color = new Scalar(0, 0, 255);
//                Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
                Imgproc.drawContours(in, contours, i, color, 2, Core.LINE_8, hierarchy, 0, new Point());
                int startX = rect.x + 20;
                int startY = rect.y;
                boolean checkDuplicateStartX = (startX == oldStartX);
                boolean checkDuplicateStartY = (startY == oldStartY);

                if (startY > 120 && !checkDuplicateStartX && !checkDuplicateStartY ) {
                    System.out.println(count++ + ": " + rect.x + "," + rect.y);
                    Imgproc.rectangle (in, new Point(startX, startY), new Point(startX + rect.height, startY + rect.width), new Scalar(0, 255, 0),1);
                    oldStartX = startX;
                    oldStartY = startY;
                }
            }
        }
        BufferedImage bb = mat2Img(in);
//        BufferedImage bb = mat2Img(drawing);
//        BufferedImage bb = mat2Img(cannyOutput);
//        BufferedImage bb = mat2Img(filter);
//        BufferedImage bb = mat2Img(bw);
        ImageIO.write(bb, "jpg", fJpg);

//        Imgproc.findContours(bwim, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//        Scalar color = new Scalar(0,0,255);
//        System.out.println(contours.size());
//        for (int i = 0; i < contours.size(); i++) {
//            Rect rect = boundingRect(contours.get(i));
//            double k = (rect.height+0.0)/rect.width;
////            if (0.9<k && k<1.1 && rect.area()>100)
////            {
//                Imgproc.drawContours(in, contours, i, color, 2, Core.LINE_8, hierarchy, 0, new Point());
////            }
//        }
//
//        BufferedImage bb = mat2Img(in);
//        ImageIO.write(bb, "jpg", fJpg);


//        vectorann<Vec4i> hierarchy;
//        vector<vector<Point2i> > contours;
//        cvtColor(img, gray, CV_BGR2GRAY);
//        threshold(gray, gray, 100, 255, THRESH_BINARY);
//        bitwise_not(gray, gray);
//
//        findContours(gray, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
//
//        for(size_t i=0; i<contours.size(); i++)
//        {
//            Rect rect = boundingRect(contours[i]);
//            double k = (rect.height+0.0)/rect.width;
//            if (0.9<k && k<1.1 && rect.area()>100)
//            {
//                drawContours(img, contours, i, Scalar(0,0,255));
//            }
//        }
//
//        imshow("result", img);
//        waitKey();
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
