public class Menu {
    private int number;
    private String name;
    private int price;
//    private String pathImage;

    public Menu(int number, String name, int price) {
        this.number = number;
        this.name = name;
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

//    public String getPathImage() {
//        return pathImage;
//    }
//
//    public void setPathImage(String pathImage) {
//        this.pathImage = pathImage;
//    }
}
