package core.backend;
import java.util.ArrayList;
import java.util.List;

class Client {
    String lastName;
    String name;
    String phone;

    public Client(String lastName, String name, String phone) {
        this.lastName = lastName;
        this.name = name;
        this.phone = phone;
    }

    public String[] detail() {
        return new String[]{lastName, name, phone};
    }
}   

public class ClientManagement {
    List<Client> clients;

    public ClientManagement() {
        this.clients = new ArrayList<>();
    }

    public void addClient(String lastName, String name, String phone) {
        this.clients.add(new Client(lastName, name, phone));
    }

    public void delClient(int ID) {
        if (ID >= 0 && ID < this.clients.size()) {
            this.clients.remove(ID);
        }
    }

    public void updateClient(int ID, String lastName, String name, String phone) {
        if (ID >= 0 && ID < this.clients.size()) {
            this.clients.get(ID).lastName = lastName;
            this.clients.get(ID).name = name;
            this.clients.get(ID).phone = phone;
        }
    }

    public Client[] getAll() {
        return this.clients.toArray(new Client[0]);
    }

    public Client getClientById(int ID) {
        if (ID >= 0 && ID < this.clients.size()) {
            return this.clients.get(ID);
        }
        return null;
    }

    public Client[] searchClientByName(String name) {
        return this.clients.stream()
                  .filter(client -> client.name.equalsIgnoreCase(name))
                  .toArray(Client[]::new);
    }
}
