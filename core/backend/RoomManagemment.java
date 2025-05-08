package core.backend;

import java.util.List;
import java.util.Map;

import core.model.Database;

import java.util.ArrayList;

public class RoomManagemment {
    static List<Room> rooms = new ArrayList<>();
    private Database database = new Database();

    public Room[] getRooms() {
        rooms.clear();
        List<Map<String, Object>> rows = database.executeReadQuery("SELECT * FROM Room");
        for (Map<String, Object> row : rows) {
            int id = (int) row.get("id");
            String type = (String) row.get("type");
            double price = (double) row.get("price");
            String description = (String) row.get("description");
            int etage = (int) row.get("etage");
            String image = (String) row.get("image");
            addRoom(type, price, id, description, etage, image);
        }
        if (rooms.isEmpty()) createRoom();

        return rooms.toArray(Room[]::new);
    }

    public void addRoom(String type, double price, int roomNumber, String description, int etage, String linkImage) {
        database.executeUpdateQuery(
            "INSERT INTO Room (room_number, etage, type, price, description) VALUES (?, ?, ?, ?, ?, ?)",
            new Object[] { roomNumber, etage, type, price, description, linkImage }
        );
        rooms.add(new Room(roomNumber, type, price, description, etage, linkImage));
    }

    public void removeRoom(Room room) {
        database.executeUpdateQuery("DELETE FROM Room WHERE room_number = ?", new Object[] { room.getroomNumber() });
        rooms.remove(room);
    }

    // public void modifyRoom(int id, String newType, double newPrice, String newDescription, int newEtage) {
    //     database.executeUpdateQuery(
    //         "UPDATE rooms SET type = ?, price = ?, description = ?, etage = ? WHERE id = ?",
    //         new Object[] { newType, newPrice, newDescription, newEtage, id }
    //     );
    //     for (Room room : rooms) {
    //         if (room.roomNumber == id) {
    //             room.type = newType;
    //             room.price = newPrice;
    //             room.description = newDescription;
    //             room.etage = newEtage;
    //             break;
    //         }
    //     }
    // }

    public Room getRoomByNumRoom(int num) {
        for (Room room : rooms) {
            if (room.getroomNumber() == num) {
                return room;
            }
        }
        return null;
    }

    public Room[] getRoomByTypes(String type) {
        List<Room> filteredRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.type.equals(type)) filteredRooms.add(room);
        }
        return filteredRooms.toArray(new Room[0]);
    }

    private String[] getRoomTypes() {
        return new String[] {"Simple", "Double", "Suite normale",
                                   "Suite présidentielle"};
    }

    private Double[] getRoomPrices() {
        return new Double[] {349.99, 549.99, 849.99, 1399.99};
    }

    private void createRoom() {
        this.addRoom(getRoomTypes()[0],getRoomPrices()[0], 101, "Chambre simple économique, idéale pour une personne, avec lit confortable et accès Wi-Fi gratuit.", 1, "./img/chambreSimple1.jpg");
        this.addRoom(getRoomTypes()[0],getRoomPrices()[0], 201, "Chambre individuelle compacte, offrant un espace fonctionnel avec salle de bain privée et petit bureau.", 2, "./img/chambreSimple2.jpg");
        this.addRoom(getRoomTypes()[1],getRoomPrices()[1], 102, "Chambre double économique, parfaite pour deux, avec lits jumeaux et salle de bain privée.", 1, "./img/chambreDouble1.jpg");
        this.addRoom(getRoomTypes()[1],getRoomPrices()[1], 202, "Chambre standard pour deux personnes, offrant un lit double confortable et un espace de rangement.", 2, "./img/chambreDouble2.jpg");
        this.addRoom(getRoomTypes()[2],getRoomPrices()[2], 301, "Suite spacieuse offrant un coin salon, lit double confortable et salle de bain avec baignoire, parfaite pour deux ou plus.", 3, "./img/suiteNormale1.jpg");
        this.addRoom(getRoomTypes()[3],getRoomPrices()[3], 401, "Suite présidentielle prestigieuse offrant un espace de vie somptueux, terrasse privée et services exclusifs pour un séjour inoubliable", 4, "./img/suitePrésidentielle.jpg");
    }
}