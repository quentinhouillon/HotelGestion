package src.controler;

public class Room {
    private static int autoID = 0;
    int id;
    int roomNumber;
    int etage;
    String type;
    double price;
    String description;
    String linkImage;

    public Room(int num, String type, double price, String description, int etage, String linkImage) {
        this.id = autoID++;
        this.roomNumber = num;
        this.type = type;
        this.price = price;
        this.description = description;
        this.etage = etage;
        this.linkImage = linkImage;
    }

    public int getRoomNumber() {
        return this.roomNumber;
    }

    public int getEtage() {
        return this.etage;
    }

    public String getType() {
        return this.type;
    }

    public double getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLinkImage() {
        return this.linkImage;
    }

    public int getID() {
        return this.id;
    }
}