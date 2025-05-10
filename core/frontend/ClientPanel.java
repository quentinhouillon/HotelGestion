package core.frontend;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.List;

import core.backend.*;

class ClientDialog extends JDialog {
    ClientManagement clients = new ClientManagement();
    Color mainColor;

    public ClientDialog(Color mainColor) {
        super((Frame) null, "Ajouter un client", true);
        this.mainColor = mainColor;

        initDialog(-1, null);
    }

    public ClientDialog(Color mainColor, Client client, JLabel firstName, JLabel lastName, JLabel phone) {
        super((Frame) null, "Modification", true);
        this.mainColor = mainColor;

        initDialog(client.getID(), client);
        lastName.setText("Nom : " + client.getLastName());
        firstName.setText("Prénom : " + client.getName());
        phone.setText("Téléphone : " + client.getPhone());
    }

    private void initDialog(int ID, Client client) {
        setSize(350, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(this);

        // Panel pour les champs de modification
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField lastNameField = new JTextField();
        lastNameField.setBackground(mainColor);
        lastNameField.setForeground(Color.WHITE);
        lastNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mainColor),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        formPanel.add(new JLabel("Nom :"));
        formPanel.add(lastNameField);

        JTextField firstNameField = new JTextField();
        firstNameField.setBackground(mainColor);
        firstNameField.setForeground(Color.WHITE);
        firstNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mainColor),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        formPanel.add(new JLabel("Prénom :"));
        formPanel.add(firstNameField);

