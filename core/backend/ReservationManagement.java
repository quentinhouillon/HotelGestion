package core.backend;

import core.model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationManagement {
    static List<Reservation> reservations = new ArrayList<>();
    private Database database = new Database();
    private ClientManagement clients = new ClientManagement();
    private RoomManagemment rooms = new RoomManagemment();

    public ReservationManagement() {
        this.getAll();
    }

    public Reservation[] getAll() {
        reservations.clear();
        List<Map<String, Object>> rows = database.executeReadQuery(("SELECT * FROM Reservation"));
        for (Map<String, Object> row : rows) {
            int id = (int) row.get("id");
            int clientID = (int) row.get("client_id");
            int roomID = (int) row.get("room_id");
            String start = (String) row.get("start_date");
            String end = (String) row.get("end_date");
            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);

            reservations.add(
                    new Reservation(id, clients.getClientById(clientID), rooms.getRoomById(roomID), startDate,
                            endDate));
        }
        return reservations.toArray(Reservation[]::new);
    }

    public Reservation add(Client client, Room room, LocalDate start, LocalDate end) {
        database.executeUpdateQuery(
                "INSERT INTO Reservation (client_id, room_id, start_date, end_date) VALUES (?, ?, ?, ?)",
                new Object[] { client.getID(), room.getID(), start, end });

        List<Map<String, Object>> rows = database
                .executeReadQuery("SELECT * FROM Reservation ORDER BY id DESC LIMIT 1");
        int id = -1;
        if (!rows.isEmpty()) {
            id = (int) rows.get(0).get("id");
        }

        Reservation newReservation = new Reservation(id, client, room, start, end);
        reservations.add(newReservation);
        return newReservation;
    }

    public void remove(Reservation res) {
        database.executeUpdateQuery("DELETE FROM Reservation WHERE id = ?", new Object[] { res.getID() });
        reservations.remove(res);
    }

    public boolean isRoomReserved(int id, LocalDate startDate, LocalDate endDate) {
        for (Reservation res : reservations) {
            if (res.getRoom().getID() == id &&
                    ((startDate.isBefore(res.getDuration()[1]) && endDate.isAfter(res.getDuration()[0])) ||
                            startDate.equals(res.getDuration()[0]) || endDate.equals(res.getDuration()[1]))) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(Reservation reservation) {
        for (Reservation res : reservations) {
            if (res.equals(reservation))
                return true;
        }
        return false;
    }

    public Reservation getReservationByID(int id) {
        for (Reservation res : getAll()) {
            if (res.getID() == id) return res;
        }
        return null;
    }

    public Reservation[] search(String clientName) {
        if (clientName.trim().isEmpty())
            return this.getAll();

        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getClient().getLastName().equalsIgnoreCase(clientName)) {
                result.add(res);
            }
        }
        return result.toArray(Reservation[]::new);
    }

    public Reservation[] search(int roomNumber) {
        if (roomNumber < 0)
            return this.getAll();

        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            if (res.getRoom().getRoomNumber() == roomNumber) {
                result.add(res);
            }
        }
        return result.toArray(Reservation[]::new);
    }

    public Reservation[] search(LocalDate date) {
        if (date == null)
            return this.getAll();

        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservations) {
            LocalDate[] duration = res.getDuration();
            if ((date.isEqual(duration[0]) || date.isEqual(duration[1])) ||
                    (date.isAfter(duration[0]) && date.isBefore(duration[1]))) {
                result.add(res);
            }
        }
        return result.toArray(Reservation[]::new);
    }
}