package core.backend;

import java.time.LocalDate;

public class Reservation {
    Client client;
    RoomType room;
    LocalDate start;
    LocalDate end;

    public Reservation(Client client, RoomType room, LocalDate start, LocalDate end) {
        this.client = client;
        this.room = room;
        this.start = start;
        this.end = end;
    }

    public Client getClient() {
        return this.client;
    }

    public RoomType getRoom() {
        return this.room;
    }

    public LocalDate[] getDuration() {
        return new LocalDate[] {this.start, this.end};
    }
}