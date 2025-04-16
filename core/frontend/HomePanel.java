package core.frontend;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class HomePanel extends JPanel {
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
        JLabel titleLabel = new JLabel("Welcome to the Hotel Management System");
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
        titleLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        titleLabel.setBackground(Color.WHITE);

        // Wrap the title label in a panel to prevent it from stretching
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);

        // Bottom panel with resizable left and right panels
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Add the calendar to the left panel
        Map<LocalDate, Integer> roomOccupancy = new HashMap<>();
        roomOccupancy.put(LocalDate.of(2025, 4, 1), 5);  // Low occupancy
        roomOccupancy.put(LocalDate.of(2025, 4, 5), 15); // Medium occupancy
        roomOccupancy.put(LocalDate.of(2025, 4, 10), 25); // High occupancy
        roomOccupancy.put(LocalDate.of(2025, 4, 15), 8);  // Low occupancy
        roomOccupancy.put(LocalDate.of(2025, 4, 20), 12); // Medium occupancy
        roomOccupancy.put(LocalDate.of(2025, 4, 25), 30); // High occupancy

        JPanel calendarPanel = createStaticCalendarPanel(roomOccupancy);
        leftPanel.add(calendarPanel, BorderLayout.CENTER);

        JPanel rightPanel = createLegendPanel() ;
        rightPanel.setBackground(Color.DARK_GRAY);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.5); // Ensures both panels resize equally
        bottomPanel.add(splitPane, BorderLayout.CENTER);

        // Add panels to the main panel
        add(imagePanel, BorderLayout.NORTH);
        add(titlePanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private static JPanel createStaticCalendarPanel(Map<LocalDate, Integer> roomOccupancy) {
        // Create the main panel for the calendar
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new BorderLayout());
        calendarPanel.setPreferredSize(new Dimension(400, 300)); // Set a fixed size for the calendar

        // Get the current month and year
        LocalDate currentDate = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonthValue());

        // Create a header for the calendar
        JLabel headerLabel = new JLabel(currentDate.getMonth() + " " + currentDate.getYear(), JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Smaller font for compactness
        calendarPanel.add(headerLabel, BorderLayout.NORTH);

        // Create a panel for the days of the week
        JPanel daysOfWeekPanel = new JPanel(new GridLayout(1, 7, 2, 2)); // Add small gaps
        String[] daysOfWeek = {"S", "M", "T", "W", "T", "F", "S"}; // Shortened day names
        for (String day : daysOfWeek) {
            JLabel dayLabel = new JLabel(day, JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Smaller font
            daysOfWeekPanel.add(dayLabel);
        }
        calendarPanel.add(daysOfWeekPanel, BorderLayout.CENTER);

        // Create a panel for the days of the month
        JPanel daysPanel = new JPanel(new GridLayout(6, 7, 2, 2)); // Add small gaps
        int firstDayOfMonth = yearMonth.atDay(1).getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int daysInMonth = yearMonth.lengthOfMonth();

        // Adjust for Sunday as the first day of the week
        firstDayOfMonth = (firstDayOfMonth % 7) + 1;

        // Fill in blank days before the first day of the month
        for (int i = 1; i < firstDayOfMonth; i++) {
            daysPanel.add(new JLabel("")); // Empty label for blank days
        }

        // Fill in the days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = yearMonth.atDay(day);
            JLabel dayLabel = new JLabel(String.valueOf(day), JLabel.CENTER);
            dayLabel.setFont(new Font("Arial", Font.PLAIN, 14)); // Smaller font

            // Highlight the date based on room occupancy
            if (roomOccupancy.containsKey(date)) {
                int roomsOccupied = roomOccupancy.get(date);
                if (roomsOccupied > 20) {
                    dayLabel.setOpaque(true);
                    dayLabel.setBackground(Color.RED); // High occupancy
                } else if (roomsOccupied > 10) {
                    dayLabel.setOpaque(true);
                    dayLabel.setBackground(Color.ORANGE); // Medium occupancy
                } else {
                    dayLabel.setOpaque(true);
                    dayLabel.setBackground(Color.GREEN); // Low occupancy
                }
            }

            daysPanel.add(dayLabel);
        }

        // Fill in blank days after the last day of the month
        int totalCells = 42; // 6 rows * 7 columns
        int filledCells = firstDayOfMonth - 1 + daysInMonth;
        for (int i = filledCells; i < totalCells; i++) {
            daysPanel.add(new JLabel("")); // Empty label for blank days
        }
        

        calendarPanel.add(daysPanel, BorderLayout.SOUTH);

        // Wrap the calendar in a centered panel
        JPanel wrapperPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout to center the calendar
        wrapperPanel.add(calendarPanel);

        return wrapperPanel;
    }
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new GridLayout(3, 1, 5, 5)); // 3 rows, 1 column, with gaps

        // Low occupancy legend
        JPanel lowOccupancyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lowColorLabel = new JLabel();
        lowColorLabel.setOpaque(true);
        lowColorLabel.setBackground(Color.GREEN);
        lowColorLabel.setPreferredSize(new Dimension(20, 20));
        JLabel lowTextLabel = new JLabel("Low Occupancy");
        lowOccupancyPanel.add(lowColorLabel);
        lowOccupancyPanel.add(lowTextLabel);

        // Medium occupancy legend
        JPanel mediumOccupancyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel mediumColorLabel = new JLabel();
        mediumColorLabel.setOpaque(true);
        mediumColorLabel.setBackground(Color.ORANGE);
        mediumColorLabel.setPreferredSize(new Dimension(20, 20));
        JLabel mediumTextLabel = new JLabel("Medium Occupancy");
        mediumOccupancyPanel.add(mediumColorLabel);
        mediumOccupancyPanel.add(mediumTextLabel);

        // High occupancy legend
        JPanel highOccupancyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel highColorLabel = new JLabel();
        highColorLabel.setOpaque(true);
        highColorLabel.setBackground(Color.RED);
        highColorLabel.setPreferredSize(new Dimension(20, 20));
        JLabel highTextLabel = new JLabel("High Occupancy");
        highOccupancyPanel.add(highColorLabel);
        highOccupancyPanel.add(highTextLabel);

        // Add all legend panels to the main legend panel
        legendPanel.add(lowOccupancyPanel);
        legendPanel.add(mediumOccupancyPanel);
        legendPanel.add(highOccupancyPanel);

        return legendPanel;
    }
}