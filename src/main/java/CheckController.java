import javafx.collections.FXCollections;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CheckController implements Initializable {
    @FXML
    private TableView<CheckTable> myTable;
    private String[] orderArr;
    private String[][] arrMenu = HelloFX.arrMenu;
    private String destPath = "D:\\newProject\\out\\now\\T15_20190611103433.ttl";


    public void initialize(URL url, ResourceBundle rb) {
        TableColumn index = new TableColumn("INDEX");
        TableColumn menu = new TableColumn("MENU");
        TableColumn amt = new TableColumn("AMOUNT");
        TableColumn price = new TableColumn("PRICE");
        TableColumn status = new TableColumn("STATUS");
        myTable.getColumns().addAll(index, menu, amt, price, status);
        index.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("index"));
        menu.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("menu"));
        amt.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("amt"));
        price.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("price"));
        status.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("status"));

        loaddataFromMain();
        fillPrepareTable();
    }

    private void loaddataFromMain() {
        FXMLLoader loader = new FXMLLoader();
        MainController mainController = loader.getController();
        String dp = mainController.destpath;
    }

    void fillPrepareTable() {
        ArrayList<CheckTable> ot = new ArrayList<>();
        int sum = 0;
        Rdf rdfRead = new Rdf();
        ArrayList<String> order = rdfRead.listMenuStatus(destPath);
        orderArr = order.toArray(new String[order.size()]);
        for (int k=0; k<order.size(); k++) {
            String menuRDF = orderArr[k].substring(0, orderArr[k].indexOf("|"));
            String statusRDF = orderArr[k].substring(orderArr[k].indexOf("|") + 2, orderArr[k].length()-1);
            for (String[] arrS : arrMenu) {
                if (menuRDF.equals(arrS[0])) {
                    ot.add(new CheckTable(Integer.toString(k+1), menuRDF, "1", arrS[1], statusRDF));
                    // SUM only Served
                    if (statusRDF.equals("Served"))
                        sum += Integer.parseInt(arrS[1]);
                }
            }
        }
        ot.add(new CheckTable("", "ALL", "", Integer.toString(sum), ""));

        ObservableList<CheckTable> data = FXCollections.observableArrayList(ot);
        myTable.setItems(data);
    }

    @FXML
    public void payPressed(ActionEvent e) throws IOException {
        Rdf rdf = new Rdf();
        if (rdf.changeStatusToPaid(destPath)) {
            System.out.println("changeStatusToPaid success.");
        } else {
            System.out.println("changeStatusToPaid failed");
        }

        // Back to main
        Parent mainSceneParent = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene mainScene = new Scene(mainSceneParent, 800, 600);
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        window.setScene(mainScene);
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
}