        JTextField phoneField = new JTextField();
        phoneField.setBackground(mainColor);
        phoneField.setForeground(Color.WHITE);
        phoneField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(mainColor),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));
        formPanel.add(new JLabel("Téléphone :"));
        formPanel.add(phoneField);

        if (client != null) {
            firstNameField.setText(client.getName());
            lastNameField.setText(client.getLastName());
            phoneField.setText(client.getPhone());
        }

        this.add(formPanel, BorderLayout.CENTER);

        // Panel pour les boutons
        JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Bouton "Enregistrer" dans la boîte de dialogue
        JButton saveButton = new JButton("Enregistrer");
        UIConstants.createStyledButton(saveButton, UIConstants.GREEN_BUTTON_COLOR, Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Bouton "Annuler" dans la boîte de dialogue
        JButton cancelButton = new JButton("Annuler");
        UIConstants.createStyledButton(cancelButton, UIConstants.RED_BUTTON_COLOR, Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Effets visuels pour le bouton "Annuler"

        saveButton.addActionListener(_ -> {
            String lastName = lastNameField.getText();
            String firstName = firstNameField.getText();
            String phone = phoneField.getText();

            if (!lastName.isEmpty() && !firstName.isEmpty() && !phone.isEmpty()) {
                if (client == null)
                    clients.addClient(lastName, firstName, phone);
                else
                    clients.updateClient(client.getID(), lastName, firstName, phone);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tous les champs doivent être remplis.", "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(_ -> this.dispose());

        dialogButtonPanel.add(saveButton);
        dialogButtonPanel.add(cancelButton);

        this.add(dialogButtonPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    };
}

class ListPanel extends JPanel {
    ClientManagement clients = new ClientManagement();

    public ListPanel(Client client, ActionListener deleteAction, Color mainColor) {
        String lastName = client.getLastName();
        String firstName = client.getName();
        String phone = client.getPhone();

        setLayout(new BorderLayout());
        setBackground(mainColor);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(43, 43, 43), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Panel pour les informations du client
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.setBackground(mainColor); // Panel de couleur
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

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
        UIConstants.createStyledButton(deleteButton, UIConstants.RED_BUTTON_COLOR, Color.WHITE);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        deleteButton.addActionListener(deleteAction);

        // Panel pour contenir les boutons "Modifier" et "Supprimer"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(mainColor);
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10)); // Deux lignes, espacement vertical de 10

        // Bouton pour modifier le client
        JButton modifyButton = new JButton("Modifier");
        UIConstants.createStyledButton(modifyButton, UIConstants.GREEN_BUTTON_COLOR, Color.WHITE);
        modifyButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Ajouter une action pour le bouton "Modifier"
        modifyButton.addActionListener(_ -> {
            new ClientDialog(mainColor, client, firstNameLabel, nameLabel, phoneLabel);
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
        reloadClientList(listContainer);

        // Ajouter le conteneur directement au panneau principal
        add(listContainer, BorderLayout.CENTER);

        // Ajouter un footer panel avec un bouton "Ajouter"
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(null);

        // Ajouter un onglet de recherche à gauche du footerPanel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        searchPanel.setBackground(null);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setMinimumSize(new Dimension(200, 40));
        searchField.setMaximumSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchField.setBackground(listColor);
        searchField.setForeground(Color.WHITE); 
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(listColor),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));

        JButton searchButton = new JButton("Rechercher");

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }
        });
        UIConstants.createStyledButton(searchButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Taille uniforme pour les boutons
        Dimension buttonSize = new Dimension(40, 40);

        // Bouton "Rechercher"
        searchButton.setPreferredSize(buttonSize);
        searchButton.setMinimumSize(buttonSize);
        searchButton.setMaximumSize(buttonSize);

        // Taille spécifique pour le bouton "Rechercher"
        Dimension searchButtonSize = new Dimension(100, 40);
        searchButton.setPreferredSize(searchButtonSize);
        searchButton.setMinimumSize(searchButtonSize);
        searchButton.setMaximumSize(searchButtonSize);

        // Action pour le bouton "Rechercher"
        searchButton.addActionListener(_ -> {
            String query = searchField.getText().toLowerCase();
            listContainer.removeAll();
            Client[] filteredClients = clients.searchClientByName(query);

            for (Client client : filteredClients) {
                ListPanel clientRow = new ListPanel(client, _ -> {
                    clients.removeClient(client);
                    reloadClientList(listContainer);
                }, listColor);

                // Appliquer les dimensions dynamiques comme dans reloadClientList
                clientRow.setPreferredSize(new Dimension(listContainer.getWidth(), 100)); 
                clientRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                clientRow.setMinimumSize(new Dimension(0, 100));

                listContainer.add(clientRow);
                listContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement
            }

            listContainer.revalidate();
            listContainer.repaint();
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Déclarez et initialisez sortPanel avant de l'utiliser
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        sortPanel.setBackground(null);

        Dimension sortButtonSize = new Dimension(40, 40);

        // Bouton "Tri Ascendant"
        JButton sortAscButton = new JButton("▲");
        UIConstants.createStyledButton(sortAscButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        sortAscButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        sortAscButton.setPreferredSize(sortButtonSize);
        sortAscButton.setMinimumSize(sortButtonSize);
        sortAscButton.setMaximumSize(sortButtonSize);

        // Action pour trier par ordre alphabétique ascendant
        sortAscButton.addActionListener(_ -> {
            clients.sortBy((c1, c2) -> c1.getLastName().compareToIgnoreCase(c2.getLastName()));
            reloadClientList(listContainer);
        });

        // Bouton "Tri Descendant"
        JButton sortDescButton = new JButton("▼");
        UIConstants.createStyledButton(sortDescButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        sortDescButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        sortDescButton.setPreferredSize(sortButtonSize);
        sortDescButton.setMinimumSize(sortButtonSize);
        sortDescButton.setMaximumSize(sortButtonSize);

        // Action pour trier par ordre alphabétique descendant
        sortDescButton.addActionListener(_ -> {
            clients.sortBy((c1, c2) -> c2.getLastName().compareToIgnoreCase(c1.getLastName()));
            reloadClientList(listContainer);
        });

        // Bouton "Tri par défaut"
        JButton sortDefaultButton = new JButton("►");
        UIConstants.createStyledButton(sortDefaultButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        sortDefaultButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        sortDefaultButton.setPreferredSize(sortButtonSize);
        sortDefaultButton.setMinimumSize(sortButtonSize);
        sortDefaultButton.setMaximumSize(sortButtonSize);

        // Action pour réinitialiser l'ordre des clients
        sortDefaultButton.addActionListener(_ -> {
            clients.sortBy((c1, c2) -> Integer.compare(c1.getID(), c2.getID()));
            reloadClientList(listContainer);
        });

        JButton invertSortDefaultButton = new JButton("◄");
        UIConstants.createStyledButton(invertSortDefaultButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        invertSortDefaultButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        invertSortDefaultButton.setPreferredSize(sortButtonSize);
        invertSortDefaultButton.setMinimumSize(sortButtonSize);
        invertSortDefaultButton.setMaximumSize(sortButtonSize);

        // Action pour réinitialiser l'ordre des clients
        invertSortDefaultButton.addActionListener(_ -> {
            clients.sortBy((c1, c2) -> Integer.compare(c2.getID(), c1.getID()));
            reloadClientList(listContainer);
        });

        // Ajouter les boutons au sortPanel
        sortPanel.add(sortAscButton);
        sortPanel.add(sortDescButton);
        sortPanel.add(sortDefaultButton);
        sortPanel.add(invertSortDefaultButton);

        // Ajouter sortPanel au searchPanel
        searchPanel.add(sortPanel);

        // Ajouter le searchPanel au footerPanel
        footerPanel.add(searchPanel, BorderLayout.WEST);

        // Bouton "Ajouter"
        JButton addButton = new JButton("Ajouter");
        UIConstants.createStyledButton(addButton, UIConstants.PURPLE_BUTTON_COLOR, Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.setPreferredSize(buttonSize);
        addButton.setMinimumSize(buttonSize);
        addButton.setMaximumSize(buttonSize);

        // Taille spécifique pour le bouton "Ajouter"
        Dimension addButtonSize = new Dimension(100, 40);
        addButton.setPreferredSize(addButtonSize);
        addButton.setMinimumSize(addButtonSize);
        addButton.setMaximumSize(addButtonSize);

        // Action pour le bouton "Ajouter"
        addButton.addActionListener(_ -> {
            new ClientDialog(accentColor);
            reloadClientList(listContainer);
        });

        footerPanel.add(addButton, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void reloadClientList(JPanel listContainer) {
        listContainer.removeAll();

        for (Client client : clients.getAll()) {
            final ListPanel[] clientRowWrapper = new ListPanel[1];
            clientRowWrapper[0] = new ListPanel(client, _ -> {
                // JDialog de suppression
                JDialog confirmDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Confirmation",
                        Dialog.ModalityType.APPLICATION_MODAL);
                confirmDialog.setSize(300, 150);
                confirmDialog.setLayout(new BorderLayout());
                confirmDialog.setLocationRelativeTo(this);

                JLabel confirmLabel = new JLabel("Êtes-vous sûr de vouloir supprimer ce client ?");
                confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
                confirmDialog.add(confirmLabel, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

                JButton yesButton = new JButton("Oui");
                UIConstants.createStyledButton(yesButton, UIConstants.GREEN_BUTTON_COLOR, Color.WHITE);
                yesButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                yesButton.addActionListener(_ -> {
                    clients.removeClient(client);
                    listContainer.remove(clientRowWrapper[0]);
                    listContainer.revalidate();
                    reloadClientList(listContainer);
                    confirmDialog.dispose();
                });

                JButton noButton = new JButton("Non");
                UIConstants.createStyledButton(noButton, UIConstants.RED_BUTTON_COLOR, Color.WHITE);
                noButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                noButton.addActionListener(_ -> confirmDialog.dispose());

                buttonPanel.add(yesButton);
                buttonPanel.add(noButton);

                confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
                confirmDialog.setVisible(true);
            }, listColor);
            ListPanel clientRow = clientRowWrapper[0];

            // Définir une taille fixe pour chaque case client
            clientRow.setPreferredSize(new Dimension(listContainer.getWidth(), 100));
            clientRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            clientRow.setMinimumSize(new Dimension(0, 100));
            listContainer.add(clientRow);
            listContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        listContainer.revalidate();
        listContainer.repaint();
    }
}