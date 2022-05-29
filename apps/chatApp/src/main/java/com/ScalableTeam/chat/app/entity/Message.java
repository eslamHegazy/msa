package com.ScalableTeam.chat.app.entity;

import com.google.api.client.util.DateTime;
import com.google.cloud.Timestamp;

import java.time.LocalDateTime;

public class Message {
    String messageId;
    String authorId;
    String content;
    Timestamp timestamp;

    public Message() {

    }

    public Message(String authorId, String content) {
        this.authorId = authorId;
        this.content = content;
    }

    public Message(String authorId, String content, Timestamp timestamp) {
        this.authorId = authorId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Message(String messageId, String authorId, String content) {
        this.messageId = messageId;
        this.authorId = authorId;
        this.content = content;
    }

    public Message(String messageId, String authorId, String content, Timestamp timestamp) {
        this.messageId = messageId;
        this.authorId = authorId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", authorId='" + authorId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
