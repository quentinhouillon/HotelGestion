package core.backend;

import java.util.List;
import java.util.ArrayList;

public class RoomManagemment {
    static List<RoomType> rooms = new ArrayList<>();

    public void addRoom(RoomType room) {
        rooms.add(room);
    }

    public void removeRoom(int id) {
        rooms.removeIf(room -> room.ID == id);
    }

    public void modifyRoom(int id, String newType, double newPrice, String newDescription, int newEtage) {
        if (id >= 0 && id < rooms.size()) {
            Room room = rooms.get(id);
            room.type = newType;
            room.price = newPrice;
            room.description = newDescription;
            room.etage = newEtage;
        }
    }

    public Room getRoomByID(int id) {
        if (id >= 0 && id < rooms.size()) return rooms.get(id);
        return null;
    }

    public RoomType[] getRoomByTypes(String type) {
        List<RoomType> filteredRooms = new ArrayList<>();
        for (RoomType room : rooms) {
            if (room.type.equals(type)) filteredRooms.add(room);
        }
        return filteredRooms.toArray(new RoomType[0]);
    }

    public RoomType[] getRooms() {
        return rooms.toArray(RoomType[]::new);
    }
}