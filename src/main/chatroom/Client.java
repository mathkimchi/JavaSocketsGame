package src.main.chatroom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        new Client(Server.port);
    }

    private Socket socket;
    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;

    public Client(int port) {
        try {
            socket = new Socket("localhost", port);
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            setUsername();
            startReadMessageLoop();
            startSendMessageLoop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUsername() throws IOException {
        System.out.print("Enter your username: ");
        try (Scanner scanner = new Scanner(System.in)) {
            bufferedWriter.write(scanner.nextLine());
        }
        bufferedWriter.newLine();
        bufferedWriter.flush();
        System.out.println("Sent username information.");
    }

    private void startSendMessageLoop() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (socket.isConnected()) {
                bufferedWriter.write(scanner.nextLine());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            System.out.println("Send message loop done.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // new Thread(new Runnable() {
        // @Override
        // public void run() {
        // }
        // }).start();
    }

    private void startReadMessageLoop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (socket.isConnected()) {
                        System.out.println(bufferedReader.readLine());
                    }
                    System.out.println("Read message loop done.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
