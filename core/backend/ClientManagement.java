package core.backend;

import java.util.ArrayList;
import java.util.List;

public class ClientManagement {
    static List<Client> clients;

    public ClientManagement() {
        clients = new ArrayList<>();
    }

    public void addClient(String lastName, String name, String phone) {
        clients.add(new Client(lastName, name, phone));
    }

    public void delClient(int ID) {
        if (ID >= 0 && ID < clients.size()) {
            clients.remove(ID);
        }
    }

    public void updateClient(int ID, String lastName, String name, String phone) {
        if (ID >= 0 && ID < clients.size()) {
            clients.get(ID).lastName = lastName;
            clients.get(ID).name = name;
            clients.get(ID).phone = phone;
        }
    }

    public Client[] getAll() {
        return clients.toArray(new Client[0]);
    }

    public Client getClientById(int ID) {
        if (ID >= 0 && ID < clients.size()) {
            return clients.get(ID);
        }
        return null;
    }

    public Client[] searchClientByName(String name) {
        return clients.stream()
                  .filter(client -> client.name.equalsIgnoreCase(name))
                  .toArray(Client[]::new);
    }
}
