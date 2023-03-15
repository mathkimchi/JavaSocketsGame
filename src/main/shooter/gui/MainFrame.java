package src.main.shooter.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import src.main.shooter.game.ClientGame;
import src.main.shooter.net.Client;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 7352768790051012L;

    public MainFrame(final Client client, final ClientGame game) {
        super("Game");

        final GamePanel gamePanel = new GamePanel(game);

        add(gamePanel, BorderLayout.CENTER);

        addKeyListener(new ClientInputHandler(game));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                client.disconnect();
            }
        });

        pack();
        setVisible(true);
    }

}
