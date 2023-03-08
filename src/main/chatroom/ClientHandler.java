package src.main.chatroom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private String username;
    private Server server;
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        setUsername();
        startSendMessageLoop();
    }

    // server to client
    public void recieveMessage(ClientHandler sender, String message) {
        if (sender == this) {
            return;
        }

        try {
            bufferedWriter.write(sender.username + ": " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
        }
    }

    public void recieveServerMessage(String message) {
        try {
            bufferedWriter.write("SERVER: " + message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
        }
    }

    private void setUsername() {
        try {
            username = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // client to server
    private void startSendMessageLoop() {
        try {
            while (socket.isConnected()) {
                String message = bufferedReader.readLine();
                server.broadcastMessage(ClientHandler.this, message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
