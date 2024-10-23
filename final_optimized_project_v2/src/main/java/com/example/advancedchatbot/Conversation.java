package com.example.advancedchatbot.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;
import androidx.room.ForeignKey;

@Entity(tableName = "conversations",
        indices = {@Index(value = {"participantOneId", "participantTwoId"}, unique = true)},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                            parentColumns = "id",
                            childColumns = "participantOneId",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                            parentColumns = "id",
                            childColumns = "participantTwoId",
                            onDelete = ForeignKey.CASCADE)
        })
public class Conversation {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "participantOneId")
    private int participantOneId;

    @ColumnInfo(name = "participantTwoId")
    private int participantTwoId;

    @ColumnInfo(name = "lastMessage")
    private String lastMessage;

    @ColumnInfo(name = "lastMessageTimestamp")
    private long lastMessageTimestamp;

    @ColumnInfo(name = "unreadCount")
    private int unreadCount;

    // Constructor completo
    public Conversation(int participantOneId, int participantTwoId, String lastMessage, long lastMessageTimestamp, int unreadCount) {
        this.participantOneId = participantOneId;
        this.participantTwoId = participantTwoId;
        this.lastMessage = lastMessage;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.unreadCount = unreadCount;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParticipantOneId() {
        return participantOneId;
    }

    public void setParticipantOneId(int participantOneId) {
        this.participantOneId = participantOneId;
    }

    public int getParticipantTwoId() {
        return participantTwoId;
    }

    public void setParticipantTwoId(int participantTwoId) {
        this.participantTwoId = participantTwoId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    // Método para verificar si hay mensajes no leídos
    public boolean hasUnreadMessages() {
        return unreadCount > 0;
    }
}