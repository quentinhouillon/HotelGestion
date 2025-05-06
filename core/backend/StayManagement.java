package core.backend;

import java.util.ArrayList;
import java.util.List;

public class StayManagement {
    List<Stay> stays = new ArrayList<>();

    public void add(Reservation reservation) {
        if ((reservation.getDuration()[0].isBefore(java.time.LocalDate.now()) || 
             reservation.getDuration()[0].isEqual(java.time.LocalDate.now())) &&
            stays.stream().noneMatch(stay -> stay.getReservation().equals(reservation))) {
            stays.add(new Stay(reservation));
        }
    }

    public void remove(Stay stay) {
        stays.remove(stay);
    }

    public void clear() {
       stays.clear();
    }

    public Stay[] getAll() {
        return stays.toArray(Stay[]::new);
    }

    public boolean contains(Reservation reservation) {
        for (Stay stay : stays) {
            if (stay.getReservation().equals(reservation)) {
                return true;
            }
        }
        return false;
    }
}