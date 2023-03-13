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

    public Client(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        initialServerCommunication();
    }

    @SuppressWarnings("unchecked")
    private void initialServerCommunication() {
        try {
            int clientId = inputStream.readInt();
            game = new ClientGame(clientId);

            game.processEntityList(((TreeMap<Integer, Entity>) inputStream.readObject()));

            mainFrame = new MainFrame(game);

            System.out.println("Finished initial server communication.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
                outputStream.writeObject(new Packet(game.getActionSet()));
                if (game.getActionSet().getLongActions().size() > 0) {
                    System.out.println("Sent: " + game.getActionSet().getLongActions().size());
                }
                game.getActionSet().getInstantActions().clear();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
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

    public void send(String s) {
        try {
            outputStream.writeObject(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client("localhost", Server.portNumber).run();
    }
}
