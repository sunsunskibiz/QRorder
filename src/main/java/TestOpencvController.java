import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestOpencvController implements Initializable {
    @FXML
    private ImageView leftFrame;
    @FXML
    private ImageView rightFrame;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FileInputStream inputstream = new FileInputStream("out/jn.jpg");
            System.out.println(inputstream);
            Image image = new Image(inputstream);
            leftFrame.setImage(image);

            FileInputStream inputstream2 = new FileInputStream("out/jn2.jpg");
            System.out.println(inputstream2);
            Image image2 = new Image(inputstream2);
            rightFrame.setImage(image2);
        } catch (IOException e) {}
    }
}
