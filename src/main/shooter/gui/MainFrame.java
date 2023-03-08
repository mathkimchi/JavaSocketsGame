package src.main.shooter.gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;

import src.main.shooter.net.Client;

public class MainFrame extends JFrame {
    private Client client;

    public MainFrame() {
        super("Test");

        client = new Client(null, "localhost");
        client.start();

        JButton pingButton = new JButton("Ping server");
        pingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendData("ping".getBytes());
            }
        });

        add(pingButton);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
