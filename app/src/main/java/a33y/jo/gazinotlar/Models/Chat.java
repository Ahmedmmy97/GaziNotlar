package a33y.jo.gazinotlar.Models;

import java.util.ArrayList;
import java.util.List;

public class Chat{
    String sender;
    String receiver;
    String message;
    String id;
    boolean isSeen;

    static List<Chat> chats = new ArrayList<>();
    static List<Chat> allchats = new ArrayList<>();
    public Chat() {
    }

    public Chat(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isSeen = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public static List<Chat> getAllchats() {
        return allchats;
    }

    public static void setAllchats(List<Chat> allchats) {
        Chat.allchats = allchats;
    }

    public static List<Chat> getChats() {
        return chats;
    }

    public static void setChats(List<Chat> chats) {
        Chat.chats = chats;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
