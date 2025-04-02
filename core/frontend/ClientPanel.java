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
        modifyButton.addActionListener(e -> {
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
            formPanel.add(lastNameField);

            formPanel.add(new JLabel("Prénom :"));
            JTextField firstNameField = new JTextField(firstName);
            formPanel.add(firstNameField);

            formPanel.add(new JLabel("Téléphone :"));
            JTextField phoneField = new JTextField(phone);
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

            saveButton.addActionListener(event -> {
                // Mettre à jour les informations du client
                String newLastName = lastNameField.getText();
                String newFirstName = firstNameField.getText();
                String newPhone = phoneField.getText();

                nameLabel.setText("Nom : " + newLastName);
                firstNameLabel.setText("Prénom : " + newFirstName);
                phoneLabel.setText("Téléphone : " + newPhone);

                modifyDialog.dispose();
            });

            cancelButton.addActionListener(event -> modifyDialog.dispose());

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

        for (Client client : clients.getAll()) {
            String lastName = client.getLastName();
            String firstName = client.getName();
            String phone = client.getPhone();

            ListPanel clientRow = new ListPanel(lastName, firstName, phone, e -> {
                // Créer un JDialog personnalisé pour la confirmation
                JDialog confirmDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Confirmation de suppression", Dialog.ModalityType.APPLICATION_MODAL);
                confirmDialog.setSize(400, 150);
                confirmDialog.setLayout(new BorderLayout());
                confirmDialog.setLocationRelativeTo(this);

                // Message de confirmation
                JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>Êtes-vous sûr de vouloir supprimer ce client ?</div></html>");
                messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                confirmDialog.add(messageLabel, BorderLayout.CENTER);

                // Panel pour les boutons
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

                // Bouton "Oui"
                JButton yesButton = new JButton("Oui");
                yesButton.setForeground(Color.WHITE);
                yesButton.setBackground(UIConstants.GREEN_BUTTON_COLOR);
                yesButton.setFocusPainted(false);
                yesButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Augmenter les marges internes
                yesButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        yesButton.setBackground(UIConstants.GREEN_HOVER_COLOR);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        yesButton.setBackground(UIConstants.GREEN_BUTTON_COLOR);
                    }

                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        yesButton.setBackground(UIConstants.GREEN_CLICK_COLOR);
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        yesButton.setBackground(UIConstants.GREEN_HOVER_COLOR);
                    }
                });
                yesButton.addActionListener(event -> {
                    // Supprimer le client
                    Component source = (Component) e.getSource();
                    ListPanel parentPanel = (ListPanel) SwingUtilities.getAncestorOfClass(ListPanel.class, source);

                    if (parentPanel != null) {
                        int index = listContainer.getComponentZOrder(parentPanel);
                        listContainer.remove(parentPanel);

                        // Supprimer l'espacement associé
                        if (index + 1 < listContainer.getComponentCount()) {
                            Component spacer = listContainer.getComponent(index);
                            if (spacer instanceof Box.Filler) {
                                listContainer.remove(spacer);
                            }
                        }

                        listContainer.revalidate();
                        listContainer.repaint();
                    }

                    confirmDialog.dispose();
                });

                // Bouton "Non"
                JButton noButton = new JButton("Non");
                noButton.setForeground(Color.WHITE);
                noButton.setBackground(UIConstants.RED_BUTTON_COLOR);
                noButton.setFocusPainted(false);
                noButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Augmenter les marges internes
                noButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        noButton.setBackground(UIConstants.RED_HOVER_COLOR);
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        noButton.setBackground(UIConstants.RED_BUTTON_COLOR);
                    }

                    @Override
                    public void mousePressed(java.awt.event.MouseEvent evt) {
                        noButton.setBackground(UIConstants.RED_CLICK_COLOR);
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent evt) {
                        noButton.setBackground(UIConstants.RED_HOVER_COLOR);
                    }
                });
                noButton.addActionListener(event -> confirmDialog.dispose());

                // Ajouter les boutons au panel
                buttonPanel.add(yesButton);
                buttonPanel.add(noButton);

                confirmDialog.add(buttonPanel, BorderLayout.SOUTH);

                // Afficher le JDialog
                confirmDialog.setVisible(true);
            }, listColor);

            // Définir une hauteur fixe pour chaque cadre, mais pas la largeur
            clientRow.setPreferredSize(new Dimension(clientRow.getPreferredSize().width, 100));
            clientRow.setMinimumSize(new Dimension(clientRow.getMinimumSize().width, 100));
            clientRow.setMaximumSize(new Dimension(clientRow.getMaximumSize().width, 100));

            listContainer.add(clientRow);
            listContainer.add(Box.createRigidArea(new Dimension(0, 10))); // Espacement entre les cadres
        }

        // Ajouter le conteneur directement au panneau principal
        add(listContainer, BorderLayout.CENTER);
    }
}