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

    private Random rng = new Random(12345);


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            testDetect();

            FileInputStream inputstream = new FileInputStream("out/testQR22.jpg");
            Image image = new Image(inputstream);
            leftFrame.setImage(image);

            FileInputStream inputstream2 = new FileInputStream("out/cvt.jpg");
            Image image2 = new Image(inputstream2);
            rightFrame.setImage(image2);


        } catch (IOException e) {}
    }

    public void testDetect() throws IOException {
//        Mat img = imread("test.jpg"), gray;
        File fJpg = new File("out/cvt.jpg");

        Imgcodecs imageCodecs = new Imgcodecs();
        Mat in = imageCodecs.imread("out/testQR22.jpg");
//        Mat cyOutput = new Mat();
//        BufferedImage bOut = mat2Img(in);
//        ImageIO.write(bOut, "jpg", fJpg);
//
//        List<MatOfPoint> contours = new ArrayList<>();
//        Mat hierarchy = new Mat();
        Mat gray = new Mat();

        Imgproc.cvtColor(in, gray, Imgproc.COLOR_RGB2GRAY);
        Mat bwim = new Mat();
//        Imgproc.threshold(gray, bwim, 100, 255, Imgproc.THRESH_BINARY);
//        bwim = gray;
//        BufferedImage bb = mat2Img(in);
//        ImageIO.write(bb, "jpg", fJpg);

        Mat cannyOutput = new Mat();
        Imgproc.Canny(gray, cannyOutput, 100, 100 * 2);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = boundingRect(contours.get(i));
            double k = (rect.height+0.0)/rect.width;
            if (0.9<k && k<1.1 && rect.area()>150) {
                Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
                Imgproc.drawContours(drawing, contours, i, color, 2, Core.LINE_8, hierarchy, 0, new Point());
            }
        }
//        BufferedImage bb = mat2Img(drawing);
        BufferedImage bb = mat2Img(drawing);
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
