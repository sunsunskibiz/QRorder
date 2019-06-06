import javafx.beans.property.SimpleStringProperty;

public class Table {
    private SimpleStringProperty tableNO;
    private SimpleStringProperty date;
    private SimpleStringProperty time;

    public Table(String tableNO, String date, String time) {
        this.tableNO = new SimpleStringProperty(tableNO);
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }

    public String getTableNO() {
        return tableNO.get();
    }

    public SimpleStringProperty tableNOProperty() {
        return tableNO;
    }

    public void setTableNO(String tableNO) {
        this.tableNO.set(tableNO);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }
}
