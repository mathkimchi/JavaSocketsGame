package src.main.shooter.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import src.main.shooter.game.ClientGame;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 7352768790051012L;

    public MainFrame(final ClientGame game) {
        super("Test");

        final GamePanel gamePanel = new GamePanel(game);

        add(gamePanel, BorderLayout.CENTER);

        addKeyListener(new ClientInputHandler(game));

        pack();
        setVisible(true);
    }
}
