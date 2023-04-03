package src.main.shooter.gui.client;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import src.main.shooter.net.Client;

public class ClientMainFrame extends JFrame {
    private Client client;

    public ClientMainFrame() {
        super("Game");

        add(new ClientMenuPanel());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (client != null) {
                    client.disconnect();
                }
            }
        });

        pack();
        setVisible(true);
    }

    public void startGame(final String ipAddress, final int port) {
        getContentPane().removeAll();

        System.out.println("Connecting to game.");
        client = new Client(this, ipAddress, port);
        final ClientGamePanel gamePanel = new ClientGamePanel(client.getGame());

        add(gamePanel, BorderLayout.CENTER);

        gamePanel.requestFocusInWindow(); // must be after add gamePanel to this

        pack();
        revalidate();
        repaint();

        client.run();
    }

    public static void main(final String[] args) {
        new ClientMainFrame();
    }
}
