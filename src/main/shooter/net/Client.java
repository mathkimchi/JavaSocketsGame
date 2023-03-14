package src.main.shooter.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.Entity;
import src.main.shooter.gui.MainFrame;

public class Client implements Runnable {
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientGame game;
    private MainFrame mainFrame;

    public Client(final String ipAddress, final int port) {
        try {
            socket = new Socket(ipAddress, port);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (final IOException e) {
            e.printStackTrace();
        }

        initialServerCommunication();
    }

    @SuppressWarnings("unchecked")
    private void initialServerCommunication() {
        try {
            final int clientId = inputStream.readInt();
            game = new ClientGame(clientId);

            game.processEntityList(((TreeMap<Integer, Entity>) inputStream.readObject()));

            mainFrame = new MainFrame(game);

            System.out.println("Finished initial server communication.");
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        new Thread(() -> startReadAndWriteLoop()).start();
        new Thread(() -> startGameloop()).start();
    }

    @SuppressWarnings("unchecked") // if type is bad, then it should throw an error anyways
    private void startReadAndWriteLoop() {
        while (true) {
            try {
                // read
                // System.out.println("Waiting to recieve.");
                game.processEntityList(((TreeMap<Integer, Entity>) inputStream.readObject()));

                // write
                final Packet packetToSend = new Packet(game);
                outputStream.writeObject(packetToSend);
                outputStream.reset();
                game.getActionSet().getInstantActions().clear();
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGameloop() {
        while (true) {
            game.tick();
            mainFrame.repaint();
        }
    }

    public void send(final String s) {
        try {
            outputStream.writeObject(s);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        new Client("localhost", Server.portNumber).run();
    }
}
