import javafx.beans.property.SimpleStringProperty;

public class Table {
    private SimpleStringProperty table;
    private SimpleStringProperty time;

    public Table(String table, String time) {
        this.table = new SimpleStringProperty(table);
        this.time = new SimpleStringProperty(time);
    }

    public String gettable() {
        return table.get();
    }

    public SimpleStringProperty tableProperty() {
        return table;
    }

    public void settable(String table) {
        this.table.set(table);
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
