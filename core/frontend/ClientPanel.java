package core.frontend;

import java.awt.*;
import javax.swing.*;

import core.backend.*;

class ListPanel extends JPanel {

}

public class ClientPanel extends JPanel {
    Color listColor;
    ClientManagement clients = new ClientManagement();

    public ClientPanel(Color accentColor) {
        this.listColor = accentColor;
    }
}