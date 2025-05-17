package src.controler;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class RoomManagemment {
    static List<Room> rooms = new ArrayList<>();

    public RoomManagemment() {
        if (rooms.isEmpty()) {
            // Chambres simples (4)
            this.addRoom(getRoomTypes()[0], getRoomPrices()[0], 101,
                    "Chambre simple économique, idéale pour une personne, avec lit confortable et accès Wi-Fi gratuit.",
                    1, "./img/chambreSimple1.jpg");
            this.addRoom(getRoomTypes()[0], getRoomPrices()[0], 102,
                    "Chambre simple lumineuse avec bureau et salle de bain privée.",
                    1, "./img/chambreSimple2.jpg");
            this.addRoom(getRoomTypes()[0], getRoomPrices()[0], 201,
                    "Chambre simple calme, vue sur cour, idéale pour le repos.",
                    2, "./img/chambreSimple1.jpg");
            this.addRoom(getRoomTypes()[0], getRoomPrices()[0], 202,
                    "Chambre simple moderne avec accès Wi-Fi et petit-déjeuner inclus.",
                    2, "./img/chambreSimple2.jpg");

            // Chambres doubles (4)
            this.addRoom(getRoomTypes()[1], getRoomPrices()[1], 103,
                    "Chambre double confortable avec lits jumeaux et salle de bain privée.",
                    1, "./img/chambreDouble1.jpg");
            this.addRoom(getRoomTypes()[1], getRoomPrices()[1], 104,
                    "Chambre double spacieuse, idéale pour couples, avec vue sur la ville.",
                    1, "./img/chambreDouble2.jpg");
            this.addRoom(getRoomTypes()[1], getRoomPrices()[1], 203,
                    "Chambre double élégante, espace de rangement et coin salon.",
                    2, "./img/chambreDouble1.jpg");
            this.addRoom(getRoomTypes()[1], getRoomPrices()[1], 204,
                    "Chambre double moderne avec balcon privé.",
                    2, "./img/chambreDouble2.jpg");

            // Suites normales (3)
            this.addRoom(getRoomTypes()[2], getRoomPrices()[2], 301,
                    "Suite normale avec coin salon, lit double et salle de bain avec baignoire.",
                    3, "./img/suiteNormale1.jpg");
            this.addRoom(getRoomTypes()[2], getRoomPrices()[2], 302,
                    "Suite normale spacieuse, idéale pour familles, avec espace bureau.",
                    3, "./img/suiteNormale2.jpg");
            this.addRoom(getRoomTypes()[2], getRoomPrices()[2], 401,
                    "Suite normale élégante avec balcon, vue panoramique et équipements modernes.",
                    4, "./img/suiteNormale1.jpg");

            // Suite présidentielle (1)
            this.addRoom(getRoomTypes()[3], getRoomPrices()[3], 402,
                    "Suite présidentielle prestigieuse avec terrasse privée et services exclusifs.",
                    4, "./img/suitePrésidentielle.jpg");
        }
    }

    public Room[] getRooms() {
        return rooms.toArray(Room[]::new);
    }

    public void addRoom(String type, double price, int roomNumber, String description, int etage, String linkImage) {
        rooms.add(new Room(roomNumber, type, price, description, etage, linkImage));
    }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public Room getRoomByNumRoom(int num) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == num) {
                return room;
            }
        }
        return null;
    }

    public Room getRoomById(int id) {
        for (Room room : rooms) {
            if (room.getID() == id) {
                return room;
            }
        }
        return null;
    }

    public Room[] getRoomByTypes(String type) {
        List<Room> filteredRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.type.equals(type))
                filteredRooms.add(room);
        }
        return filteredRooms.toArray(new Room[0]);
    }

    private String[] getRoomTypes() {
        return new String[] { "Simple", "Double", "Suite normale",
                "Suite présidentielle" };
    }

    private Double[] getRoomPrices() {
        return new Double[] { 349.99, 549.99, 849.99, 1399.99 };
    }
}