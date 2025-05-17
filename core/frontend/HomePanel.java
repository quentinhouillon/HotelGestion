package core.frontend;

import core.backend.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class HomePanel extends JPanel {
    private ReservationManagement reservationManagement = new ReservationManagement();
    private RoomManagemment rooms = new RoomManagemment();
    private JPanel leftPanel; // Store a reference to the left panel

    public HomePanel() {
        setLayout(new BorderLayout());

        // Top panel with a dynamically resizing image
        JPanel imagePanel = new JPanel(new BorderLayout());
        ImageIcon originalIcon = new ImageIcon("img/HomePageImg.jpg");
        Image originalImage = originalIcon.getImage();
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(60, 20, 0, 20)); // Add top margin

        // Resize image dynamically
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight() / 3; // Adjust height proportionally
                Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            }
        });

        // Title label
        JLabel titleLabel = new JLabel("Bienvenue à l'Hôtel Continental", JLabel.CENTER);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Increase font size for a bigger title
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 50, 0)); // Add top and bottom margins
        titleLabel.setBackground(Color.WHITE);

        // Wrap the title label in a panel to prevent it from stretching
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding around the title
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Bottom panel with resizable left and right panels
        JPanel bottomPanel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new BorderLayout()); // Initialize and store the left panel
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Add the dynamic calendar to the left panel
        JPanel calendarPanel = createDynamicCalendarPanel();
        leftPanel.add(calendarPanel, BorderLayout.CENTER);

        JPanel rightPanel = createLegendPanel();
        rightPanel.setBackground(Color.DARK_GRAY);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.5); // Ensures both panels resize equally
        bottomPanel.add(splitPane, BorderLayout.CENTER);

        // Add panels to the main panel
        add(imagePanel, BorderLayout.NORTH);
        add(titlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createDynamicCalendarPanel() {
        // Calculate room occupancy dynamically
        Map<LocalDate, Integer> roomOccupancy = calculateRoomOccupancy();

        // Create the calendar panel using the dynamic data
        return createStaticCalendarPanel(roomOccupancy);
    }


    private Map<LocalDate, Integer> calculateRoomOccupancy() {
        Map<LocalDate, Integer> roomOccupancy = new HashMap<>();

        // Get the current month and year
        LocalDate currentDate = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonthValue());

        // Initialize the map with all dates in the current month set to 0
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            roomOccupancy.put(date, 0);
        }

        // Fetch reservations and process them
        List<Reservation> reservations = List.of(reservationManagement.getAll());
        for (Reservation reservation : reservations) {
            LocalDate[] duration = reservation.getDuration();
            LocalDate startDate = duration[0];
            LocalDate endDate = duration[1];

            // Increment room occupancy for each day in the reservation period
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (roomOccupancy.containsKey(date)) {
                    roomOccupancy.put(date, roomOccupancy.get(date) + 1);
                }
            }
        }
        return roomOccupancy;
    }

    private Map<LocalDate, Integer> calculateRoomOccupancy(YearMonth yearMonth) {
        Map<LocalDate, Integer> roomOccupancy = new HashMap<>();

        // Initialize the map with all dates in the specified month set to 0
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            roomOccupancy.put(date, 0);
        }

        // Fetch reservations and process them
        List<Reservation> reservations = List.of(reservationManagement.getAll());
        for (Reservation reservation : reservations) {
            LocalDate[] duration = reservation.getDuration();
            LocalDate startDate = duration[0];
            LocalDate endDate = duration[1];

            // Increment room occupancy for each day in the reservation period
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (roomOccupancy.containsKey(date)) {
                    roomOccupancy.put(date, roomOccupancy.get(date) + 1);
                }
            }
        }
        return roomOccupancy;
    }

    private JPanel createStaticCalendarPanel(Map<LocalDate, Integer> roomOccupancy) {
        // Create the main panel for the calendar
        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setLayout(new BorderLayout());
        calendarPanel.setPreferredSize(new Dimension(400, 300)); // Set a fixed size for the calendar

        // Get the current month and year
        LocalDate currentDate = LocalDate.now();
        YearMonth[] currentYearMonth = {YearMonth.of(currentDate.getYear(), currentDate.getMonthValue())};

        // Create a header panel with navigation buttons and the month label
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        UIConstants.createStyledButton(prevButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        JButton nextButton = new JButton(">");
        UIConstants.createStyledButton(nextButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        JLabel headerLabel = new JLabel("", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Font for the month label

        // Update the header label with the current month and year
        Runnable updateHeader = () -> headerLabel.setText(currentYearMonth[0].getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) + " " + currentYearMonth[0].getYear());
        updateHeader.run();

        // Add action listeners to the navigation buttons
        prevButton.addActionListener(e -> {
            YearMonth newMonth = currentYearMonth[0].minusMonths(1);
            refreshCalendar(newMonth); // Refresh the calendar with the previous month
        });

        nextButton.addActionListener(e -> {
            YearMonth newMonth = currentYearMonth[0].plusMonths(1);
            refreshCalendar(newMonth); // Refresh the calendar with the next month
        });

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        // Create a legend panel for day labels
        JPanel legendPanel = new JPanel(new GridLayout(1, 7, 0, 0)); // 1 row, 7 columns, no gaps
        String[] daysOfWeek = {"Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Smaller font
            legendPanel.add(dayLabel);
        }

        // Create the calendar grid
        JPanel daysPanel = new JPanel(new GridLayout(6, 7, 0, 0)); // 6 rows, 7 columns, no gaps
        daysPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove any padding

        // Combine the legend and days panels into a single panel
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.add(legendPanel);
        combinedPanel.add(daysPanel);

        // Add the header and combined panels to the calendar panel
        calendarPanel.add(headerPanel, BorderLayout.NORTH);
        calendarPanel.add(combinedPanel, BorderLayout.CENTER);

        // Populate the calendar for the current month
        updateCalendar(daysPanel, currentYearMonth[0], roomOccupancy);

        // Wrap the calendar in a centered panel
        JPanel wrapperPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout to center the calendar
        wrapperPanel.add(calendarPanel);

        return wrapperPanel;
    }


    private void updateCalendar(JPanel daysPanel, YearMonth yearMonth, Map<LocalDate, Integer> roomOccupancy) {
        daysPanel.removeAll(); // Clear the previous calendar
    
        // Calculate the first day of the month and the number of days in the month
        int firstDayOfMonth = yearMonth.atDay(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int daysInMonth = yearMonth.lengthOfMonth();
    
        // Adjust for Sunday as the last day of the week
        firstDayOfMonth = (firstDayOfMonth == 7) ? 0 : firstDayOfMonth;
    
        // Fill in blank days before the first day of the month
        for (int i = 0; i < firstDayOfMonth; i++) {
            daysPanel.add(new JLabel("")); // Empty label for blank days
        }
    
        // Fill in the days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            JLabel dayLabel = new JLabel(String.valueOf(day), JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Regular font
    
            // Highlight the date based on room occupancy
            if (roomOccupancy.containsKey(date)) {
                int roomsOccupied = roomOccupancy.get(date);
                int full = rooms.getRooms().length;
                if (roomsOccupied == full) {
                    dayLabel.setForeground(Color.RED); // High occupancy
                } else if (roomsOccupied >= full/2) {
                    dayLabel.setForeground(Color.ORANGE); // Medium occupancy
                } else {
                    dayLabel.setForeground(Color.GREEN); // Low occupancy
                }
            }
    
            // Wrap the label in a panel to avoid layout constraints
            JPanel dayWrapper = new JPanel(new BorderLayout());
            dayWrapper.add(dayLabel, BorderLayout.CENTER);
            daysPanel.add(dayWrapper);
        }
    
        // Fill in blank days after the last day of the month
        int totalCells = 42; // 6 rows * 7 columns
        int filledCells = firstDayOfMonth + daysInMonth;
        for (int i = filledCells; i < totalCells; i++) {
            daysPanel.add(new JLabel("")); // Empty label for blank days
        }
    
        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new GridLayout(4, 1, 5, 5)); // 4 rows, 1 column, with gaps

        // Low occupancy legend
        JPanel lowOccupancyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lowColorLabel = new JLabel();
        lowColorLabel.setOpaque(true);
        lowColorLabel.setBackground(Color.GREEN);
        lowColorLabel.setPreferredSize(new Dimension(20, 20));
        JLabel lowTextLabel = new JLabel("Peu de monde");
        lowOccupancyPanel.add(lowColorLabel);
        lowOccupancyPanel.add(lowTextLabel);

        // Medium occupancy legend
        JPanel mediumOccupancyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel mediumColorLabel = new JLabel();
        mediumColorLabel.setOpaque(true);
        mediumColorLabel.setBackground(Color.ORANGE);
        mediumColorLabel.setPreferredSize(new Dimension(20, 20));
        JLabel mediumTextLabel = new JLabel("beaucoup de monde");
        mediumOccupancyPanel.add(mediumColorLabel);
        mediumOccupancyPanel.add(mediumTextLabel);

        // High occupancy legend
        JPanel highOccupancyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel highColorLabel = new JLabel();
        highColorLabel.setOpaque(true);
        highColorLabel.setBackground(Color.RED);
        highColorLabel.setPreferredSize(new Dimension(20, 20));
        JLabel highTextLabel = new JLabel("Complet");
        highOccupancyPanel.add(highColorLabel);
        highOccupancyPanel.add(highTextLabel);

        // Refresh button
        JButton refreshButton = new JButton("Actualiser");
        UIConstants.createStyledButton(refreshButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        refreshButton.addActionListener(e -> refreshCalendar()); // Call refreshCalendar() when clicked

        // Add all legend panels and the button to the main legend panel
        legendPanel.add(lowOccupancyPanel);
        legendPanel.add(mediumOccupancyPanel);
        legendPanel.add(highOccupancyPanel);
        legendPanel.add(refreshButton);

        return legendPanel;
    }

    public void refreshCalendar() {
        // Remove the old calendar panel
        leftPanel.removeAll();
        // Add the new calendar panel
        JPanel newCalendarPanel = createDynamicCalendarPanel();
        leftPanel.add(newCalendarPanel, BorderLayout.CENTER);
        // Revalidate and repaint the panel to apply changes
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    public void refreshCalendar(YearMonth yearMonth) {
        // Remove the old calendar panel
        leftPanel.removeAll();

        // Create a new calendar panel with the specified month
        JPanel calendarPanel = new JPanel(new BorderLayout());

        // Create a header panel with navigation buttons and the month label
        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        UIConstants.createStyledButton(prevButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        JButton nextButton = new JButton(">");
        UIConstants.createStyledButton(nextButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        JLabel headerLabel = new JLabel("", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Font for the month label

        // Update the header label with the current month and year
        headerLabel.setText(yearMonth.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) + " " + yearMonth.getYear());

        // Add action listeners to the navigation buttons
        prevButton.addActionListener(e -> {
            YearMonth newMonth = yearMonth.minusMonths(1);
            refreshCalendar(newMonth); // Refresh the calendar with the previous month
        });

        nextButton.addActionListener(e -> {
            YearMonth newMonth = yearMonth.plusMonths(1);
            refreshCalendar(newMonth); // Refresh the calendar with the next month
        });

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        // Create a legend panel for day labels
        JPanel legendPanel = new JPanel(new GridLayout(1, 7, 0, 0)); // 1 row, 7 columns, no gaps
        String[] daysOfWeek = {"Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"};
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Smaller font
            legendPanel.add(dayLabel);
        }

        // Create the calendar grid
        JPanel daysPanel = new JPanel(new GridLayout(6, 7, 0, 0)); // 6 rows, 7 columns, no gaps
        daysPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Remove any padding

        // Combine the legend and days panels into a single panel
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.add(legendPanel);
        combinedPanel.add(daysPanel);

        // Add the header and combined panels to the calendar panel
        calendarPanel.add(headerPanel, BorderLayout.NORTH);
        calendarPanel.add(combinedPanel, BorderLayout.CENTER);

        // Populate the calendar for the specified month
        updateCalendar(daysPanel, yearMonth, calculateRoomOccupancy(yearMonth));

        // Add the new calendar panel to the left panel
        leftPanel.add(calendarPanel, BorderLayout.CENTER);

        // Revalidate and repaint the panel to apply changes
        leftPanel.revalidate();
        leftPanel.repaint();
    }
}