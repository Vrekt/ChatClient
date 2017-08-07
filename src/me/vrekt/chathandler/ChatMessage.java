package me.vrekt.chathandler;

import me.vrekt.chathandler.type.ChatType;

import java.io.Serializable;

/**
 * Created by Vrekt on 8/7/2017.
 */
public class ChatMessage implements Serializable {

    private ChatType chatType;
    private String message;

    public ChatMessage(ChatType type, String message) {
        this.chatType = type;
        this.message = message;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public String getMessage() {
        return message;
    }


}
