package courier;

public class Order {

    private String firstName;
    private String lastName;
    private String address;
    private int metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;

    public Order(String[] color) {
        this.firstName = "Naruto";
        this.lastName = "Uchiha";
        this.address = "Konoha, 142 apt.";
        this.metroStation = 4;
        this.phone = "+7 800 355 35 35";
        this.rentTime = 5;
        this.deliveryDate = "2020-06-06";
        this.comment = "Saske, come back to Konoha";
        this.color = color;
    }

    public String[] getColor() {
        return color;
    }
}
