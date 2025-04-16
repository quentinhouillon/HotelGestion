package core.backend;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClientManagement {
    static List<Client> clients;
    private List<Client> originalOrder = new ArrayList<>();

    public ClientManagement() {
        clients = new ArrayList<>();
    }

    public void addClient(String lastName, String firstName, String phone) {
        Client client = new Client(lastName, firstName, phone);
        clients.add(client);
        originalOrder.add(client); // Sauvegarder l'ordre d'origine
    }

    public void delClient(int ID) {
        if (ID >= 0 && ID < clients.size()) {
            clients.remove(ID);
        }
    }

    public void updateClient(int ID, String lastName, String name, String phone) {
        if (ID >= 0 && ID < clients.size()) {
            clients.get(ID).setLastName(lastName);
            clients.get(ID).setName(name);
            clients.get(ID).setPhone(phone);
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

    public List<Client> searchClientByName(String query) {
        final String lowerCaseQuery = query.toLowerCase();
        return clients.stream()
            .filter(client -> client.getLastName().toLowerCase().contains(lowerCaseQuery) ||
                              client.getName().toLowerCase().contains(lowerCaseQuery) ||
                              client.getPhone().toLowerCase().contains(lowerCaseQuery))
            .collect(Collectors.toList());
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    public void sortBy(Comparator<Client> comparator) {
        clients.sort(comparator);
    }
}
