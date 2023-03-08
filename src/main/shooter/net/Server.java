package src.main.shooter.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import src.main.shooter.game.Game;

public class Server extends Thread {
    public final static int portNumber = 1234;

    private DatagramSocket socket;
    private Game game;

    public Server(Game game) {
        this.game = game;
        try {
            this.socket = new DatagramSocket(portNumber);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            byte[] data = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String message = new String(packet.getData());
            System.out.println("CLIENT: \"" + message + "\"");
            // System.out.println("Was message equal to \"ping\": " +
            // message.equals("ping"));
            if (message.trim().equalsIgnoreCase("ping")) {
                System.out.println("Got pinged.");
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
            }
        }
    }

    public void sendData(byte[] data, InetAddress ipAddress, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(null).start();
    }
}
