import javafx.beans.property.SimpleStringProperty;

public class KitchenTable {
    private SimpleStringProperty order;
    private SimpleStringProperty tableNO;

    public KitchenTable (String tableNO, String order) {
        this.order = new SimpleStringProperty(order);
        this.tableNO = new SimpleStringProperty(tableNO);
    }

    public String getOrder() {
        return order.get();
    }

    public SimpleStringProperty orderProperty() {
        return order;
    }

    public void setOrder(String order) {
        this.order.set(order);
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
}
