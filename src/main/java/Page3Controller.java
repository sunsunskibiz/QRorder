import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author AMIT
 */
public class Page3Controller implements Initializable {


    @FXML
    private TableView<Table> myTable;

    @FXML
    private Button payButton;


    @FXML
    public void changeScreenButtonPushed(ActionEvent e) throws IOException {
        Parent page1SceneParent = FXMLLoader.load(getClass().getResource("page1.fxml"));
        Scene page1Scene = new Scene(page1SceneParent, 800, 600);

        // get Stage information
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();

        window.setScene(page1Scene);
        window.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        TableColumn tableNO = new TableColumn("TABLE NO.");
        TableColumn date = new TableColumn("DATE");
        TableColumn time = new TableColumn("TIME");



        myTable.getColumns().addAll(tableNO, date, time);


        //Step : 1# Create a person class that will represtent data

        //Step : 2# Define data in an Observable list and add data as you want to show inside table
//        final ObservableList<Table> data = FXCollections.observableArrayList(
//                new Person("1", "Jacob", "24", "", "jacob.smith@example.com", "jacob.smith@example.com"),
//                new Person("2","Isabella", "25", "","isabella.johnson@example.com", "jacob.smith@example.com"),
//                new Person("3","Ethan", "27","" ,"ethan.williams@example.com", "jacob.smith@example.com"),
//                new Person("4","Emma", "28","" ,"emma.jones@example.com", "jacob.smith@example.com"),
//                new Person("5","Michael", "29", "" ,"michael.brown@example.com", "jacob.smith@example.com"),
//                new Person("5","Michael", "29", "","michael.brown@example.com", "jacob.smith@example.com")
        final ObservableList<Table> data = FXCollections.observableArrayList(
                new Table("1", "22-04-2019", "16:24:23"),
                new Table("2", "22-04-2019", "17:18:37"),
                new Table("3", "22-04-2019", "18:01:49"),
                new Table("4", "22-04-2019", "18:48:10"),
                new Table("5", "22-04-2019", "19:30:23")
        );


        //Step : 3#  Associate data with columns
        tableNO.setCellValueFactory(new PropertyValueFactory<Table,String>("tableNO"));

        date.setCellValueFactory(new PropertyValueFactory<Table,String>("date"));

        time.setCellValueFactory(new PropertyValueFactory<Table,String>("time"));




        //Step 4: add data inside table
        myTable.setItems(data);


//        myTable.setRowFactory(tv -> {
//            TableRow<Table> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
//                    Table rowData = row.getItem();
//                    System.out.println("Double click on: "+rowData.getTableNO());
//
//                    // Go to page4
//                    FXMLLoader loader = new FXMLLoader();
//                    loader.setLocation(getClass().getResource("page4.fxml"));
//                    Parent tableViewParent = loader.load();
//
//                    Scene tableViewScene = new Scene(tableViewParent);
//
//                    //access the controller and call a method
//                    page4Controller controller = loader.getController();
//                    controller.initData(myTable.getSelectionModel().getSelectedItem());
//
//                    //This line gets the Stage information
//                    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//
//                    window.setScene(tableViewScene);
//                    window.show();
//                }
//            });
//            return row ;
//        });
    }

//    public void changeSceneToPage4(ActionEvent event) throws IOException
//    {
//        if (myTable.getSelectionModel().getSelectedItem() != null) {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("page4.fxml"));
//            Parent tableViewParent = loader.load();
//
//            Scene tableViewScene = new Scene(tableViewParent);
//
//            //access the controller and call a method
//            page4Controller controller = loader.getController();
//            controller.initData(myTable.getSelectionModel().getSelectedItem());
//
//            //This line gets the Stage information
//            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
//
//            window.setScene(tableViewScene);
//            window.show();
//        }
//
//    }

//    @FXML
//    public void handleKeyRelease() {
//        String text = nameField.getText();
//        boolean disableButton = text.isEmpty() || text.trim().isEmpty();
//        helloButton.setDisable(disableButton);
//        byeButton.setDisable(disableButton);
//    }

}
