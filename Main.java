import java.awt.*;
import javax.swing.*;

import core.frontend.*;

public class Main extends JFrame {
    public Main() {
        // THEME MANAGER
        Color mainColor = new Color(0x2e3440);
        Color accentColor = new Color(0x3b4252);
        Color primaryColor = new Color(0x5e81ac);

        setBackground(mainColor);
        UIManager.put("Panel.background", mainColor);
        UIManager.put("Button.background", primaryColor);

        UIManager.put("Button.foreground", Color.white);
        UIManager.put("Label.foreground", Color.WHITE);

        setTitle("Hotel Continental");
        setPreferredSize(new Dimension(850, 725));
        setMinimumSize(new Dimension(700, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        HomePanel homePanel = new HomePanel();
        ClientPanel clientPanel = new ClientPanel(accentColor);
        RoomPanel roomPanel = new RoomPanel(accentColor);
        
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.add(homePanel);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(130, 600));
        sidebar.setBackground(accentColor);

        // BUTTON ICON
        ImageIcon roomIcon = new ImageIcon("./icons/room.png");
        Image scaledRoomImage = roomIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        ImageIcon clientIcon = new ImageIcon("./icons/client.png");
        Image scaledClientImage = clientIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        

        ImageIcon homeIcon = new ImageIcon("./icons/home.png");
        Image scaledHomeImage = homeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        ImageIcon reservationIcon = new ImageIcon("./icons/reservation.png");
        Image scaledReservationImage = reservationIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        ImageIcon stayIcon = new ImageIcon("./icons/stay.png");
        Image scaledStayImage = stayIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        // SIDEBAR BUTTON
        JButton homeButton = new JButton("Accueil");
        homeButton.setIcon(new ImageIcon(scaledHomeImage));
        homeButton.addActionListener(_ -> {
                main.removeAll();
                main.add(homePanel);
                main.revalidate();
                main.repaint();
        });
        sidebar.add(homeButton);

        JButton clientButton = new JButton("Client");
        clientButton.setIcon(new ImageIcon(scaledClientImage));
        clientButton.addActionListener(_ -> {
                main.removeAll();
                main.add(clientPanel);
                main.revalidate();
                main.repaint();
        });
        sidebar.add(clientButton);

        JButton roomButton = new JButton("Chambres");
        roomButton.setIcon(new ImageIcon(scaledRoomImage));
        roomButton.addActionListener(_ -> {
            main.removeAll();
            main.add(roomPanel);
            main.revalidate();
            main.repaint();
        });
        sidebar.add(roomButton);

        JButton reservationButton = new JButton("Réservations");
        reservationButton.setIcon(new ImageIcon(scaledReservationImage));
        // sidebar.add(reservationButton);

        JButton stayButton = new JButton("Séjour");
        stayButton.setIcon(new ImageIcon(scaledStayImage));
        // sidebar.add(stayButton);

        // Add buttons to an array
        JButton[] buttons = {roomButton, homeButton, reservationButton, stayButton, clientButton};
        configButton(buttons);


        // PANEL ADD
        add(sidebar, BorderLayout.WEST);
        add(main, BorderLayout.CENTER);
    }

    void configButton(JButton[] buttons) {
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            button.setBackground(null);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);


            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (!button.getBackground().equals(getBackground())) {
                        button.setBackground(Color.GRAY);
                        button.setForeground(Color.WHITE);
                        button.setOpaque(true);
                    }
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!button.getBackground().equals(getBackground())) {
                        button.setBackground(null);
                        button.setForeground(Color.WHITE);
                        button.setOpaque(false);
                    }
                }
            });

            button.addActionListener(_ -> {
                for (JButton b : buttons) {
                    if (!b.equals(button)) {
                        b.setBackground(null);
                        // button.setForeground(Color.BLACK);
                        b.setOpaque(false);
                    }
                }
                button.setBackground(getBackground());
                button.setOpaque(true);
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main mainFrame = new Main();
            mainFrame.setVisible(true);
        });

    }
}