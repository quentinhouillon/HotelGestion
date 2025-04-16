package core.frontend;

import java.awt.*;
import javax.swing.*;

import core.backend.*;

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
            if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;
            
            int hgap = getHgap();
            int vgap = getVgap();
            int width = 0, height = vgap, rowHeight = 0;

            for (Component comp : target.getComponents()) {
                if (!comp.isVisible()) continue;
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
    private int etage;
    private double price;
    private ImageIcon image;
    private Color accentColor;
    private Color primaryColor;

    public CardPanel(String type, String description, int etage, double price, ImageIcon image, Color color, Color primary) {
        this.type = type;
        this.description = description;
        this.etage = etage;
        this.price = price;
        this.image = image;
        this.accentColor = color;
        this.primaryColor = primary;
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

        JLabel typeLabel = new JLabel(this.type + " - " + " étage n°" + this.etage);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        typeLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        add(typeLabel);

        JLabel priceLabel = new JLabel("prix: " + this.price + "€");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        add(priceLabel);
        
        // JLabel descriptionLabel = new JLabel(this.description);
        // descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 20, 5));
        // add(descriptionLabel, BorderLayout.CENTER);
        
        JButton dialogButton = new JButton("Voir détail");
        dialogButton.setBackground(primaryColor);
        dialogButton.setFocusPainted(false);
        dialogButton.setBorderPainted(false);
        dialogButton.setOpaque(true);


        dialogButton.addActionListener(_ -> {
            RoomDialog dialog = new RoomDialog(this.type, this.description, this.etage, this.price, this.image, this.accentColor);
            dialog.setVisible(getFocusTraversalKeysEnabled());
        });
        add(dialogButton, BorderLayout.EAST);
    }
}

class RoomDialog extends JDialog {
    private String type;
    private String description;
    private int etage;
    private double price;
    private ImageIcon image;
    private Color accentColor;

    public RoomDialog(String type, String description, int etage, double price, ImageIcon image, Color color) {
        super((Frame) null, "Détail des chambres", true);
        this.type = type;
        this.description = description;
        this.etage = etage;
        this.price = price;
        this.image = image;
        this.accentColor = color;

        initDialog();
    }

    private void initDialog() {
        setTitle("Room Details");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(accentColor);


        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(accentColor);

        if (image != null) {
            Image scaledImage = image.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(scaledIcon);
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(imageLabel);
        }

        JLabel typeLabel = new JLabel("Type: " + type);
        typeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        typeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(typeLabel);

        JLabel etageLabel = new JLabel("Étage: " + etage);
        etageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        etageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(etageLabel);

        JLabel priceLabel = new JLabel("Prix: " + price + "€");
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(priceLabel);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(descriptionLabel);

        add(contentPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.addActionListener(_ -> dispose());
    }
}

public class RoomPanel extends JPanel {
    Color cardColor;
    Color primaryColor;
    RoomManagemment rooms = new RoomManagemment();

    public RoomPanel(Color cardColor, Color primary) {
        this.cardColor = cardColor;
        this.primaryColor = primary;
        createRoom();

        setLayout(new WrapLayout(FlowLayout.LEFT, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (RoomType room : this.rooms.getRooms()) {
            ImageIcon roomImage = new ImageIcon(room.getLinkImage());
            CardPanel card = new CardPanel(
                room.getType(),
                room.getDescription(),
                room.getEtage(),
                room.getPrice(),
                roomImage,
                cardColor,
                primaryColor
            );
            add(card);
        }
    }

    private void createRoom() {
        this.rooms.addRoom(new RoomType(0, "Chambre simple économique, idéale pour une personne, avec lit confortable et accès Wi-Fi gratuit.", 1, "./img/chambreSimple1.jpg"));
        this.rooms.addRoom(new RoomType(0, "Chambre individuelle compacte, offrant un espace fonctionnel avec salle de bain privée et petit bureau.", 2, "./img/chambreSimple2.jpg"));
        this.rooms.addRoom(new RoomType(1, "Chambre double économique, parfaite pour deux, avec lits jumeaux et salle de bain privée.", 1, "./img/chambreDouble1.jpg"));
        this.rooms.addRoom(new RoomType(1, "Chambre standard pour deux personnes, offrant un lit double confortable et un espace de rangement.", 2, "./img/chambreDouble2.jpg"));
        this.rooms.addRoom(new RoomType(2, "Suite spacieuse offrant un coin salon, lit double confortable et salle de bain avec baignoire, parfaite pour deux ou plus.", 3, "./img/suiteNormale1.jpg"));
        this.rooms.addRoom(new RoomType(3, "Suite présidentielle prestigieuse offrant un espace de vie somptueux, terrasse privée et services exclusifs pour un séjour inoubliable", 4, "./img/suitePrésidentielle.jpg"));
    }

}