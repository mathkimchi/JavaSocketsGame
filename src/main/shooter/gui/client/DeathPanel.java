package src.main.shooter.gui.client;

import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DeathPanel extends JPanel {
    public DeathPanel() {
        add(new JLabel("You died."));
        add(new JButton("Replay") {
            {
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        ((ClientMainFrame) DeathPanel.this.getTopLevelAncestor()).setMenuPanel();
                        ;
                    };
                });
            }
        });
    }
}
