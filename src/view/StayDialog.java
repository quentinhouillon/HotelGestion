package src.view;

import javax.swing.*;

import src.controler.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class StayDialog extends JDialog {
    private JPanel mainPanel;
    private Stay stay;
    private StayManagement stays = new StayManagement();

    public StayDialog(Stay stay) {
        super((Frame) null, "Formulaire séjour", false);
        this.stay = stay;

        setSize(700, 550);
        setResizable(false);

        JLabel title = new JLabel("Séjour de Mr/Mme " + this.stay.getClient().getLastName());
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JLabel consomation = new JLabel("Consommation");
        consomation.setFont(new Font("Arial", Font.PLAIN, 17));
        consomation.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(consomation);

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton addButton = new JButton("Commander");
        UIConstants.createStyledButton(addButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        addButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        addButton.setPreferredSize(new Dimension(100, 40));
        addButton.addActionListener(_ -> {
            this.addConsommation();
        });

        JButton pdfButton = new JButton("PDF");
        pdfButton.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        pdfButton.setPreferredSize(new Dimension(100, 40));
        UIConstants.createStyledButton(pdfButton, UIConstants.ORANGE_BUTTON_COLOR, Color.WHITE);
        pdfButton.addActionListener(_ -> {
            this.generatePDF();
        });

        footerPanel.add(addButton);
        footerPanel.add(pdfButton);
        add(mainPanel, BorderLayout.WEST);
        add(footerPanel, BorderLayout.SOUTH);

        reloadConso();
        setVisible(true);
    }

    private void addConsommation() {
        List<String> allConsommations = new ArrayList<>();
        for (int i = 0; i < stay.getAllBoissons().length; i++) {
            allConsommations.add(stay.getAllBoissons()[i] + " - " + stay.getAllPrices()[i] + " €");
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

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
        buttonPanel.add(confirmButton);

        JButton cancelButton = new JButton("Annuler");
        UIConstants.createStyledButton(cancelButton, UIConstants.RED_BUTTON_COLOR, Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        cancelButton.addActionListener(_ -> dialog.dispose());
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
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
                int index = i;

                JPanel consoPanel = new JPanel();
                consoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
                consoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                consoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                JButton deleteButton = new JButton("X");
                UIConstants.createStyledButton(deleteButton, UIConstants.RED_BUTTON_COLOR, Color.WHITE);
                deleteButton.setPreferredSize(new Dimension(40, 20));
                deleteButton.addActionListener(_ -> {
                    stay.removeConso(index);
                    reloadConso();
                });

                JLabel consoLabel = new JLabel(stay.getConso()[i] + " - " + stay.getPrice()[i] + " €");
                consoLabel.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));

                consoPanel.add(deleteButton);
                consoPanel.add(consoLabel);

                if (i > 0)
                    mainPanel.add(Box.createRigidArea(new Dimension(0, 1)));

                mainPanel.add(consoPanel);
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void generatePDF() {
        String filePath = "./factures/Facture_" + stay.getClient().getLastName() + ".pdf";
        stays.generatePDF(stay, filePath);
    }
}