import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CheckController implements Initializable {
    @FXML
    private TableView<CheckTable> myTable;

    public void initialize(URL url, ResourceBundle rb) {
        fillPrepareTable();
    }

    void fillPrepareTable() {
        // TODO
        TableColumn index = new TableColumn("INDEX");
        TableColumn menu = new TableColumn("MENU");
        TableColumn amt = new TableColumn("AMOUNT");
        TableColumn price = new TableColumn("PRICE");
        TableColumn status = new TableColumn("STATUS");

        myTable.getColumns().addAll(index, menu, amt, price, status);
//        myTable.getColumns().addAll(index, menu, price, status);
        final ObservableList<CheckTable> data = FXCollections.observableArrayList(
                new CheckTable("01", "LYNCHEE JUICE", "1", "29", "Served"),
                new CheckTable("02", "WHITE CHOC MACCHIATO", "1", "49", "Served"),
                new CheckTable("03", "MATCHA FRAPPE", "1", "49", "Served"),
                new CheckTable("04", "PINKLEMONADE", "1", "39", "Served"),
                new CheckTable("", "ALL", "4", "166", "")
        );


        //Step : 3#  Associate data with columns
        index.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("index"));
        menu.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("menu"));
        amt.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("amt"));
        price.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("price"));
        status.setCellValueFactory(new PropertyValueFactory<CheckTable,String>("status"));


        //Step 4: add data inside table
        myTable.setItems(data);
    }
}
