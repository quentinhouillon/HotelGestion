package core.frontend;

import java.awt.*;
import javax.swing.*;

import core.backend.*;

class CardPanel extends JPanel {
    private String type;
    private String description;
    private int etage;
    private double price;
    private ImageIcon image;
    private Color color;

    public CardPanel(String type, String description, int etage, double price, ImageIcon image, Color color) {
        this.type = type;
        this.description = description;
        this.etage = etage;
        this.price = price;
        this.image = image;
        this.color = color;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        setBackground(color);

        if (image != null) {
            Image scaledImage = this.image.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
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
        priceLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(priceLabel);

        JLabel descriptionLabel = new JLabel(this.description);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 5, 20, 5));
        add(descriptionLabel, BorderLayout.CENTER);

    }
}

public class RoomPanel extends JPanel {
    Color cardColor;
    RoomManagemment rooms = new RoomManagemment();

    public RoomPanel(Color cardColor) {
        this.cardColor = cardColor;
        createRoom();

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (RoomType room : this.rooms.getRooms()) {
            ImageIcon roomImage = new ImageIcon(room.getLinkImage());
            CardPanel card = new CardPanel(
                room.getType(),
                room.getDescription(),
                room.getEtage(),
                room.getPrice(),
                roomImage,
                cardColor
            );
            add(card);
        }
    }

    private void createRoom() {
        this.rooms.addRoom(new RoomType(0, "Description", 1, "./img/chambreSimple1.jpg"));
        this.rooms.addRoom(new RoomType(0, "Description", 2, "./img/chambreSimple2.jpg"));
        this.rooms.addRoom(new RoomType(1, "Description", 1, "./img/chambreDouble1.jpg"));
        this.rooms.addRoom(new RoomType(3, "Description", 1, "./img/suitePrésidentielle.jpg"));
    }

}