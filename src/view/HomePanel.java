package src.view;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

import src.controler.*;

public class HomePanel extends JPanel {
    private final ReservationManagement reservationManagement = new ReservationManagement();
    private final RoomManagemment rooms = new RoomManagemment();
    private final JPanel leftPanel;
    private YearMonth currentYearMonth;

    public HomePanel() {
        setLayout(new BorderLayout());
        currentYearMonth = YearMonth.now();

        // Top image panel
        JPanel imagePanel = createImagePanel();
        JPanel titlePanel = createTitlePanel();

        // Bottom split panel
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY);
        leftPanel.add(new CalendarPanel(currentYearMonth), BorderLayout.CENTER);

        JPanel rightPanel = createLegendPanel();
        rightPanel.setBackground(Color.DARK_GRAY);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setPreferredSize(new Dimension(splitPane.getWidth(), 300));
        splitPane.setMinimumSize(new Dimension(splitPane.getWidth(), 300));
        splitPane.setMaximumSize(new Dimension(splitPane.getWidth(), 300));
        splitPane.setResizeWeight(0.5);

        add(imagePanel, BorderLayout.NORTH);
        add(titlePanel, BorderLayout.CENTER);
        add(splitPane, BorderLayout.SOUTH);
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        ImageIcon originalIcon = new ImageIcon("img/HomePageImg.jpg");
        Image originalImage = originalIcon.getImage();
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 0, 20));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight() / 3;
                imageLabel.setIcon(new ImageIcon(originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            }
        });
        return imagePanel;
    }

    private JPanel createTitlePanel() {
        JLabel titleLabel = new JLabel("Bienvenue à l'Hôtel Continental", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 50, 0));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        return titlePanel;
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        legendPanel.add(createLegendRow(Color.GREEN, "Peu de réservation"));
        legendPanel.add(createLegendRow(Color.ORANGE, "Beaucoup de réservation"));
        legendPanel.add(createLegendRow(Color.RED, "Complet"));

        JButton refreshButton = new JButton("Actualiser");
        UIConstants.createStyledButton(refreshButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> refreshCalendar(YearMonth.now()));
        legendPanel.add(refreshButton);

        return legendPanel;
    }

    private JPanel createLegendRow(Color color, String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel colorLabel = new JLabel();
        colorLabel.setOpaque(true);
        colorLabel.setBackground(color);
        colorLabel.setPreferredSize(new Dimension(20, 20));
        panel.add(colorLabel);
        panel.add(new JLabel(text));
        return panel;
    }

    public void refreshCalendar() {
        refreshCalendar(currentYearMonth);
    }

    public void refreshCalendar(YearMonth yearMonth) {
        currentYearMonth = yearMonth;
        leftPanel.removeAll();
        leftPanel.add(new CalendarPanel(currentYearMonth), BorderLayout.CENTER);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    // Inner class for the calendar panel
    private class CalendarPanel extends JPanel {
        CalendarPanel(YearMonth yearMonth) {
            setLayout(new BorderLayout());
            add(createHeader(yearMonth), BorderLayout.NORTH);
            add(createCalendarGrid(yearMonth), BorderLayout.CENTER);
        }

        private JPanel createHeader(YearMonth yearMonth) {
            JPanel headerPanel = new JPanel(new BorderLayout());
            JButton prevButton = new JButton("<");
            UIConstants.createStyledButton(prevButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
            JButton nextButton = new JButton(">");
            UIConstants.createStyledButton(nextButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
            JLabel headerLabel = new JLabel(
                    yearMonth.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) + " "
                            + yearMonth.getYear(),
                    JLabel.CENTER);
            headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
            prevButton.addActionListener(e -> refreshCalendar(yearMonth.minusMonths(1)));
            nextButton.addActionListener(e -> refreshCalendar(yearMonth.plusMonths(1)));
            headerPanel.add(prevButton, BorderLayout.WEST);
            headerPanel.add(headerLabel, BorderLayout.CENTER);
            headerPanel.add(nextButton, BorderLayout.EAST);
            return headerPanel;
        }

        private JPanel createCalendarGrid(YearMonth yearMonth) {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JPanel legendPanel = new JPanel(new GridLayout(1, 7));
            String[] daysOfWeek = { "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim" };
            for (String day : daysOfWeek) {
                JLabel dayLabel = new JLabel(day, JLabel.CENTER);
                dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
                legendPanel.add(dayLabel);
            }
            panel.add(legendPanel);

            JPanel daysPanel = new JPanel(new GridLayout(6, 7));
            Map<LocalDate, Integer> occupancy = calculateRoomOccupancy(yearMonth);

            int firstDayOfMonth = yearMonth.atDay(1).getDayOfWeek().getValue();
            // En Java, Lundi = 1, Dimanche = 7. On veut que lundi soit la première colonne.
            int leadingEmptyCells = firstDayOfMonth - 1;
            if (leadingEmptyCells < 0) leadingEmptyCells = 6; // Sécurité, mais ne devrait pas arriver

            int daysInMonth = yearMonth.lengthOfMonth();

            for (int i = 0; i < leadingEmptyCells; i++)
                daysPanel.add(new JLabel(""));

            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = yearMonth.atDay(day);
                JLabel dayLabel = new JLabel(String.valueOf(day), JLabel.CENTER);
                int roomsOccupied = occupancy.getOrDefault(date, 0);
                int full = rooms.getRooms().length;
                if (roomsOccupied == full) {
                    dayLabel.setForeground(Color.RED);
                } else if (roomsOccupied >= full / 2) {
                    dayLabel.setForeground(Color.ORANGE);
                } else {
                    dayLabel.setForeground(Color.GREEN);
                }
                JPanel dayWrapper = new JPanel(new BorderLayout());
                dayWrapper.add(dayLabel, BorderLayout.CENTER);
                daysPanel.add(dayWrapper);
            }
            int totalCells = 42;
            int filledCells = leadingEmptyCells + daysInMonth;
            for (int i = filledCells; i < totalCells; i++)
                daysPanel.add(new JLabel(""));
            panel.add(daysPanel);
            return panel;
        }
    }

    private Map<LocalDate, Integer> calculateRoomOccupancy(YearMonth yearMonth) {
        Map<LocalDate, Integer> roomOccupancy = new HashMap<>();
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            roomOccupancy.put(yearMonth.atDay(day), 0);
        }
        Reservation[] reservations = reservationManagement.getAll();
        for (Reservation reservation : reservations) {
            LocalDate[] duration = reservation.getDuration();
            for (LocalDate date = duration[0]; !date.isAfter(duration[1]); date = date.plusDays(1)) {
                if (roomOccupancy.containsKey(date)) {
                    roomOccupancy.put(date, roomOccupancy.get(date) + 1);
                }
            }
        }
        return roomOccupancy;
    }
}
