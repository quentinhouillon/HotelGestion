package core.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationManagement {
    static List<Reservation> reservation = new ArrayList<>();

    public void add(Client client, Room room, LocalDate start, LocalDate end) {
        reservation.add(new Reservation(client, room, start, end));
    }

    public void remove(Reservation res) {
        reservation.remove(res);
    }

    public Reservation[] getAll() {
        return reservation.toArray(Reservation[]::new);
    }

    public boolean isRoomReserved(int id, LocalDate startDate, LocalDate endDate) {
        Room room = new RoomManagemment().getRoomByNumRoom(id);
        if (room == null) return false;

        for (Reservation res : reservation) {
            if (res.getRoom().equals(room) &&
            ((startDate.isBefore(res.getDuration()[1]) && endDate.isAfter(res.getDuration()[0])) ||
            startDate.equals(res.getDuration()[0]) || endDate.equals(res.getDuration()[1]))) {
            return true;
            }
        }
        return false;
    }

    public boolean contains(Reservation reservation_) {
        for (Reservation res : this.reservation) {
            if (reservation_ == res)
                return true;
        }
        return false;
    }

    public Reservation[] search(String clientName) {
        if (clientName.trim().isEmpty()) return this.getAll();

        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservation) {
            if (res.getClient().getLastName().equalsIgnoreCase(clientName)) {
                result.add(res);
            }
        }
        return result.toArray(Reservation[]::new);
    }
    
    public Reservation[] search(int roomNumber) {
        if (roomNumber < 0) return this.getAll();

        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservation) {
            if (res.getRoom().getroomNumber() == roomNumber) {
                result.add(res);
            }
        }
        return result.toArray(Reservation[]::new);
    }
    
    public Reservation[] search(LocalDate date) {
        if (date == null) return this.getAll();

        List<Reservation> result = new ArrayList<>();
        for (Reservation res : reservation) {
            LocalDate[] duration = res.getDuration();
            if ((date.isEqual(duration[0]) || date.isEqual(duration[1])) ||
            (date.isAfter(duration[0]) && date.isBefore(duration[1]))) {
                result.add(res);
            }
        }
        return result.toArray(Reservation[]::new);
    }
}