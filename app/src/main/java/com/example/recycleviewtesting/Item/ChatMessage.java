package com.example.recycleviewtesting.Item;
public class ChatMessage {
    private String sender;
    private String text;
    private long timestamp;

    // Constructor
    public ChatMessage(String sender, String text, long timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    // Getters
    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
