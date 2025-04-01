package core.frontend;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

import core.backend.*;

class ListPanel extends JPanel {
    public ListPanel(String lastName, String firstName, String phone, ActionListener deleteAction) {
        setLayout(new BorderLayout());
        setBackground(new Color(60, 63, 65)); // Couleur de fond des cadres (alignée avec CardPanel)
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(43, 43, 43), 1), // Bordure sombre (alignée avec CardPanel)
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Marges internes
        ));

        // Panel pour les informations du client
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.setBackground(new Color(60, 63, 65)); // Même couleur que le fond
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Espacement à droite

        JLabel nameLabel = new JLabel("Nom : " + lastName);
        nameLabel.setForeground(new Color(187, 187, 187)); // Texte gris clair (aligné avec CardPanel)
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(nameLabel);

        JLabel firstNameLabel = new JLabel("Prénom : " + firstName);
        firstNameLabel.setForeground(new Color(187, 187, 187)); // Texte gris clair
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(firstNameLabel);

        JLabel phoneLabel = new JLabel("Téléphone : " + phone);
        phoneLabel.setForeground(new Color(187, 187, 187)); // Texte gris clair
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(phoneLabel);

        add(infoPanel, BorderLayout.CENTER);

        // Bouton pour supprimer le client
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(192, 57, 43)); // Rouge vif
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes

        // Effets visuels pour le survol et le clic
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(255, 69, 58)); // Rouge plus clair au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(192, 57, 43)); // Retour à la couleur d'origine
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(139, 0, 0)); // Rouge foncé lors du clic
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(new Color(255, 69, 58)); // Retour à la couleur de survol après le clic
            }
        });

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

        // Titre
        JLabel titleLabel = new JLabel("Liste des Clients");
        titleLabel.setForeground(new Color(187, 187, 187)); // Texte gris clair
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel pour la liste des clients
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Exemple de données de clients
        String[][] clientData = {
            {"Dupont", "Denis", "0601020304"},
            {"Martin", "Sophie", "0605060708"},
            {"Durand", "Paul", "0611121314"},
            {"Bernard", "Claire", "0615161718"}
        };

        for (String[] client : clientData) {
            String lastName = client[0];
            String firstName = client[1];
            String phone = client[2];

            ListPanel clientRow = new ListPanel(lastName, firstName, phone, e -> {
                // Supprimer le client
                Component source = (Component) e.getSource();
                Container parent = source.getParent();
                listContainer.remove(parent);
                listContainer.revalidate();
                listContainer.repaint();
            });
            listContainer.add(clientRow);
            listContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement entre les cadres
        }

        // Ajout de la liste dans un JScrollPane pour le défilement
        JScrollPane scrollPane = new JScrollPane(listContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Supprime la bordure par défaut
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Défilement fluide
        add(scrollPane, BorderLayout.CENTER);
    }
}