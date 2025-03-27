package core.backend;
import java.util.ArrayList;
import java.util.List;


class MiniBar {
    static final String[] boissons = {"Eau", "Soda", "Jus de fruit","Thé",
                                      "Café", "Vin blanc", "Vin rouge",
                                      "Vin rosé", "Bière", "Champagne"};
    static final double[] prices = {3.0, 4.25, 3.5, 4.0, 4.0, 12.0, 12.0,
                                    12., 8.5, 17.5};

    public String[] getBoisson() {
        return boissons;
    }

    public double[] getPrices() {
        return prices;
    }
}

class Payment {
    static final String[] allPaymentMode = {"Carte bleue", "Espèce", "Chèque"};
    String PaymentMode;
    double price;

    public Payment(String paymentMode, double price) {
        this.PaymentMode = paymentMode;
        this.price = price;
    }
}

public class Stay {
    Client client;
    Payment payment;
    List<String> consomation;
    List<Double> price;

    public Stay(Client client, Payment payment) {
        this.client = client;
        this.payment = payment;
        this.consomation = new ArrayList<>();
        this.price = new ArrayList<>();
    }

    public Client getClient() {
        return client;
    }

    public Payment getPayment() {
        return payment;
    }

    public String[] getConso() {
        return this.consomation.toArray(new String[0]);
    }

    public double[] getPrice() {
        return this.price.stream().mapToDouble(Double::doubleValue).toArray(); // Converti List<Double> en double[]
    }

    public void add(String consomation_, double price_) {
        this.consomation.add(consomation_);
        this.price.add(price_);
    }

    public void delete(int ID) {
        if (ID >= 0 && ID < this.consomation.size()) {
            this.consomation.remove(ID);
            this.price.remove(ID);
        }
    }
}