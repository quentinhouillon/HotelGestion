package core.frontend;

import javax.swing.*;
import javax.swing.border.Border;

import core.backend.*;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

class ReservationDialog extends JDialog {
    public ReservationDialog() {
        super((Frame) null, "Ajouter une reservation", true);
        this.setResizable(false);

        this.setSize(550, 400);
        this.setLayout(new BorderLayout());
        this.add(new CalendarPanel(), BorderLayout.CENTER);

        // FOOTER
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(_ -> dispose());

        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.add(closeButton, BorderLayout.EAST);

        this.add(footerPanel, BorderLayout.SOUTH);
        this.setVisible(true);
    }
}

class CalendarPanel extends JPanel {
    private JPanel mainPanel;
    private JLabel monthLabel;
    private YearMonth currentMonth;
    private LocalDate firstSelectedDate = null; // First selected date
    private LocalDate secondSelectedDate = null; // Second selected date
    private List<JButton> dateButtons; // Store all date buttons for easy access

    public CalendarPanel() {
        currentMonth = YearMonth.now();
        dateButtons = new ArrayList<>();

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);

        prevButton.addActionListener(_ -> changeMonth(-1));
        nextButton.addActionListener(_ -> changeMonth(1));

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        mainPanel = new JPanel(new GridLayout(0, 7));
        updateCalendar();

        // Add a validation button
        JButton validateButton = new JButton("Validate Selection");
        validateButton.addActionListener(_ -> validateSelection());

        JPanel footerPanel = new JPanel();
        footerPanel.add(validateButton);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateCalendar() {
        mainPanel.removeAll();
        dateButtons.clear();
        monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            mainPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < dayOfWeek; i++) {
            mainPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            LocalDate date = currentMonth.atDay(day);

            dayButton.addActionListener(_ -> handleDateSelection(dayButton, date));
            dateButtons.add(dayButton);
            mainPanel.add(dayButton);

            // Highlight dates if they are in the selected range
            if (isDateInRange(date)) {
                dayButton.setBackground(Color.CYAN);
            }
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void handleDateSelection(JButton button, LocalDate date) {
        if (firstSelectedDate == null) {
            // First date selection
            firstSelectedDate = date;
            button.setBackground(Color.CYAN);
        } else if (secondSelectedDate == null) {
            // Second date selection
            secondSelectedDate = date;
            highlightDateRange();
        } else {
            // Reset selection if both dates are already selected
            resetSelection();
            firstSelectedDate = date;
            button.setBackground(Color.CYAN);
        }
    }

    private void highlightDateRange() {
        if (firstSelectedDate != null && secondSelectedDate != null) {
            LocalDate start = firstSelectedDate.isBefore(secondSelectedDate) ? firstSelectedDate : secondSelectedDate;
            LocalDate end = firstSelectedDate.isAfter(secondSelectedDate) ? firstSelectedDate : secondSelectedDate;

            for (JButton button : dateButtons) {
                LocalDate buttonDate = currentMonth.atDay(Integer.parseInt(button.getText()));
                if ((buttonDate.isEqual(start) || buttonDate.isAfter(start)) && buttonDate.isBefore(end.plusDays(1))) {
                    button.setBackground(Color.CYAN);
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
            button.setBackground(null);
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
            JOptionPane.showMessageDialog(this, "Selected Period: " + start + " to " + end);
            SwingUtilities.getWindowAncestor(this).dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a valid date range.");
        }
    }
}

class ClientReservationPanel extends JPanel {
    public ClientReservationPanel() {

    }
}

class RoomReservationPanel extends JPanel {
    public RoomReservationPanel() {

    }
}

public class ReservationPanel extends JPanel {
    ReservationManagement reservations = new ReservationManagement();
    Color mainColor;

    public ReservationPanel() {
        this.setLayout(new BorderLayout());
        
        // FOOTER
        JButton addButton = new JButton("+ Ajouter");
        addButton.addActionListener(_ -> new ReservationDialog());
        addButton.setForeground(Color.BLACK);
        
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());
        footerPanel.add(addButton, BorderLayout.EAST);
        
        this.add(footerPanel, BorderLayout.SOUTH);
    }
}
