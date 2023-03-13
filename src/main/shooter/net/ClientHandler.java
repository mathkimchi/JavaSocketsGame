package src.main.shooter.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;

import src.main.shooter.game.Entity;

public class ClientHandler implements Runnable {
    private final int entityId;
    private Server server;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientHandler(Server server, Socket socket, int id) {
        this.server = server;
        this.socket = socket;
        this.entityId = id;

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        initialClientCommunication();
    }

    private void initialClientCommunication() {
        try {
            outputStream.writeInt(entityId);
            server.sendUpdates(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        startRecieveMessageLoop();
    }

    // client to server
    private void startRecieveMessageLoop() {
        while (socket.isConnected()) {
            try {
                Packet packet = (Packet) inputStream.readObject();
                server.processPacket(entityId, packet);
                // System.out.println(packet.actionSet.getLongActions().size());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    // server to client
    public void sendUpdate(TreeMap<Integer, Entity> update) {
        try {
            outputStream.writeObject(update);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
