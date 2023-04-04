package src.main.shooter.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.TreeMap;

import src.main.shooter.game.ClientGame;
import src.main.shooter.game.ServerGame.Entity;
import src.main.shooter.gui.client.ClientMainFrame;
import src.main.shooter.net.packets.ActionPacket;
import src.main.shooter.net.packets.ClientPacket;
import src.main.shooter.net.packets.DisconnectPacket;

public class Client implements Runnable {
    private boolean isRunning;
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private ClientGame game;
    private final ClientMainFrame mainFrame;

    public ClientGame getGame() {
        return game;
    }

    public Client(final ClientMainFrame mainFrame, final String ipAddress, final int port) throws IOException {
        socket = new Socket(ipAddress, port);
        inputStream = new ObjectInputStream(socket.getInputStream());
        outputStream = new ObjectOutputStream(socket.getOutputStream());

        this.mainFrame = mainFrame;

        initialServerCommunication();
    }

    @SuppressWarnings("unchecked")
    private void initialServerCommunication() {
        try {
            final int clientId = inputStream.readInt();
            game = new ClientGame(this, clientId);

            game.processEntityList(((TreeMap<Integer, Entity>) inputStream.readObject()));

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
            } catch (final SocketException e) {
                break;
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

    public void sendPacket(final ClientPacket packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.reset();
        } catch (final IOException e) {
            e.printStackTrace();
        }
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

        mainFrame.handleDeath();
    }
}
