package me.vrekt.chathandler;

import java.io.Serializable;

/**
 * Created by Vrekt on 8/7/2017.
 */
public class ClientInformation implements Serializable {

    private String clientUsername;
    private int clientID;

    /**
     * Client information.
     * @param clientUsername the clients username
     * @param clientID the clients unique ID
     */
    public ClientInformation(String clientUsername, int clientID) {
        this.clientUsername = clientUsername;
        this.clientID = clientID;
    }

    /**
     * Get the client username.
     *
     * @return clientUsername
     */
    public String getClientUsername() {
        return clientUsername;
    }

    /**
     * Set the client username.
     *
     * @param clientUsername the clients username
     */
    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    /**
     * Get the clients unique ID.
     *
     * @return
     */
    public int getClientID() {
        return clientID;
    }

}
