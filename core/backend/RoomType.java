package core.backend;

class Room {
    static private int numID = 0;
    int ID;
    int etage;
    String type;
    double price;
    String description;
    String linkImage;

    public Room(String type, double price, String description, int etage, String linkImage) {
        this.ID = numID++;
        this.type = type;
        this.price = price;
        this.description = description;
        this.etage = etage;
        this.linkImage = linkImage;
    }
}

public class RoomType extends Room {
    static final String[] rooms = {"Simple", "Double", "Suite normale",
                                   "Suite présidentielle"};
    static final Double[] roomPrice = {349.99, 549.99, 849.99, 1399.99};

    public RoomType(int id, String description, int etage, String link) {
        super(rooms[id], roomPrice[id], description, etage, link);
    }

    public int getID() {
        return this.ID;
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
}