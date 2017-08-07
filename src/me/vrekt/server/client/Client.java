package me.vrekt.server.client;

import me.vrekt.chathandler.ChatMessage;
import me.vrekt.chathandler.ClientInformation;
import me.vrekt.chathandler.type.ChatType;
import me.vrekt.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Vrekt on 8/7/2017.
 */
public class Client extends Thread {

    private Socket socket;

    private ObjectInputStream clientIn;
    private ObjectOutputStream clientOut;

    private ClientInformation clientInfo;

    private ChatMessage clientMessage;

    private boolean clientConnected;

    /**
     * Initialize a new client connecting.
     *
     * @param socket the client socket.
     * @param userID the unique ID of the user.
     */
    public Client(Socket socket, int userID) {
        this.socket = socket;

        // if the socket is connected

        // protection
        if (socket == null || !socket.isConnected()) {
            Server.logCriticalMessage("Failed to connect client with user ID: " + userID);
            return;
        }

        Server.logInformation("Attempting to connect user with ID " + userID);
        try {
            // Create inputs and outputs to write to the socket and retrieve their username,
            // Create client output first because for some reason if the input is created first nothing works.
            clientOut = new ObjectOutputStream(socket.getOutputStream());
            clientIn = new ObjectInputStream(socket.getInputStream());
            // send their userID.
            clientOut.writeObject(new ChatMessage(ChatType.USERID, String.valueOf(userID)));

            ChatMessage clientUsername = (ChatMessage) clientIn.readObject();
            // security maybe? if somebody tries to send logout or who and not a message with their username.
            if (clientUsername.getChatType() == ChatType.MESSAGE) {
                clientInfo = new ClientInformation(clientUsername.getMessage(), userID);
            } else {
                Server.logCriticalMessage("Failed to authenticate and connect user. USER: " + userID);
                return;
            }

            clientConnected = true;
            Server.logInformation("User: " + clientInfo.getClientUsername() + " has connected. [ " + clientInfo.getClientID() + " ]");

        } catch (IOException | ClassNotFoundException e) {
            Server.logCriticalMessage("Failed to authenticate and connect user. USER: " + userID);
            return;
        }

    }

    /**
     * Thread.
     */
    public void run() {
        while (clientConnected) {
            updateClient();
        }
        closeConnection();
    }

    /**
     * Update the client, retrieve data and decide what to do.
     */
    public void updateClient() {
        // receive new messages
        try {
            clientMessage = (ChatMessage) clientIn.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            // do nothing.
        }

        // logout, check whos online or send message to everybody on the server.

        if (clientMessage != null) {
            switch (clientMessage.getChatType()) {
                case MESSAGE:

                    // get the username and ID.
                    String username = clientMessage.getMessageOwner().getClientUsername();
                    int ID = clientMessage.getMessageOwner().getClientID();
                    Server.logInformation("[" + ID + "][" + username + "]: " + clientMessage.getMessage());

                    Server.broadcastMessage("[" + username + "]: " + clientMessage.getMessage());
                    clientMessage = null;
                    break;
                case WHO:
                    //
                    break;
                case LOGOUT:
                    // do more stuff haven't got to yet.
                    closeConnection();
                    break;
                default:
                    break;
            }


        }
    }

    /**
     * Close the connection.
     */
    public void closeConnection() {
        clientConnected = false;
        try {
            clientIn.close();
            clientOut.close();
            socket.close();

        } catch (IOException e) {
        }
    }

    /**
     * Try to send the client a message.
     *
     * @param message the message
     * @return true or false depending if it failed or not.
     */
    public boolean sendClientMessage(ChatMessage message) {
        try {
            clientOut.writeObject(message);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Check if the client is connected.
     *
     * @return if the client is connected or not.
     */
    public boolean isClientConnected() {
        // improve later check if were actually connected.
       return clientConnected;
    }

    /**
     * Get client information.
     *
     * @return client information
     */
    public ClientInformation getClientInfo() {
        return clientInfo;
    }
}
