package src.main.shooter.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import src.main.shooter.game.ClientGame;

public class MainFrame extends JFrame {
    public MainFrame(ClientGame game) {
        super("Test");

        GamePanel gamePanel = new GamePanel(game);

        add(gamePanel, BorderLayout.CENTER);

        addKeyListener(new ClientInputHandler(game));

        pack();
        setVisible(true);
    }
}
