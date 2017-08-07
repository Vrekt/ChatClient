package me.vrekt.client;

import me.vrekt.chathandler.ChatMessage;
import me.vrekt.chathandler.type.ChatType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Vrekt on 8/7/2017.
 */
public class Client {

    private Socket socket;

    private ObjectInputStream serverIn;
    private ObjectOutputStream serverOut;

    private static String username, server;
    private static int port;

    static boolean isConnected;

    /**
     * Initialize.
     *
     * @param username the username of the client
     * @param server   the server address
     * @param port     the server port
     */
    public Client(String username, String server, int port) {
        this.username = username;
        this.server = server;
        this.port = port;
    }

    /**
     * Main method.
     *
     * @param args
     */
    public static void main(String[] args) {

        // more options later like choosing port etc.
        Client client = new Client("username", "localhost", 1569);
        // attempt to connect.
        if (!client.attemptConnect()) {
            System.out.println("Failed to connect.");
            System.exit(1);
        }

        Scanner input = new Scanner(System.in);
        // while connected get and send messages to the server.
        while (isConnected) {

            System.out.print("> ");
            String message = input.nextLine();

            client.sendMessage(new ChatMessage(ChatType.MESSAGE, message));

        }

    }

    /**
     * Attempt to connect to the server.
     *
     * @return if we failed to connect or not.
     */
    public boolean attemptConnect() {
        // attempt to create new socket.
        try {
            socket = new Socket(server, port);
        } catch (IOException e) {
            return false;
        }

        // create serverIn and serverOut
        try {
            System.out.println("Attempting to connect to " + server);

            serverOut = new ObjectOutputStream(socket.getOutputStream());
            serverIn = new ObjectInputStream(socket.getInputStream());

            isConnected = true;
            // start the serverlistener and send our username to the server.
            new ServerListener().start();
            serverOut.writeObject(new ChatMessage(ChatType.MESSAGE, username));

        } catch (IOException e) {
            return false;
        }

        return true;

    }

    /**
     * Disconnect from the server.
     */
    private void disconnect() {
        try {
            // close everything.
            socket.close();
            serverIn.close();
            serverOut.close();
        } catch (IOException e) {
            // do nothing.
        }
    }

    /**
     * Send a message to the server.
     *
     * @param message the message
     */
    private void sendMessage(ChatMessage message) {
        try {
            serverOut.writeObject(message);
        } catch (IOException e) {
            System.out.println("Failed to send message: " + message.getMessage());
        }
    }

    /**
     * Class for receiving data from the server.
     */
    class ServerListener extends Thread {

        public void run() {
            // while connected run this loop.
            while (isConnected) {

                try {
                    // retrieve chatmessage and display it.
                    ChatMessage message = (ChatMessage) serverIn.readObject();
                    System.out.println(message.getMessage());
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    isConnected = false;
                    disconnect();
                    break;
                }

            }
        }

    }

}
