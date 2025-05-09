package core.backend;

import java.time.LocalDate;

public class Reservation {
    int id;
    Client client;
    Room room;
    LocalDate start;
    LocalDate end;

    public Reservation(int id, Client client, Room room, LocalDate start, LocalDate end) {
        this.id = id;
        this.client = client;
        this.room = room;
        this.start = start;
        this.end = end;
    }

    public Client getClient() {
        return this.client;
    }

    public Room getRoom() {
        return this.room;
    }

    public LocalDate[] getDuration() {
        return new LocalDate[] {this.start, this.end};
    }

    public int getID() {
        return this.id;
    }
}