package src.controler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import src.model.Database;

class MiniBar {
    static final String[] boissons = { "Eau", "Soda", "Jus de fruit", "Thé",
            "Café", "Vin blanc", "Vin rouge",
            "Vin rosé", "Bière", "Champagne" };
    static final double[] prices = { 3.0, 4.25, 3.50, 4.0, 4.0, 12.0, 12.0,
            12.0, 8.50, 17.50 };

    public String[] getBoissons() {
        return boissons;
    }

    public double[] getPrices() {
        return prices;
    }
}

public class Stay {
    private int id;
    private Reservation reservation;
    private List<String> consomation;
    private List<Double> price;
    private Database database;
    private String payment;
    private static MiniBar miniBar;

    public Stay(int id, Reservation reservation) {
        this.id = id;
        this.reservation = reservation;
        this.consomation = new ArrayList<>();
        this.price = new ArrayList<>();
        this.database = new Database();
        this.miniBar = new MiniBar();
        this.payment = getPayment();
    }

    public Client getClient() {
        return this.reservation.getClient();
    }

    public Reservation getReservation() {
        return this.reservation;
    }

    public int getID() {
        return this.id;
    }

    public String getPayment() {
        List<Map<String, Object>> rows = database.executeReadQuery("SELECT payment FROM Stay WHERE id = " + this.getID());
        if (!rows.isEmpty())
            return (String) rows.get(0).get("payment");
        return null;
    }

    public void setPayment(String payment) {
        this.database.executeUpdateQuery(
                "UPDATE Stay SET payment = ? WHERE id = ?",
                new Object[] { payment, this.id });
        this.payment = payment;
    }

    public String[] getAllPayments() {
        return new String[] { "Carte bleue", "Espèce", "Chèque" };
    }

    public String[] getConso() {
        this.consomation.clear();
        List<Map<String, Object>> rows = database.executeReadQuery(("SELECT * FROM Consommation"));
        for (Map<String, Object> row : rows) {
            int stayID = (int) row.get("stay_id");
            String conso = (String) row.get("conso");

            if (stayID == this.id) {
                this.consomation.add(conso);
            }
        }
        return this.consomation.toArray(new String[0]);
    }

    public double[] getPrice() {
        this.price.clear();
        List<Map<String, Object>> rows = database.executeReadQuery(("SELECT * FROM Consommation"));
        for (Map<String, Object> row : rows) {
            int stayID = (int) row.get("stay_id");
            Double price_ = (Double) row.get("price");

            if (stayID == this.id) {
                this.price.add(price_);
            }
        }
        return this.price.stream().mapToDouble(Double::doubleValue).toArray(); // Converti List<Double> en double[]
    }

    public void add(String consomation_, double price_) {
        this.database.executeUpdateQuery("INSERT INTO Consommation (stay_id, conso, price) VALUES (?, ?, ?)",
                new Object[] { this.id, consomation_, price_ });
        this.consomation.add(consomation_);
        this.price.add(price_);
    }

    public void removeConso(int index) {
        this.database.executeUpdateQuery("DELETE FROM Consommation WHERE stay_id = ? AND conso = ? AND price = ?",
                new Object[] { this.id, this.consomation.get(index), this.price.get(index) });
        if (index >= 0 && index < consomation.size()) {
            this.consomation.remove(index);
            this.price.remove(index);
        }
    }

    public String[] getAllBoissons() {
        return this.miniBar.getBoissons();
    }

    public double[] getAllPrices() {
        return this.miniBar.getPrices();
    }

    public double getTotalPrice() {
        double roomPricePerNight = getReservation().getRoom().getPrice();
        long nights = java.time.temporal.ChronoUnit.DAYS.between(
                getReservation().getDuration()[0],
                getReservation().getDuration()[1]);
        double totalRoomPrice = roomPricePerNight * nights;

        double total = totalRoomPrice;
        for (int i = 0; i < getConso().length; i++) {
            total += getPrice()[i];
        }
        return total;
    }
}