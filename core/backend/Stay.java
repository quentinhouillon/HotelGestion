package core.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import core.model.Database;

class MiniBar {
    static final String[] boissons = {"Eau", "Soda", "Jus de fruit","Thé",
                                      "Café", "Vin blanc", "Vin rouge",
                                      "Vin rosé", "Bière", "Champagne"};
    static final double[] prices = {3.0, 4.25, 3.50, 4.0, 4.0, 12.0, 12.0,
                                    12.0, 8.50, 17.50};

    public String[] getBoissons() {
        return boissons;
    }

    public double[] getPrices() {
        return prices;
    }
}

class Payment {
    static final String[] allPaymentMode = {"Carte bleue", "Espèce", "Chèque"};
    String PaymentMode;

    public Payment(String paymentMode, double price) {
        this.PaymentMode = paymentMode;
    }

    public String[] getAllPayments() {
        return allPaymentMode;
    }
}

public class Stay {
    int id;
    Reservation reservation;
    Payment payment;
    List<String> consomation;
    List<Double> price;
    Database database;
    static MiniBar miniBar;
    static Payment payments;

    public Stay(int id, Reservation reservation) {
        this.id = id;
        this.reservation = reservation;
        this.consomation = new ArrayList<>();
        this.price = new ArrayList<>();
        this.database = new Database();
        miniBar = new MiniBar();
    }

    public Client getClient() {
        return this.reservation.getClient();
    }

    public Reservation getReservation() {
        return this.reservation;
    }

    public Payment getPayment() {
        return this.payment;
    }

    public int getID() {
        return this.id;
    }

    public void setPayment(Payment payment_) {
        this.payment = payment_;
    }

    public String[] getConso(int stayID_) {
        this.consomation.clear();
        List<Map<String, Object>> rows = database.executeReadQuery(("SELECT * FROM Consommation"));
        for (Map<String, Object> row : rows) {
            int stayID = (int) row.get("stay_id");
            String conso = (String) row.get("conso");

            if (stayID == stayID_) {
                this.consomation.add(conso);
            }
        }
        return this.consomation.toArray(new String[0]);
    }

    public double[] getPrice(int stayID_) {
        this.price.clear();
        List<Map<String, Object>> rows = database.executeReadQuery(("SELECT * FROM Consommation"));
        for (Map<String, Object> row : rows) {
            int stayID = (int) row.get("stay_id");
            Double price_ = (Double) row.get("price");

            if (stayID == stayID_) {
                this.price.add(price_);
            }
        }
        return this.price.stream().mapToDouble(Double::doubleValue).toArray(); // Converti List<Double> en double[]
    }

    public void add(String consomation_, double price_) {
        this.database.executeUpdateQuery("INSERT INTO Consommation (stay_id, conso, price) VALUES (?, ?, ?)",
                new Object[] {this.id, consomation_, price_});
        this.consomation.add(consomation_);
        this.price.add(price_);
    }

    public void delete(int ID) {
        if (ID >= 0 && ID < this.consomation.size()) {
            this.consomation.remove(ID);
            this.price.remove(ID);
        }
    }

    public void removeConso(int index) {
        if (index >= 0 && index < consomation.size()) { 
            consomation.remove(index); 
            price.remove(index); 
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
            getReservation().getDuration()[1]
        );
        double totalRoomPrice = roomPricePerNight * nights;

        double total = totalRoomPrice;
        for (int i = 0; i < getConso().length; i++) {
            total += getPrice()[i];
        }
        return total;
    }
}