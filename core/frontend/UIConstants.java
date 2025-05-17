package core.frontend;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BorderFactory;

public class UIConstants {
    // Main color
    public static final Color MAIN_COLOR = new Color(0x2e3440);

    // Accent Color
    public static final  Color ACCENT_COLOR = new Color(0x3b4252);

    // Couleurs pour les boutons verts
    public static final Color GREEN_BUTTON_COLOR = new Color(163, 190, 140); // #A3BE8C

    // Couleurs pour les boutons rouges
    public static final Color RED_BUTTON_COLOR = new Color(191, 97, 106);    // #BF616A

    // Couleurs pour les boutons jaunes
    public static final Color YELLOW_BUTTON_COLOR = new Color(235, 203, 139); // #EBCB8B

    // Couleurs pour les boutons violets
    public static final Color PURPLE_BUTTON_COLOR = new Color(180, 142, 173); // #B48EAD

    // Couleurs pour les boutons oranges
    public static final Color ORANGE_BUTTON_COLOR = new Color(208, 135, 112); // #D08770

    // Couleurs pour les boutons bleus
    public static final Color BLUE_BUTTON_COLOR = new Color(94, 129, 172);    // #5E81AC

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

    public static void createStyledButton(JButton button, Color backgroundColor, Color foregroundColor) {
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Marges internes
        applyButtonEffects(button, backgroundColor, backgroundColor.darker(), backgroundColor.darker().darker());
    }
}

