package core.backend;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.model.*;

public class StayManagement {
    List<Stay> stays = new ArrayList<>();
    ReservationManagement reservations = new ReservationManagement();
    private Database database = new Database();

    public Stay[] getAll() {
        stays.clear();
        List<Map<String, Object>> rows = database.executeReadQuery(("SELECT * FROM Stay"));
        for (Map<String, Object> row : rows) {
            int id = (int) row.get("id");
            int reservationID = (int) row.get("reservation_id");
            String payment = (String) row.get("payment");

            stays.add(new Stay(id, reservations.getReservationByID(reservationID)));
        }
        return stays.toArray(Stay[]::new);
    }

    public void add(Reservation reservation) {
        if ((reservation.getDuration()[0].isBefore(java.time.LocalDate.now()) ||
                reservation.getDuration()[0].isEqual(java.time.LocalDate.now())) &&
                stays.stream().noneMatch(stay -> stay.getReservation().equals(reservation))) {

            List<Map<String, Object>> existingRows = database.executeReadQuery(
                    "SELECT * FROM Stay WHERE reservation_id = " + reservation.getID());
            if (existingRows.isEmpty()) {
                database.executeUpdateQuery(
                        "INSERT INTO Stay (reservation_id, payment) VALUES (?, ?)",
                        new Object[] { reservation.getID(), null });
            }

            List<Map<String, Object>> rows = database
                    .executeReadQuery("SELECT * FROM Stay ORDER BY id DESC LIMIT 1");
            int id = -1;
            if (!rows.isEmpty()) {
                id = (int) rows.get(0).get("id");
            }
            stays.add(new Stay(id, reservation));
        }
    }

    public void remove(Stay stay) {
        database.executeUpdateQuery("DELETE FROM Stay WHERE id = ?", new Object[] {stay.getID()});
        stays.remove(stay);
    }

    public void clear() {
        stays.clear();
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