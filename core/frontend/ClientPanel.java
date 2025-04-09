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
        infoPanel.setBackground(mainColor); // Panel de couleur
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
        deleteButton.setBackground(UIConstants.RED_BUTTON_COLOR);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes

        // Effets visuels pour le bouton "Supprimer"
        deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(UIConstants.RED_HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(UIConstants.RED_BUTTON_COLOR);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(UIConstants.RED_CLICK_COLOR);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                deleteButton.setBackground(UIConstants.RED_HOVER_COLOR);
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
        modifyButton.setBackground(UIConstants.GREEN_BUTTON_COLOR);
        modifyButton.setFocusPainted(false);
        modifyButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes

        // Effets visuels pour le bouton "Modifier"
        modifyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                modifyButton.setBackground(UIConstants.GREEN_HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                modifyButton.setBackground(UIConstants.GREEN_BUTTON_COLOR);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                modifyButton.setBackground(UIConstants.GREEN_CLICK_COLOR);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                modifyButton.setBackground(UIConstants.GREEN_HOVER_COLOR);
            }
        });

        // Ajouter une action pour le bouton "Modifier" (exemple)
        modifyButton.addActionListener(_ -> {
            // Créer une fenêtre JDialog pour modifier les informations du client
            JDialog modifyDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Modifier le client", Dialog.ModalityType.APPLICATION_MODAL);
            modifyDialog.setSize(300, 200);
            modifyDialog.setLayout(new BorderLayout());
            modifyDialog.setLocationRelativeTo(this);

            // Panel pour les champs de modification
            JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            formPanel.add(new JLabel("Nom :"));
            JTextField lastNameField = new JTextField(lastName);
            lastNameField.setBackground(mainColor);
            lastNameField.setForeground(Color.WHITE);
            lastNameField.setBorder(null);
            formPanel.add(lastNameField);

            formPanel.add(new JLabel("Prénom :"));
            JTextField firstNameField = new JTextField(firstName);
            firstNameField.setBackground(mainColor);
            firstNameField.setForeground(Color.WHITE);
            firstNameField.setBorder(null);
            formPanel.add(firstNameField);

            formPanel.add(new JLabel("Téléphone :"));
            JTextField phoneField = new JTextField(phone);
            phoneField.setBackground(mainColor);
            phoneField.setForeground(Color.WHITE);
            phoneField.setBorder(null);
            formPanel.add(phoneField);

            modifyDialog.add(formPanel, BorderLayout.CENTER);

            // Panel pour les boutons
            JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            // Bouton "Enregistrer" dans la boîte de dialogue
            JButton saveButton = new JButton("Enregistrer");
            saveButton.setForeground(Color.WHITE);
            saveButton.setBackground(UIConstants.GREEN_BUTTON_COLOR);
            saveButton.setFocusPainted(false);
            saveButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes

            // Effets visuels pour le bouton "Enregistrer"
            saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    saveButton.setBackground(UIConstants.GREEN_HOVER_COLOR);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    saveButton.setBackground(UIConstants.GREEN_BUTTON_COLOR);
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    saveButton.setBackground(UIConstants.GREEN_CLICK_COLOR);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    saveButton.setBackground(UIConstants.GREEN_HOVER_COLOR);
                }
            });

            // Bouton "Annuler" dans la boîte de dialogue
            JButton cancelButton = new JButton("Annuler");
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setBackground(UIConstants.RED_BUTTON_COLOR);
            cancelButton.setFocusPainted(false);
            cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes

            // Effets visuels pour le bouton "Annuler"
            cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    cancelButton.setBackground(UIConstants.RED_HOVER_COLOR);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    cancelButton.setBackground(UIConstants.RED_BUTTON_COLOR);
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    cancelButton.setBackground(UIConstants.RED_CLICK_COLOR);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    cancelButton.setBackground(UIConstants.RED_HOVER_COLOR);
                }
            });

            saveButton.addActionListener(_ -> {
                // Mettre à jour les informations du client
                String newLastName = lastNameField.getText();
                String newFirstName = firstNameField.getText();
                String newPhone = phoneField.getText();

                nameLabel.setText("Nom : " + newLastName);
                firstNameLabel.setText("Prénom : " + newFirstName);
                phoneLabel.setText("Téléphone : " + newPhone);

                modifyDialog.dispose();
            });

            cancelButton.addActionListener(_ -> modifyDialog.dispose());

            dialogButtonPanel.add(saveButton);
            dialogButtonPanel.add(cancelButton);

            modifyDialog.add(dialogButtonPanel, BorderLayout.SOUTH);

            modifyDialog.setVisible(true);
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

        // Ajouter les clients au conteneur
        clients.addClient("Dupoont", "Jean", "0123456789");
        clients.addClient("Martin", "Sophie", "0987654321");
        clients.addClient("Durand", "Pierre", "0147258369");
        clients.addClient("Dupuis", "Marie", "0678901234");

        reloadClientList(listContainer);

        // Ajouter le conteneur directement au panneau principal
        add(listContainer, BorderLayout.CENTER);

        // Ajouter un footer panel avec un bouton "Ajouter"
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges internes
        footerPanel.setBackground(null);
        
        // Ajouter un onglet de recherche à gauche du footerPanel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Espacement à droite
        searchPanel.setBackground(null);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 30)); // Taille du champ de recherche
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1), // Bordure extérieure
            BorderFactory.createEmptyBorder(5, 5, 5, 5)   // Marges internes
        ));

        JButton searchButton = new JButton("Rechercher");

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    // Simuler un clic sur le bouton "Rechercher"
                    searchButton.doClick();
                }
            }
        });
        searchButton.setForeground(Color.WHITE);
        searchButton.setBackground(UIConstants.BLUE_BUTTON_COLOR);
        searchButton.setFocusPainted(false);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes

        // Action pour le bouton "Rechercher"
        searchButton.addActionListener(_ -> {
            String query = searchField.getText().toLowerCase(); // Récupérer la chaîne de recherche
            listContainer.removeAll(); // Supprime tous les composants existants

            for (Client client : clients.getAll()) {
                String lastName = client.getLastName().toLowerCase();
                String firstName = client.getName().toLowerCase();
                String phone = client.getPhone().toLowerCase();

                // Vérifier si la chaîne de recherche correspond au nom, prénom ou numéro
                if (lastName.contains(query) || firstName.contains(query) || phone.contains(query)) {
                    ListPanel clientRow = new ListPanel(client.getLastName(), client.getName(), client.getPhone(), e -> {
                        clients.removeClient(client);
                        reloadClientList(listContainer);
                    }, listColor);

                    // Appliquer les dimensions dynamiques comme dans reloadClientList
                    clientRow.setPreferredSize(new Dimension(listContainer.getWidth(), 100)); // Largeur dynamique, Hauteur : 100px
                    clientRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Largeur maximale, Hauteur fixe
                    clientRow.setMinimumSize(new Dimension(0, 100)); // Largeur minimale, Hauteur fixe

                    listContainer.add(clientRow);
                    listContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement
                }
            }

            listContainer.revalidate();
            listContainer.repaint();
        });

        // Ajouter des effets visuels pour le bouton "Rechercher"
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(UIConstants.BLUE_HOVER_COLOR); // Couleur au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(UIConstants.BLUE_BUTTON_COLOR); // Couleur par défaut
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(UIConstants.BLUE_CLICK_COLOR); // Couleur lorsqu'il est pressé
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                searchButton.setBackground(UIConstants.BLUE_HOVER_COLOR); // Retour à la couleur au survol
            }
        });

        // Ajouter le champ de recherche et le bouton au searchPanel
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Ajouter le searchPanel au footerPanel
        footerPanel.add(searchPanel, BorderLayout.WEST);

        // Bouton "Ajouter"
        JButton addButton = new JButton("Ajouter");
        addButton.setForeground(Color.WHITE);
        addButton.setBackground(UIConstants.PURPLE_BUTTON_COLOR);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Marges internes

        // Effets visuels pour le bouton "Ajouter"
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addButton.setBackground(UIConstants.PURPLE_HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addButton.setBackground(UIConstants.PURPLE_BUTTON_COLOR);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addButton.setBackground(UIConstants.PURPLE_CLICK_COLOR);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                addButton.setBackground(UIConstants.PURPLE_HOVER_COLOR);
            }
        });

        // Action pour le bouton "Ajouter"
        addButton.addActionListener(_ -> {
            JDialog addDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Ajouter un client", Dialog.ModalityType.APPLICATION_MODAL);
            addDialog.setSize(300, 200);
            addDialog.setLayout(new BorderLayout());
            addDialog.setLocationRelativeTo(this);

            JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
            formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            formPanel.add(new JLabel("Nom :"));
            JTextField lastNameField = new JTextField();
            lastNameField.setBackground(accentColor);
            lastNameField.setForeground(Color.WHITE);
            lastNameField.setBorder(null);
            formPanel.add(lastNameField);

            formPanel.add(new JLabel("Prénom :"));
            JTextField firstNameField = new JTextField();
            firstNameField.setBackground(accentColor);
            firstNameField.setForeground(Color.WHITE);
            firstNameField.setBorder(null);
            formPanel.add(firstNameField);

            formPanel.add(new JLabel("Téléphone :"));
            JTextField phoneField = new JTextField();
            phoneField.setBackground(accentColor);
            phoneField.setForeground(Color.WHITE);
            phoneField.setBorder(null);
            formPanel.add(phoneField);

            addDialog.add(formPanel, BorderLayout.CENTER);

            JPanel dialogButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            JButton saveButton = new JButton("Enregistrer");
            saveButton.setForeground(Color.WHITE);
            saveButton.setBackground(UIConstants.GREEN_BUTTON_COLOR);
            saveButton.setFocusPainted(false);
            saveButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            saveButton.addActionListener(_ -> {
                String lastName = lastNameField.getText();
                String firstName = firstNameField.getText();
                String phone = phoneField.getText();

                if (!lastName.isEmpty() && !firstName.isEmpty() && !phone.isEmpty()) {
                    clients.addClient(lastName, firstName, phone);
                    reloadClientList(listContainer); 
                    addDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(addDialog, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            JButton cancelButton = new JButton("Annuler");
            cancelButton.setForeground(Color.WHITE);
            cancelButton.setBackground(UIConstants.RED_BUTTON_COLOR);
            cancelButton.setFocusPainted(false);
            cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            cancelButton.addActionListener(_ -> addDialog.dispose());

            dialogButtonPanel.add(saveButton);
            dialogButtonPanel.add(cancelButton);

            addDialog.add(dialogButtonPanel, BorderLayout.SOUTH);

            addDialog.setVisible(true);
        });

        // Ajouter le bouton "Ajouter" au footer panel
        footerPanel.add(addButton, BorderLayout.EAST);

        // Ajouter le footer panel au bas du panneau principal
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void reloadClientList(JPanel listContainer) {
        listContainer.removeAll(); // Supprime tous les composants existants

        for (Client client : clients.getAll()) {
            String lastName = client.getLastName();
            String firstName = client.getName();
            String phone = client.getPhone();

            ListPanel clientRow = new ListPanel(lastName, firstName, phone, e -> {
                // Logique de suppression
                JDialog confirmDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Confirmation", Dialog.ModalityType.APPLICATION_MODAL);
                confirmDialog.setSize(300, 150);
                confirmDialog.setLayout(new BorderLayout());
                confirmDialog.setLocationRelativeTo(this);

                JLabel confirmLabel = new JLabel("Êtes-vous sûr de vouloir supprimer ce client ?");
                confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
                confirmDialog.add(confirmLabel, BorderLayout.CENTER);

                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

                JButton yesButton = new JButton("Oui");
                yesButton.setForeground(Color.WHITE);
                yesButton.setBackground(UIConstants.GREEN_BUTTON_COLOR);
                yesButton.setFocusPainted(false);
                yesButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                yesButton.addActionListener(_ -> {
                    clients.removeClient(client); 
                    reloadClientList(listContainer); 
                    confirmDialog.dispose();
                });

                JButton noButton = new JButton("Non");
                noButton.setForeground(Color.WHITE);
                noButton.setBackground(UIConstants.RED_BUTTON_COLOR);
                noButton.setFocusPainted(false);
                noButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                noButton.addActionListener(_ -> confirmDialog.dispose());

                buttonPanel.add(yesButton);
                buttonPanel.add(noButton);

                confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
                confirmDialog.setVisible(true);
            }, listColor);

            // Définir une taille fixe pour chaque case client
            clientRow.setPreferredSize(new Dimension(listContainer.getWidth(), 100)); // Largeur dynamique, Hauteur : 100px
            clientRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // Largeur maximale, Hauteur fixe
            clientRow.setMinimumSize(new Dimension(0, 100)); // Largeur minimale, Hauteur fixe

            listContainer.add(clientRow);
            listContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement entre les cases
        }

        listContainer.revalidate(); 
        listContainer.repaint(); 
    }
}