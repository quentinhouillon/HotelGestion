package src.controler;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import src.model.*;

import java.util.Map;

public class ClientManagement {
    static List<Client> clients = new ArrayList<>();
    private Database database = new Database();


    public Client[] getAll() {
        clients.clear();
        List<Map<String, Object>> rows = database.executeReadQuery("SELECT * FROM Client");
        for (Map<String, Object> row : rows) {
            int id = (int) row.get("id");
            String lastName = (String) row.get("lastName");
            String firstName = (String) row.get("firstName");
            String phone = (String) row.get("phone");
            clients.add(new Client(id, lastName, firstName, phone));
        }
        return clients.toArray(Client[]::new);
    }

    public void addClient(String lastName, String firstName, String phone) {
        database.executeUpdateQuery("INSERT INTO Client (lastName, firstName, phone) VALUES (?, ?, ?)",
                new Object[] { lastName, firstName, phone });

        List<Map<String, Object>> rows = database.executeReadQuery("SELECT * FROM Client ORDER BY id DESC LIMIT 1");
        int id = -1;
        if (!rows.isEmpty()) {
            id = (int) rows.get(0).get("id");
        }
        clients.add(new Client(id, lastName, firstName, phone));
    }

    public void removeClient(Client client) {
        clients.remove(client);
        database.executeUpdateQuery("DELETE FROM Client WHERE id = ?", new Object[] { client.getID() });
    }
    
    public void updateClient(int ID, String lastName, String name, String phone) {
        database.executeUpdateQuery("UPDATE Client SET lastName = ?, firstName = ?, phone = ? WHERE id = ?",
        new Object[] { lastName, name, phone, ID });
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
        for (Client client : clients) {
            if (client.getID() == ID) {
                return client;
            }
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

    public Client getByName(String name) {
        for (Client client : clients) {
            if (client.getName().equalsIgnoreCase(name.split(" ")[0])
                    && client.getLastName().equalsIgnoreCase(name.split(" ")[1])) {
                return client;
            }
        }
        return null;
    }
}
