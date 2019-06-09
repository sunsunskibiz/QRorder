import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class Scan2Controller implements Initializable {
    @FXML
    private TableView<OrderTable> table;

//    public void initData() {
////        selectedTable = table;
//
//    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn order = new TableColumn("Order");
        table.getColumns().addAll(order);

        final ObservableList<OrderTable> data = FXCollections.observableArrayList(
                new OrderTable("Honey Toast"),
                new OrderTable("Honey Toast2"),
                new OrderTable("Honey Toast3")
        );

        order.setCellValueFactory(new PropertyValueFactory<OrderTable,String>("order"));
        table.setItems(data);
    }



}
