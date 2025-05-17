package core.frontend;

import java.awt.*;
import java.time.LocalDate;

import javax.swing.*;

import core.backend.*;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class WrapLayout extends FlowLayout {
    public WrapLayout() {
        super();
    }

    public WrapLayout(int align) {
        super(align);
    }

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target, false);
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getParent().getWidth();
            if (targetWidth == 0)
                targetWidth = Integer.MAX_VALUE;

            int hgap = getHgap();
            int vgap = getVgap();
            int width = 0, height = vgap, rowHeight = 0;

            for (Component comp : target.getComponents()) {
                if (!comp.isVisible())
                    continue;
                Dimension dim = preferred ? comp.getPreferredSize() : comp.getMinimumSize();

                if (width + dim.width > targetWidth) {
                    width = 0;
                    height += rowHeight + vgap;
                    rowHeight = 0;
                }

                width += dim.width + hgap;
                rowHeight = Math.max(rowHeight, dim.height);
            }

            height += rowHeight;
            return new Dimension(targetWidth, height);
        }
    }
}

class CardPanel extends JPanel {
    private String type;
    private String description;
    private int roomNumber;
    private double price;
    private ImageIcon image;
    private int etage;
    private Color accentColor;
    private Color primaryColor;

    public CardPanel(String type, String description, int roomNumber, double price, ImageIcon image, int etage) {
        this.type = type;
        this.description = description;
        this.roomNumber = roomNumber;
        this.price = price;
        this.image = image;
        this.etage = etage;
        this.accentColor = UIConstants.ACCENT_COLOR;
        this.primaryColor = UIConstants.BLUE_BUTTON_COLOR;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setBackground(accentColor);

        if (image != null) {
            Image scaledImage = this.image.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
            this.image = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(image);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            add(imageLabel, BorderLayout.NORTH);
        }

        JLabel typeLabel = new JLabel(this.type);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        add(typeLabel);

        JLabel numLabel = new JLabel("chambre n°" + this.roomNumber);
        numLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        numLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(numLabel);

        JLabel priceLabel = new JLabel("prix: " + this.price + "€");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        add(priceLabel);

        JButton dialogButton = new JButton("Voir détails");
        UIConstants.createStyledButton(dialogButton, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);

        dialogButton.addActionListener(_ -> {
            RoomDialog dialog = new RoomDialog(this.type, this.description, this.roomNumber, this.etage, this.price,
                    this.image);
            dialog.setVisible(getFocusTraversalKeysEnabled());
        });
        add(dialogButton, BorderLayout.EAST);
    }
}

class RoomDialog extends JDialog {
    private String type;
    private String description;
    private int roomNumber;
    private int etage;
    private double price;
    private ImageIcon image;
    private Color accentColor;
    private JPanel calendarMainPanel;
    private JPanel calendarPanel;
    private LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
    private JLabel monthLabel;
    private JPanel header;
    private ReservationManagement reservations = new ReservationManagement();

    public RoomDialog(String type, String description, int roomNumber, int etage, double price, ImageIcon image) {
        super((Frame) null, "Détail des chambres", true);
        this.type = type;
        this.description = description;
        this.roomNumber = roomNumber;
        this.etage = etage;
        this.price = price;
        this.image = image;
        this.accentColor = UIConstants.ACCENT_COLOR;

        initDialog();
    }

    private void initDialog() {
        setTitle("Chambre n°" + this.roomNumber);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(accentColor);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setBackground(null);
        leftPanel.add(showCalendar());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.setBackground(null);

        JLabel typeLabel = new JLabel("Type: " + type);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rightPanel.add(typeLabel);

        JLabel etageLabel = new JLabel("Étage: " + etage);
        etageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        etageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rightPanel.add(etageLabel);

        JLabel priceLabel = new JLabel("Prix: " + price + " €");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rightPanel.add(priceLabel);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        rightPanel.add(descriptionLabel);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(1, 2));
        mainPanel.setBackground(null);
        add(mainPanel, BorderLayout.CENTER);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
    }

    private JPanel showCalendar() {
        calendarMainPanel = new JPanel(new BorderLayout());
        header = new JPanel(new BorderLayout());
        JButton prev = new JButton("<");
        UIConstants.createStyledButton(prev, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        JButton next = new JButton(">");
        UIConstants.createStyledButton(next, UIConstants.BLUE_BUTTON_COLOR, Color.WHITE);
        monthLabel = new JLabel("");
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);

        prev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });

        next.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });

        header.add(prev, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(next, BorderLayout.EAST);

        calendarPanel = new JPanel(new GridLayout(0, 7));

        calendarMainPanel.add(header, BorderLayout.NORTH);
        calendarMainPanel.add(calendarPanel, BorderLayout.CENTER);
        updateCalendar();
        return calendarMainPanel;
    }

    private void updateCalendar() {
        calendarPanel.removeAll();
        List<LocalDate> dates = new ArrayList<>();

        for (Reservation reservation : reservations.getAll()) {
            if (reservation.getRoom().getRoomNumber() == this.roomNumber) {
                LocalDate starDate = reservation.getDuration()[0];
                LocalDate enDate = reservation.getDuration()[1];
                while (!starDate.isAfter(enDate)) {
                    if (starDate.getMonth().equals(currentMonth.getMonth())
                            && starDate.getYear() == currentMonth.getYear()) {
                        dates.add(starDate);
                    }
                    starDate = starDate.plusDays(1);
                }
            }
        }

        // Ajouter les noms des jours
        for (DayOfWeek day : DayOfWeek.values()) {
            JLabel lbl = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.FRANCE));
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            calendarPanel.add(lbl);
        }

        YearMonth yearMonth = YearMonth.from(currentMonth);
        int daysInMonth = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = currentMonth;
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        // Espaces vides avant le 1er jour
        for (int i = 1; i < firstDayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.withDayOfMonth(day);
            JLabel dayButton = new JLabel(String.valueOf(day));
            calendarPanel.add(dayButton);
            if (dates.contains(date))
                dayButton.setForeground(UIConstants.YELLOW_BUTTON_COLOR);
        }

        monthLabel.setText(
                currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE) + " " + currentMonth.getYear());
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
}

public class RoomPanel extends JPanel {
    Color cardColor;
    Color primaryColor;
    RoomManagemment rooms = new RoomManagemment();

    public RoomPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Liste des Chambres");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 10, 10));
        mainPanel.setBackground(null);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(UIConstants.MAIN_COLOR);
        scrollPane.setBorder(null);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        for (Room room : this.rooms.getRooms()) {
            ImageIcon roomImage = new ImageIcon(room.getLinkImage());
            CardPanel card = new CardPanel(
                    room.getType(),
                    room.getDescription(),
                    room.getRoomNumber(),
                    room.getPrice(),
                    roomImage,
                    room.getEtage());
            mainPanel.add(card);
        }
    }
}