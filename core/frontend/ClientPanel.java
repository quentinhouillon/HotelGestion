package core.frontend;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

import core.backend.*;

class ListPanel extends JPanel {
    public ListPanel(String lastName, String firstName, String phone, ActionListener deleteAction, Color mainColor) {
        setLayout(new BorderLayout());
        setBackground(mainColor);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(43, 43, 43), 1), // Bordure extérieure
            BorderFactory.createEmptyBorder(10, 10, 10, 10) // Marges internes
        ));
        // Panel pour les informations du client
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.setBackground(mainColor); //panel de couleur
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Espacement à droite

        JLabel nameLabel = new JLabel("Nom : " + lastName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(nameLabel);

        JLabel firstNameLabel = new JLabel("Prénom : " + firstName);
        firstNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(firstNameLabel);

        JLabel phoneLabel = new JLabel("Téléphone : " + phone);
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

        // Panel pour contenir les boutons "Modifier" et "Supprimer"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0)); // Alignement à droite avec un espacement horizontal
        buttonPanel.setBackground(mainColor); 
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10)); // Deux lignes, espacement vertical de 10

        // Bouton pour modifier le client
        JButton modifyButton = new JButton("Modifier");
        modifyButton.setForeground(Color.WHITE);
        modifyButton.setBackground(new Color(43, 192, 57)); // Vert
        modifyButton.setFocusPainted(false);
        modifyButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes

        // Effets visuels pour le survol et le clic
        modifyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                modifyButton.setBackground(new Color(58, 255, 69)); // Vert plus vif au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                modifyButton.setBackground(new Color(43, 192, 57)); // Retour au vert d'origine
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                modifyButton.setBackground(new Color(0, 139, 0)); // Vert vif foncé lors du clic
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                modifyButton.setBackground(new Color(58, 255, 69)); // Retour à la couleur de survol après le clic
            }
        });

        modifyButton.addActionListener(e -> {
            // Logique de modification du client
            JOptionPane.showMessageDialog(this, "Modification de " + firstName + " " + lastName);
        });

        // Ajout des boutons au panel
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        // Ajout du panel des boutons à l'est
        add(buttonPanel, BorderLayout.EAST);
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
        titleLabel.setForeground(Color.WHITE); 
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel pour la liste des clients
        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        clients.addClient("Dupoont", "Jean", "0123456789");
        clients.addClient("Martin", "Sophie", "0987654321");
        clients.addClient("Durand", "Pierre", "0147258369");
        clients.addClient("Leroy", "Marie", "0162837465");

        for (Client client : clients.getAll()) {
            String lastName = client.getLastName();
            String firstName = client.getName();    
            String phone = client.getPhone();

            ListPanel clientRow = new ListPanel(lastName, firstName, phone, e -> {
                // Supprimer le client
                Component source = (Component) e.getSource();
                Container parent = source.getParent();
                listContainer.remove(parent);
                listContainer.revalidate();
                listContainer.repaint();
            }, listColor);

            // Définir une hauteur fixe pour chaque cadre, mais pas la largeur
            clientRow.setPreferredSize(new Dimension(clientRow.getPreferredSize().width, 100));
            clientRow.setMinimumSize(new Dimension(clientRow.getMinimumSize().width, 100));
            clientRow.setMaximumSize(new Dimension(clientRow.getMaximumSize().width, 100));

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