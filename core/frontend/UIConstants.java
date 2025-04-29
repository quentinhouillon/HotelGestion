package core.frontend;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

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
    public static final Color BLUE_BUTTON_COLOR = new Color(94, 129, 172);    // #5E81AC
    public static final Color BLUE_HOVER_COLOR = new Color(74, 101, 135);     // Couleur légèrement plus foncée
    public static final Color BLUE_CLICK_COLOR = new Color(65, 89, 119);     // Couleur encore plus foncée

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

    public static JTextField createStyledTextField(Color backgroundColor, Color textColor, Color borderColor) {
        JTextField textField = new JTextField();
        textField.setBackground(backgroundColor);
        textField.setForeground(textColor);
        textField.setCaretColor(textColor); // Couleur du curseur
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1), // Bordure extérieure
            BorderFactory.createEmptyBorder(5, 5, 5, 5)    // Marges internes
        ));
        return textField;
    }

    public static JButton createStyledButton(String text, Color backgroundColor, Color hoverColor, Color clickColor, Dimension size) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setMaximumSize(size);

        // Ajouter des effets visuels pour le bouton
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(clickColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
        });

        return button;
    }
}

