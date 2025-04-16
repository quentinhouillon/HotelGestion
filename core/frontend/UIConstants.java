package core.frontend;

import java.awt.Color;
import javax.swing.JButton;

public class UIConstants {
    // Couleurs pour les boutons verts
    public static final Color GREEN_BUTTON_COLOR = new Color(163, 190, 140); // #A3BE8C
    public static final Color GREEN_HOVER_COLOR = new Color(129, 161, 105);  // Couleur légèrement plus foncée
    public static final Color GREEN_CLICK_COLOR = new Color(106, 135, 89);   // Couleur encore plus foncée

    // Couleurs pour les boutons rouges
    public static final Color RED_BUTTON_COLOR = new Color(191, 97, 106);    // #BF616A
    public static final Color RED_HOVER_COLOR = new Color(165, 84, 92);      // Couleur légèrement plus foncée
    public static final Color RED_CLICK_COLOR = new Color(139, 71, 79);      // Couleur encore plus foncée

    // Couleurs pour les boutons jaunes
    public static final Color YELLOW_BUTTON_COLOR = new Color(235, 203, 139); // #EBCB8B
    public static final Color YELLOW_HOVER_COLOR = new Color(216, 186, 127);  // Couleur légèrement plus foncée
    public static final Color YELLOW_CLICK_COLOR = new Color(197, 169, 115);  // Couleur encore plus foncée

    // Couleurs pour les boutons violets
    public static final Color PURPLE_BUTTON_COLOR = new Color(180, 142, 173); // #B48EAD
    public static final Color PURPLE_HOVER_COLOR = new Color(162, 128, 155);  // Couleur légèrement plus foncée
    public static final Color PURPLE_CLICK_COLOR = new Color(144, 114, 137);  // Couleur encore plus foncée

    // Couleurs pour les boutons oranges
    public static final Color ORANGE_BUTTON_COLOR = new Color(208, 135, 112); // #D08770
    public static final Color ORANGE_HOVER_COLOR = new Color(187, 122, 101);  // Couleur légèrement plus foncée
    public static final Color ORANGE_CLICK_COLOR = new Color(166, 109, 90);   // Couleur encore plus foncée

    // Couleurs pour les boutons bleus
    public static final Color BLUE_BUTTON_COLOR = new Color(96, 189, 255);    // #60BFFF
    public static final Color BLUE_HOVER_COLOR = new Color(80, 170, 255);     // Couleur légèrement plus foncée
    public static final Color BLUE_CLICK_COLOR = new Color(64, 150, 255);     // Couleur encore plus foncée

    public static void applyButtonEffects(JButton button, Color defaultColor, Color hoverColor, Color clickColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor); // Couleur au survol
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(defaultColor); // Couleur par défaut
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(clickColor); // Couleur lorsqu'il est pressé
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor); // Retour à la couleur au survol
            }
        });
    }
}

