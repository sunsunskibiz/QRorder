import javafx.beans.property.SimpleStringProperty;

public class OrderTable {
    private SimpleStringProperty order;

    public OrderTable(String order) {
        this.order = new SimpleStringProperty(order);;
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
}
