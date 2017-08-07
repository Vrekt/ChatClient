package me.vrekt.chathandler;

import me.vrekt.chathandler.type.ChatType;

import java.io.Serializable;

/**
 * Created by Vrekt on 8/7/2017.
 */
public class ChatMessage implements Serializable {

    private ChatType chatType;
    private String message;

    private ClientInformation messageOwner;

    public ChatMessage(ChatType type, String message, ClientInformation messageOwner) {
        this.chatType = type;
        this.message = message;

        this.messageOwner = messageOwner;

    }

    public ChatMessage(ChatType type, String message) {
        this.chatType = type;
        this.message = message;
    }

    public ChatMessage(ChatType type) {
        this.chatType = type;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public String getMessage() {
        return message;
    }

    public ClientInformation getMessageOwner() {
        return messageOwner;
    }

    public void setMessageOwner(ClientInformation messageOwner) {
        this.messageOwner = messageOwner;
    }
}
