package core.backend;
class Room {
    static private int numID = 0;
    int ID;
    int etage;
    String type;
    double price;
    String Description;
    String linkImage;

    public Room(String type, double price, String description, int etage) {
        this.ID = numID++;
        this.type = type;
        this.price = price;
        this.Description = description;
        this.etage = etage;
    }
}

public class RoomManagemment extends Room {
    static final String[] rooms = {"Simple", "Double", "Suite normale",
                                   "Suite présidentielle"};
    static final Double[] roomPrice = {349.99, 549.99, 849.99, 1399.99};

    public RoomManagemment(int id, String description, int etage) {
        super(rooms[id], roomPrice[id], description, etage);
    }
}