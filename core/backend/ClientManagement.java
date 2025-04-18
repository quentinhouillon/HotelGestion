package core.backend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClientManagement {
    static List<Client> clients = clients = new ArrayList<>();


    public Client[] getAll() {
        return clients.toArray(Client[]::new);
    }

    public void addClient(String lastName, String firstName, String phone) {
        Client client = new Client(lastName, firstName, phone);
        clients.add(client);
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public void updateClient(int ID, String lastName, String name, String phone) {
        for (Client client : clients) {
            if (client.getID() == ID) {
                client.setLastName(lastName);
                client.setName(name);
                client.setPhone(phone);
                break;
            }
        }
    }

    public Client getClientById(int ID) {
        if (ID >= 0 && ID < clients.size()) {
            return clients.get(ID);
        }
        return null;
    }

    public Client[] searchClientByName(String query) {
        final String lowerCaseQuery = query.toLowerCase();
        return clients.stream()
            .filter(client -> client.getLastName().toLowerCase().contains(lowerCaseQuery) ||
                      client.getName().toLowerCase().contains(lowerCaseQuery) ||
                      client.getPhone().toLowerCase().contains(lowerCaseQuery))
            .toArray(Client[]::new);
    }

    public void sortBy(Comparator<Client> comparator) {
        clients.sort(comparator);
    }
}
