import javafx.beans.property.SimpleStringProperty;

public class CheckTable {
    private SimpleStringProperty index;
    private SimpleStringProperty menu;
    private SimpleStringProperty amt;
    private SimpleStringProperty price;
    private SimpleStringProperty status;

    public CheckTable(String index, String menu, String amt, String price, String status) {
        this.index = new SimpleStringProperty(index);
        this.menu = new SimpleStringProperty(menu);
        this.amt = new SimpleStringProperty(amt);
        this.price = new SimpleStringProperty(price);
        this.status = new SimpleStringProperty(status);
    }

    public String getAmt() {
        return amt.get();
    }

    public SimpleStringProperty amtProperty() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt.set(amt);
    }

    public String getIndex() {
        return index.get();
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public void setIndex(String index) {
        this.index.set(index);
    }

    public String getMenu() {
        return menu.get();
    }

    public SimpleStringProperty menuProperty() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu.set(menu);
    }

    public String getPrice() {
        return price.get();
    }

    public SimpleStringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
