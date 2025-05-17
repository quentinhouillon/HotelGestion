package src.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.validation.Validator;

import src.controler.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

class CalendarDialog extends JDialog {
    private JPanel mainPanel;
    private JLabel monthLabel;
    private JButton validateButton;
    private YearMonth currentMonth;
    private LocalDate firstSelectedDate = null; // First selected date
    private LocalDate secondSelectedDate = null; // Second selected date
    private List<JButton> dateButtons; // Store all date buttons for easy access
    private LocalDate[] tabDates = new LocalDate[2];

    public CalendarDialog() {
        super((Frame) null, "Ajouter une date", true);
        this.setResizable(false);
        this.setSize(550, 400);

        currentMonth = YearMonth.now();
        dateButtons = new ArrayList<>();
        this.tabDates = tabDates;

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        UIConstants.createStyledButton(prevButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        JButton nextButton = new JButton(">");
        UIConstants.createStyledButton(nextButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        monthLabel = new JLabel("", SwingConstants.CENTER);

        prevButton.addActionListener(_ -> changeMonth(-1));
        nextButton.addActionListener(_ -> changeMonth(1));

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        mainPanel = new JPanel(new GridLayout(0, 7));
        updateCalendar();

        // Add a validation button
        validateButton = new JButton("Valider la séléction");
        UIConstants.createStyledButton(validateButton, UIConstants.BLUE_BUTTON_COLOR.darker(), Color.WHITE);
        UIConstants.applyButtonEffects(validateButton, UIConstants.BLUE_BUTTON_COLOR.darker(),
                UIConstants.BLUE_BUTTON_COLOR.darker(),
                UIConstants.BLUE_BUTTON_COLOR.darker());

        JPanel footerPanel = new JPanel();
        footerPanel.add(validateButton);

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateCalendar() {
        mainPanel.removeAll();
        dateButtons.clear();
        monthLabel.setText(
                currentMonth.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) + " "
                        + currentMonth.getYear());

        String[] daysOfWeek = { "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim" };
        for (String day : daysOfWeek) {
            mainPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        // Java's DayOfWeek: 1=Lundi, ..., 7=Dimanche
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue(); // 1 (Lundi) à 7 (Dimanche)
        int leadingEmptyCells = dayOfWeek - 1; // 0 si lundi, 6 si dimanche

        for (int i = 0; i < leadingEmptyCells; i++) {
            mainPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
            dayButton.setBorder(blackBorder);
            UIConstants.createStyledButton(dayButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
            LocalDate date = currentMonth.atDay(day);

            dayButton.addActionListener(_ -> handleDateSelection(dayButton, date));
            dateButtons.add(dayButton);
            mainPanel.add(dayButton);

            // Highlight dates if they are in the selected range
            if (isDateInRange(date)) {
            dayButton.setBackground(UIConstants.BLUE_BUTTON_COLOR.darker().darker());
            UIConstants.applyButtonEffects(dayButton, UIConstants.BLUE_BUTTON_COLOR.darker().darker(),
                UIConstants.BLUE_BUTTON_COLOR.darker().darker(),
                UIConstants.BLUE_BUTTON_COLOR.darker().darker());
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void handleDateSelection(JButton button, LocalDate date) {
        if (firstSelectedDate == null) {
            // First date selection
            firstSelectedDate = date;
            button.setBackground(UIConstants.BLUE_BUTTON_COLOR.darker().darker());
        } else if (secondSelectedDate == null) {
            // Second date selection
            secondSelectedDate = date;
            UIConstants.createStyledButton(button, UIConstants.BLUE_BUTTON_COLOR, getForeground());
            UIConstants.applyButtonEffects(validateButton, UIConstants.BLUE_BUTTON_COLOR,
                    UIConstants.BLUE_BUTTON_COLOR.darker(),
                    UIConstants.BLUE_BUTTON_COLOR.darker().darker());
            validateButton.addActionListener(_ -> validateSelection());
            highlightDateRange();
        } else {
            // Reset selection if both dates are already selected
            UIConstants.createStyledButton(button, UIConstants.BLUE_BUTTON_COLOR.darker(), getForeground());
            UIConstants.applyButtonEffects(validateButton, UIConstants.BLUE_BUTTON_COLOR.darker(),
                    UIConstants.BLUE_BUTTON_COLOR.darker(),
                    UIConstants.BLUE_BUTTON_COLOR.darker());
            validateButton.removeActionListener(validateButton.getActionListeners()[0]);
            resetSelection();
            firstSelectedDate = date;
            button.setBackground(UIConstants.BLUE_BUTTON_COLOR.darker().darker());
        }
    }

    private void highlightDateRange() {
        if (firstSelectedDate != null && secondSelectedDate != null) {
            LocalDate start = firstSelectedDate.isBefore(secondSelectedDate) ? firstSelectedDate : secondSelectedDate;
            LocalDate end = firstSelectedDate.isAfter(secondSelectedDate) ? firstSelectedDate : secondSelectedDate;

            for (JButton button : dateButtons) {
                LocalDate buttonDate = currentMonth.atDay(Integer.parseInt(button.getText()));
                if ((buttonDate.isEqual(start) || buttonDate.isAfter(start)) && buttonDate.isBefore(end.plusDays(1))) {
                    button.setBackground(UIConstants.BLUE_BUTTON_COLOR.darker().darker());
                    UIConstants.applyButtonEffects(button, UIConstants.BLUE_BUTTON_COLOR.darker().darker(),
                            UIConstants.BLUE_BUTTON_COLOR.darker().darker(),
                            UIConstants.BLUE_BUTTON_COLOR.darker().darker());
                }
            }
        }
    }

    private boolean isDateInRange(LocalDate date) {
        if (firstSelectedDate != null && secondSelectedDate != null) {
            LocalDate start = firstSelectedDate.isBefore(secondSelectedDate) ? firstSelectedDate : secondSelectedDate;
            LocalDate end = firstSelectedDate.isAfter(secondSelectedDate) ? firstSelectedDate : secondSelectedDate;
            return (date.isEqual(start) || date.isAfter(start)) && date.isBefore(end.plusDays(1));
        }
        return false;
    }

    private void resetSelection() {
        firstSelectedDate = null;
        secondSelectedDate = null;
        for (JButton button : dateButtons) {
            UIConstants.createStyledButton(button, UIConstants.BLUE_BUTTON_COLOR, getForeground());
        }
    }

    private void changeMonth(int offset) {
        currentMonth = currentMonth.plusMonths(offset);
        updateCalendar();
    }

    private void validateSelection() {
        if (firstSelectedDate != null && secondSelectedDate != null) {
            LocalDate start = firstSelectedDate.isBefore(secondSelectedDate) ? firstSelectedDate : secondSelectedDate;
            LocalDate end = firstSelectedDate.isAfter(secondSelectedDate) ? firstSelectedDate : secondSelectedDate;
            this.tabDates[0] = start;
            this.tabDates[1] = end;
            dispose();
            new ReservationDialog(tabDates);
        }
    }
}

class ReservationDialog extends JDialog {
    ClientManagement clients = new ClientManagement();
    RoomManagemment rooms = new RoomManagemment();
    ReservationManagement reservations = new ReservationManagement();
    StayManagement stays = new StayManagement();
    LocalDate[] tabDates;
    List<String> clientsName = new ArrayList<>();
    List<String> roomsNumber = new ArrayList<>();

    public ReservationDialog(LocalDate[] tabDates) {
        super((Frame) null, "Ajouter une réservation", true);
        this.tabDates = tabDates;

        this.setResizable(false);
        this.setSize(450, 250);
        this.setLayout(new BorderLayout());
        fillArrays();

        JLabel titleLabel = new JLabel("Réservation du " + this.tabDates[0] + " au " + this.tabDates[1]);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // MAIN
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(0, 2, 0, 0)); // 2 elements per row with spacing
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel clientLabel = new JLabel("Client :");
        JLabel roomLabel = new JLabel("Chambre :");

        JComboBox<String> boxClient = new JComboBox<>(clientsName.toArray(new String[0]));
        JComboBox<String> boxRoom = new JComboBox<>(roomsNumber.toArray(new String[0]));

        mainPanel.add(clientLabel);
        mainPanel.add(boxClient);
        mainPanel.add(roomLabel);
        mainPanel.add(boxRoom);

        // FOOTER
        JButton validButton = new JButton("Ajouter");
        UIConstants.createStyledButton(validButton, UIConstants.GREEN_BUTTON_COLOR, Color.WHITE);
        validButton.addActionListener(_ -> {
            String selectedClientName = (String) boxClient.getSelectedItem();
            String selectedRoomNumber = (String) boxRoom.getSelectedItem();
            int roomNumber = Integer.parseInt(selectedRoomNumber.replaceAll("[^0-9]", ""));

            Client selectedClient = clients.getByName(selectedClientName);
            Room selectedRoom = rooms.getRoomByNumRoom(roomNumber);

            Reservation newReservation = reservations.add(selectedClient, selectedRoom, tabDates[0], tabDates[1]);
            stays.add(newReservation);
            dispose();
        });

        JButton closeButton = new JButton("Fermer");
        UIConstants.createStyledButton(closeButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        closeButton.addActionListener(_ -> dispose());

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 5));
        footerPanel.add(closeButton);
        footerPanel.add(validButton);

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(footerPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void fillArrays() {
        for (Client client : clients.getAll()) {
            clientsName.add(client.getName() + " " + client.getLastName());
        }

        for (Room room : rooms.getRooms()) {
            if (!reservations.isRoomReserved(room.getID(), this.tabDates[0], this.tabDates[1])) {
                roomsNumber.add("Chambre n°" + room.getRoomNumber());
            }
        }
    }
}

class LsPanel extends JPanel {
    private Reservation reservation;
    private ReservationManagement reservations = new ReservationManagement();
    private StayManagement stays = new StayManagement();

    public LsPanel(Reservation reservation, ActionListener deleteAction) {
        this.reservation = reservation;
        String clientName = this.reservation.getClient().getName();
        String clientLastName = this.reservation.getClient().getLastName();
        int roomNumber = this.reservation.getRoom().getRoomNumber();
        LocalDate start = this.reservation.getDuration()[0];
        LocalDate end = this.reservation.getDuration()[1];

        setLayout(new BorderLayout());
        setBackground(UIConstants.ACCENT_COLOR);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(43, 43, 43), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Panel pour les informations de la resercation
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 1));
        infoPanel.setBackground(null);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel nameLabel = new JLabel("Client : " + clientName + " " + clientLastName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        infoPanel.add(nameLabel);

        JLabel roomLabel = new JLabel("Chambre : " + roomNumber);
        roomLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roomLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        infoPanel.add(roomLabel);

        JLabel durationLabel = new JLabel("Séjour : du " + start + " au " + end);
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        durationLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        infoPanel.add(durationLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 10));
        buttonPanel.setBackground(null);

        JButton deleteButton = new JButton("Annuler");
        UIConstants.createStyledButton(deleteButton, UIConstants.RED_BUTTON_COLOR, Color.white);
        deleteButton.addActionListener(deleteAction);

        JButton stayButton = new JButton("Voir le séjour");

        if (stays.stayStarted(this.reservation)) {
            deleteButton.setText("Supprimer");
            UIConstants.createStyledButton(stayButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
            stayButton.addActionListener(_ -> {
                new StayDialog(stays.getStayByResercation(this.reservation));
            });
        } else {
            UIConstants.createStyledButton(stayButton, UIConstants.BLUE_BUTTON_COLOR.darker(), Color.WHITE);
            UIConstants.applyButtonEffects(stayButton, stayButton.getBackground(), stayButton.getBackground(),
                    stayButton.getBackground());
        }

        buttonPanel.add(stayButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.EAST);
        add(infoPanel, BorderLayout.WEST);
    }
}

public class ReservationPanel extends JPanel {
    ReservationManagement reservations = new ReservationManagement();
    StayManagement stays = new StayManagement();

    public ReservationPanel() {
        this.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Liste des réservations");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // MAIN
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(UIConstants.MAIN_COLOR);
        scrollPane.setBorder(null);

        // FOOTER
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(null);

        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 40));
        searchField.setMinimumSize(new Dimension(200, 40));
        searchField.setMaximumSize(new Dimension(200, 40));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchField.setBackground(UIConstants.ACCENT_COLOR);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIConstants.ACCENT_COLOR),
                BorderFactory.createEmptyBorder(0, 10, 0, 0)));

        JButton searchButton = new JButton("Rechercher");
        UIConstants.createStyledButton(searchButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        searchButton.setPreferredSize(new Dimension(100, 40));
        searchButton.addActionListener(_ -> {
            String query = searchField.getText().trim();
            try {
                // Try to parse the query as a date
                LocalDate searchDate = LocalDate.parse(query);
                reloadLsPanel(mainPanel, reservations.search(searchDate));
            } catch (Exception e1) {
                try {
                    // Try to parse the query as an integer (room number)
                    int roomNumber = Integer.parseInt(query);
                    reloadLsPanel(mainPanel, reservations.search(roomNumber));

                } catch (Exception e2) {
                    // Fallback to string search (client name)
                    reloadLsPanel(mainPanel, reservations.search(query));
                }
            }

        });

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    searchButton.doClick();
                }
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JButton addButton = new JButton("+ Ajouter");
        UIConstants.createStyledButton(addButton, UIConstants.PURPLE_BUTTON_COLOR, Color.WHITE);
        addButton.setPreferredSize(new Dimension(80, 40));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        addButton.addActionListener(_ -> {
            new CalendarDialog();
            reloadLsPanel(mainPanel, reservations.getAll());
        });

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        footerPanel.add(addButton, BorderLayout.EAST);
        footerPanel.add(searchPanel, BorderLayout.WEST);

        this.add(titleLabel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(footerPanel, BorderLayout.SOUTH);
        reloadLsPanel(mainPanel, reservations.getAll());
    }

    private void reloadLsPanel(JPanel listPanel, Reservation[] reservationTab) {
        listPanel.removeAll();
        if (reservationTab.length == 0) {
            JLabel emptyLabel = new JLabel("Aucune réservation n'a été ajouée !");
            emptyLabel.setForeground(Color.LIGHT_GRAY);
            listPanel.add(emptyLabel);
        } else {
            for (Reservation reservation : reservationTab) {
                LsPanel lsPanel = new LsPanel(reservation, _ -> {
                    JDialog confirmDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Confirmation",
                            Dialog.ModalityType.APPLICATION_MODAL);
                    confirmDialog.setSize(550, 150);
                    confirmDialog.setResizable(false);
                    confirmDialog.setLayout(new BorderLayout());
                    confirmDialog.setLocationRelativeTo(this);
    
                    JLabel confirmLabel = new JLabel(
                            "Êtes-vous sûr de vouloir annuler la reservation ? Ceci supprimera le séjour associé.");
                    confirmLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    confirmDialog.add(confirmLabel, BorderLayout.CENTER);
    
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    
                    JButton yesButton = new JButton("Oui");
                    UIConstants.createStyledButton(yesButton, UIConstants.GREEN_BUTTON_COLOR, Color.WHITE);
                    yesButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    
                    yesButton.addActionListener(_ -> {
                        stays.remove(stays.getStayByResercation(reservation));
                        reservations.remove(reservation);
                        reloadLsPanel(listPanel, reservations.getAll());
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
                });
    
                lsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                listPanel.add(lsPanel, BorderLayout.NORTH);
            }

        }
        listPanel.revalidate();
        listPanel.repaint();
    }
}