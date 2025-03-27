package core.backend;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


class Reservation {
    Client client;
    Room room;
    Date start;
    Date end;

    public Reservation(Client client, Room room, Date start, Date end) {
        this.client = client;
        this.room = room;
        this.start = start;
        this.end = end;
    }

    Client getClient() {
        return this.client;
    }

    Room getRoom() {
        return this.room;
    }

    Date[] getDuration() {
        return new Date[] {this.start, this.end};
    }
}

public class ReservationManagement {
    static List<Reservation> reservation;

    public ReservationManagement() {
        reservation = new ArrayList<>();
    }

    void add(Client client, Room room, Date Start, Date end) {
        reservation.add(new Reservation(client, room, Start, end));
    }

    void cancel(int id) {
        if (id >= 0 && id < reservation.size()) 
            reservation.remove(id);
    }

    void modify() {
        
    }
}