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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TableView<Table> mytable;

    private ArrayList<String> order;
    String[] arrOrdered;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final File folder = new File("D:\\newProject\\out\\now");
        listFilesForFolder(folder);


        TableColumn table = new TableColumn("Table");
        TableColumn time = new TableColumn("Time");
        mytable.getColumns().addAll(table, time);

        ArrayList<Table> ot = new ArrayList<>();
        for (int i =0; i<order.size(); i++) {
            String tableNO = order.get(i).substring(1,3);
            String tme = order.get(i).substring( order.get(i).length()-10, order.get(i).length()-4);
            String t = tme.substring(0, tme.length()-4) + " : " + tme.substring(2, tme.length()-2) + " : " + tme.substring(4);
            ot.add(new Table(tableNO, t));
        }
        ObservableList<Table> data = FXCollections.observableArrayList(ot);

        table.setCellValueFactory(new PropertyValueFactory<OrderTable,String>("table"));
        time.setCellValueFactory(new PropertyValueFactory<OrderTable,String>("time"));
        mytable.setItems(data);
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

    public void listFilesForFolder(final File folder) {
        order = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                order.add(fileEntry.getName());
            }
        }
    }


}
