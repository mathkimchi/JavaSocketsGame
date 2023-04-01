package src.main.chatroom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static final int port = 1234;

    public static void main(final String[] args) throws IOException {
        new Server(new ServerSocket(port));
    }

    private final ServerSocket serverSocket;

    private ArrayList<ClientHandler> clientHandlers;

    public Server(final ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        startAcceptClientLoop();
    }

    // sender client to server to every client
    public void broadcastMessage(final ClientHandler sender, final String message) {
        for (final ClientHandler clientHandler : clientHandlers) {
            clientHandler.recieveMessage(sender, message);
        }
        System.out.println("Message has been broadcasted.");
    }

    private void sendServerMessage(final String message) {
        for (final ClientHandler clientHandler : clientHandlers) {
            clientHandler.recieveServerMessage(message);
        }

        System.out.println("Server message has been broadcasted.");
    }

    private void startAcceptClientLoop() {
        System.out.println("Starting server.");
        clientHandlers = new ArrayList<ClientHandler>();
        try {
            while (!serverSocket.isClosed()) {
                System.out.println("Waiting for new client.");
                final Socket socket = serverSocket.accept();
                System.out.println("A new client has connected.");
                final ClientHandler clientHandler = new ClientHandler(this, socket);
                clientHandlers.add(clientHandler);
                System.out.println("A new client handler has been made and added.");
                new Thread(clientHandler).start();
                System.out.println("A new client handler has been started.");
                sendServerMessage("A new user has joined.");
            }
        } catch (final IOException e) {
            System.out.println("There was an IOException.");
        }
    }
}
