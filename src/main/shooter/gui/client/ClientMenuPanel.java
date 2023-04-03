package src.main.shooter.gui.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import src.main.shooter.net.Server;

public class ClientMenuPanel extends JPanel {
    private final JTextField ipAddress, portNumber;

    public ClientMenuPanel() {
        ipAddress = new JTextField("localhost");
        add(ipAddress);

        portNumber = new JTextField("" + Server.DEFAULT_PORT_NUMBER);
        add(portNumber);

        add(new JButton("Join Game") {
            {
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        ((ClientMainFrame) ClientMenuPanel.this.getTopLevelAncestor()).startGame(ipAddress.getText(),
                                Integer.parseInt(portNumber.getText()));
                    }
                });
            }
        });
    }
}
