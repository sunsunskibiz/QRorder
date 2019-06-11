import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {
    @FXML
    private TableView<ManageTable> myTable;

    public void initialize(URL url, ResourceBundle rb) {
        fillManageTable();
    }

    void fillManageTable() {
        // TODO
        TableColumn index = new TableColumn("INDEX");
        TableColumn menu = new TableColumn("MENU");
        TableColumn amt = new TableColumn("AMOUNT");
        TableColumn price = new TableColumn("TOTAL PRICE");

        myTable.getColumns().addAll(index, menu, amt, price);
        final ObservableList<ManageTable> data = FXCollections.observableArrayList(
                new ManageTable("01", "TWO-TONE KAKIGORI", "20", "2780"),
                new ManageTable("02", "CHOCOLATE MUD BROWNIE", "9", "711"),
                new ManageTable("03", "FIGGY PUDDING", "5", "445"),
                new ManageTable("04", "HONEY TOAST", "32", "3488"),
                new ManageTable("05", "DARK CHOCOLATE PRAPPE", "7", "343"),
                new ManageTable("06", "MATHCA MILLE CREPE", "16", "2704"),
                new ManageTable("07", "WARM CHOCOLATE CHIP PANOOKIE", "17", "1343"),
                new ManageTable("08", "STRAWBERRY MILLE CREPR", "14", "966"),
                new ManageTable("09", "LYNCHEE JUICE", "8", "232"),
                new ManageTable("10", "WHITE CHOC MACCHIATO", "10", "490"),
                new ManageTable("11", "MATCHA FRAPPE", "7", "343"),
                new ManageTable("12", "PINKLEMONADE", "12", "468"),
                new ManageTable("", "ALL", "157", "13912")
        );


        //Step : 3#  Associate data with columns
        index.setCellValueFactory(new PropertyValueFactory<ManageTable,String>("index"));
        menu.setCellValueFactory(new PropertyValueFactory<ManageTable,String>("menu"));
        amt.setCellValueFactory(new PropertyValueFactory<ManageTable,String>("amt"));
        price.setCellValueFactory(new PropertyValueFactory<ManageTable,String>("price"));


        //Step 4: add data inside table
        myTable.setItems(data);
    }
}
