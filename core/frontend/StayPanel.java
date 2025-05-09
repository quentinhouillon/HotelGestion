package core.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import core.backend.*;

class FormDialog extends JDialog {
    JPanel mainPanel;
    Stay stay;

    public FormDialog(Stay stay_) {
        super((Frame) null, "Formulaire séjour", false);
        this.stay = stay_;

        setSize(700, 550);
        setResizable(false);

        JLabel title = new JLabel("Séjour de Monsieur " + stay.getClient().getLastName());
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        // MAIN
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel consomation = new JLabel("Consommation");
        consomation.setFont(new Font("Arial", Font.PLAIN, 17));
        consomation.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(consomation);

        // FOOTER
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Commander");
        UIConstants.createStyledButton(addButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.addActionListener(_ -> {
            addConsommation();
        });
        footerPanel.add(addButton);

        add(mainPanel, BorderLayout.WEST);
        add(footerPanel, BorderLayout.SOUTH);

        reloadConso();
        setVisible(true);
    }

    private void addConsommation() {
        List<String> allConsommations = new ArrayList<>();
        for (int i = 0; i < stay.getAllBoissons().length; i++) {
            allConsommations.add(stay.getAllBoissons()[i] + " - " + stay.getAllPrices()[i] + "€");
        }

        JDialog dialog = new JDialog((Frame) null, "Ajouter une consommation", false);
        dialog.setSize(350, 150);
        dialog.setLayout(new BorderLayout());
        dialog.setModal(true);
        dialog.setResizable(false);

        JComboBox consoComboBox = new JComboBox<>(allConsommations.toArray(String[]::new));

        JPanel panel = new JPanel();
        panel.setBackground(null);
        panel.setLayout(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("choisir une boisson :"));
        panel.add(consoComboBox);


        JButton confirmButton = new JButton("Ajouter");
        UIConstants.createStyledButton(confirmButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        confirmButton.addActionListener(_ -> {
            int id = consoComboBox.getSelectedIndex();
            if (id >= 0) {
                stay.add(stay.getAllBoissons()[id], stay.getAllPrices()[id]);
                dialog.dispose();
                reloadConso();
            }
        });

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(confirmButton, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void reloadConso() {
        mainPanel.removeAll();
        if (stay.getConso().length == 0) {
            JLabel empty = new JLabel(stay.getClient().getLastName() + " n'a encore rien consommé");
            empty.setForeground(Color.LIGHT_GRAY);
            mainPanel.add(empty);
        } else {
            for (int i = 0; i < stay.getConso().length; i++) {
                JLabel consoLabel = new JLabel(stay.getConso()[i] + " - " + stay.getPrice()[i] + "€");
                consoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                mainPanel.add(consoLabel);
            }
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}

class LsStayPanel extends JPanel {
    StayManagement stays = new StayManagement();

    public LsStayPanel(Stay stay) {
        setLayout(new BorderLayout());
        setBackground(UIConstants.ACCENT_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(43, 43, 43), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Panel pour les informations de la resercation
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.setBackground(null);

        JLabel clientNameLabel = new JLabel("Client: " + stay.getClient().getLastName());
        clientNameLabel.setForeground(Color.WHITE);
        clientNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(clientNameLabel);

        JLabel roomNumberLabel = new JLabel("Chambre n°" + stay.getReservation().getRoom().getRoomNumber());
        roomNumberLabel.setForeground(Color.WHITE);
        roomNumberLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPanel.add(roomNumberLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(null);
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10));

        JButton detailButton = new JButton("Détails");
        UIConstants.createStyledButton(detailButton, UIConstants.BLUE_BUTTON_COLOR, Color.white);
        detailButton.addActionListener(_ -> {
            new FormDialog(stay);
        });

        buttonPanel.add(detailButton);
        add(buttonPanel, BorderLayout.EAST);
        add(infoPanel, BorderLayout.WEST);
    }
}

public class StayPanel extends JPanel {
    StayManagement stays = new StayManagement();
    ReservationManagement reservations = new ReservationManagement();

    public StayPanel() {
        setLayout(new BorderLayout());

        // TITRE
        JLabel titleLabel = new JLabel("Liste des séjours en cours");
        titleLabel.setForeground(Color.WHITE); 
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // FOOTER
        JButton addButton = new JButton("Actualiser");
        UIConstants.createStyledButton(addButton, UIConstants.PURPLE_BUTTON_COLOR, Color.WHITE);
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addButton.addActionListener(_ -> {
            this.reload(mainPanel);
        });

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.add(addButton, BorderLayout.EAST);

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void reload(JPanel listPanel) {
        listPanel.removeAll();

        for (Reservation res : reservations.getAll()) {
            if (!this.stays.contains(res))
                this.stays.add(res);
        }

        for (Stay stay : stays.getAll()) {
            if (!reservations.contains(stay.getReservation())) {
                this.stays.remove(stay);
                continue;
            }
            LsStayPanel lsStayPanel = new LsStayPanel(stay);
            lsStayPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            listPanel.add(lsStayPanel, BorderLayout.NORTH);
        }
        listPanel.revalidate();
        listPanel.repaint();
    }
}