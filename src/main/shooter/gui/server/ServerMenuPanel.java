package src.main.shooter.gui.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import src.main.shooter.net.Server;

public class ServerMenuPanel extends JPanel {
    private final JComboBox<String> gameType;
    private final JTextField portNumber;

    public ServerMenuPanel() {
        final String[] gameTypes = {
                "Red vs Blue",
                "F4A"
        };
        gameType = new JComboBox<String>(gameTypes);
        add(gameType);

        portNumber = new JTextField("" + Server.DEFAULT_PORT_NUMBER);
        add(portNumber);

        add(new JButton("Start Server") {
            {
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        ((ServerMainFrame) ServerMenuPanel.this.getTopLevelAncestor())
                                .startServer(Integer.parseInt(portNumber.getText()),
                                        gameTypes[gameType.getSelectedIndex()]);
                    }
                });
            }
        });
    }
}
