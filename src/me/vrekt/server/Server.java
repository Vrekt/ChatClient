package me.vrekt.server;

import me.vrekt.chathandler.ChatMessage;
import me.vrekt.chathandler.type.ChatType;
import me.vrekt.server.client.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vrekt on 8/7/2017.
 */
public class Server extends Thread {

    private boolean serverRunning;

    private static ArrayList<Client> connectedClients = new ArrayList<>();
    private ServerSocket server;

    /**
     * Initialize the server.
     *
     * @param port the server port.
     */
    public Server(int port) {
        // try to create new server socket.
        try {
            logInformation("Creating new ServerSocket on port " + port);
            server = new ServerSocket(port);
        } catch (IOException e) {
            logCriticalMessage("Failed to start server, error with creating ServerSocket on port: " + port);
            e.printStackTrace();
            stopServer();
        }

    }

    /**
     * Log a critical message.
     *
     * @param message the message
     */
    public static void logCriticalMessage(String message) {
        System.out.println("[CRITICAL] " + message);
    }

    /**
     * General info like a user connecting.
     *
     * @param message the message
     */
    public static void logInformation(String message) {
        System.out.println("[INFO]" + message);
    }

    /**
     * Broadcast a message to everybody on the server.
     *
     * @param message the message
     */
    public static void broadcastMessage(String message) {
        connectedClients.forEach(client -> client.sendClientMessage(new ChatMessage(ChatType.MESSAGE, message)));
    }

    /**
     * Update the connected clients list.
     */
    private void validateClients() {
        List<Client> invalidClients = connectedClients.stream().filter(client -> !client.isClientConnected()).collect(Collectors.toList());
        invalidClients.forEach(client -> connectedClients.remove(client));

        if (!invalidClients.isEmpty()) {
            invalidClients.forEach(client -> logInformation(client.getClientInfo().getClientUsername() + " has logged out."));
        }

    }

    /**
     * Start the server.
     */
    public void startServer() {
        logInformation("Starting server....");
        serverRunning = true;
        this.start();
    }


    /**
     * Stop the server.
     */
    public void stopServer() {
        serverRunning = false;
        connectedClients.forEach(client -> client.closeConnection());
        connectedClients.clear();
    }

    /**
     * Connect a client.
     *
     * @param client the client.
     */
    private void connectClient(Client client) {
        connectedClients.add(client);
        client.start();
    }

    /**
     * Handle all the server stuff.
     */
    public void run() {
        logInformation("Server started.");
        while (serverRunning) {
            // validate clients.
            validateClients();

            // accept and create new socket.
            try {
                Socket socket = server.accept();
                if (!serverRunning) {
                    break;
                }

                connectClient(new Client(socket, 1));


            } catch (IOException e) {
                // do nothing?
            }


        }

        stopServer();

    }

}
