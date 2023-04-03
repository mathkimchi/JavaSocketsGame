package src.main.shooter;

import src.main.shooter.gui.client.ClientMainFrame;
import src.main.shooter.net.Server;

public class SinglePlayerRunner {
    public static void main(final String[] args) {
        Server.main(args);
        ClientMainFrame.main(args);
    }
}
