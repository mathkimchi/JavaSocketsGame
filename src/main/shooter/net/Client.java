package src.main.shooter.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.entities.Entity;
import src.main.shooter.gui.MainFrame;
import src.main.shooter.net.packets.ActionPacket;
import src.main.shooter.net.packets.DisconnectPacket;
import src.main.shooter.net.packets.Packet;

public class Client implements Runnable {
    private boolean isRunning;
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

            mainFrame = new MainFrame(this, game);

            System.out.println("Finished initial server communication.");
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        isRunning = true;
        new Thread(() -> startReadAndWriteLoop()).start();
        new Thread(() -> startGameloop()).start();
    }

    @SuppressWarnings("unchecked") // if type is bad, then it should throw an error anyways
    private void startReadAndWriteLoop() {
        while (isRunning) {
            try {
                // read
                game.processEntityList(((TreeMap<Integer, Entity>) inputStream.readObject()));

                // write
                sendPacket(new ActionPacket(game));
                game.getActionSet().getInstantActions().clear();
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGameloop() {
        while (isRunning) {
            game.tick();
            mainFrame.repaint();
        }
    }

    public void sendPacket(final Packet packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.reset();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) {
        new Client("localhost", Server.portNumber).run();
    }

    public void disconnect() {
        System.out.println("Disconnecting from server.");

        isRunning = false;

        // tell server we disconnected
        sendPacket(new DisconnectPacket());

        // close everything
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        System.exit(0); // Mostly to stop the read
    }
}
