package core.frontend;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import core.backend.*;

class ListPanel extends JPanel {
    public ListPanel(String clientName, ActionListener deleteAction) {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50)); // Couleur de fond des lignes
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes

        // Label pour afficher le nom du client
        JLabel nameLabel = new JLabel(clientName);
        nameLabel.setForeground(Color.WHITE); // Couleur du texte
        add(nameLabel, BorderLayout.CENTER);

        // Bouton pour supprimer le client
        JButton deleteButton = new JButton("X");
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(100, 0, 0));
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(deleteAction);
        add(deleteButton, BorderLayout.EAST);
    }
}

public class ClientPanel extends JPanel {
    Color listColor;
    ClientManagement clients = new ClientManagement();

    public ClientPanel(Color accentColor) {
        this.listColor = accentColor;
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30)); // Couleur de fond du panel principal

        // Titre
        JLabel titleLabel = new JLabel("Clients");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel pour la liste des clients
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(new Color(30, 30, 30)); // Même couleur que le fond principal

        // Exemple de données de clients
        String[] clientNames = {"Client 1", "Client 2", "Client 3", "Client 4"};

        for (String clientName : clientNames) {
            ListPanel clientRow = new ListPanel(clientName, e -> {
                // Utilisation d'une variable temporaire pour éviter les problèmes de portée
                Component source = (Component) e.getSource();
                Container parent = source.getParent();
                listContainer.remove(parent);
                listContainer.revalidate();
                listContainer.repaint();
            });
            listContainer.add(clientRow);
        }

        // Ajout de la liste dans un JScrollPane pour le défilement
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(null); // Supprime la bordure par défaut
        add(scrollPane, BorderLayout.CENTER);
    }
}