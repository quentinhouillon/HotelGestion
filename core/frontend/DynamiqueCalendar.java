package core.frontend;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DynamiqueCalendar {
    private JFrame frame;
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private YearMonth currentMonth;
    private LocalDate firstSelectedDate = null; // First selected date
    private LocalDate secondSelectedDate = null; // Second selected date
    private List<JButton> dateButtons; // Store all date buttons for easy access

    public DynamiqueCalendar() {
        frame = new JFrame("Dynamic Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450); // Adjusted size to fit the validation button
        frame.setLayout(new BorderLayout());

        currentMonth = YearMonth.now();
        dateButtons = new ArrayList<>();

        JPanel headerPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("<");
        JButton nextButton = new JButton(">");
        monthLabel = new JLabel("", SwingConstants.CENTER);

        prevButton.addActionListener(e -> changeMonth(-1));
        nextButton.addActionListener(e -> changeMonth(1));

        headerPanel.add(prevButton, BorderLayout.WEST);
        headerPanel.add(monthLabel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        updateCalendar();

        // Add a validation button
        JButton validateButton = new JButton("Validate Selection");
        validateButton.addActionListener(e -> validateSelection());

        JPanel footerPanel = new JPanel();
        footerPanel.add(validateButton);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(calendarPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void updateCalendar() {
        calendarPanel.removeAll();
        dateButtons.clear();
        monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String day : daysOfWeek) {
            calendarPanel.add(new JLabel(day, SwingConstants.CENTER));
        }

        LocalDate firstDayOfMonth = currentMonth.atDay(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < dayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            LocalDate date = currentMonth.atDay(day);

            dayButton.addActionListener(e -> handleDateSelection(dayButton, date));
            dateButtons.add(dayButton);
            calendarPanel.add(dayButton);

            // Highlight dates if they are in the selected range
            if (isDateInRange(date)) {
                dayButton.setBackground(Color.CYAN);
            }
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
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
            JOptionPane.showMessageDialog(frame, "Selected Period: " + start + " to " + end);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a valid date range.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DynamiqueCalendar::new);
    }
}

