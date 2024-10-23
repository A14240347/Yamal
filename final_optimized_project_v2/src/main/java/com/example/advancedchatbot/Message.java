package com.example.advancedchatbot.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

import java.util.Date;

@Entity(tableName = "messages",
        indices = {
                @Index(value = {"conversationId"}, unique = false),
                @Index(value = {"timestamp"}, unique = false),
                @Index(value = {"type"}, unique = false)
        })
public class Message {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "conversationId")
    private int conversationId;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "isFromBot")
    private boolean isFromBot;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "isRead")
    private boolean isRead;

    @ColumnInfo(name = "type")
    private String type;  // Valores posibles: "text", "image", "audio", etc.

    @ColumnInfo(name = "attachmentUrl")
    private String attachmentUrl;  // URL para archivos adjuntos como imágenes o audio

    @ColumnInfo(name = "priority")
    private int priority;  // Define la prioridad del mensaje (por ejemplo, mensajes de advertencia pueden tener una prioridad alta)

    @ColumnInfo(name = "repliedToMessageId")
    private Integer repliedToMessageId;  // ID del mensaje al que se está respondiendo

    // Constructor completo para inicializar todos los campos
    public Message(int conversationId, String text, boolean isFromBot, long timestamp, 
                   boolean isRead, String type, String attachmentUrl, int priority, Integer repliedToMessageId) {
        this.conversationId = conversationId;
        this.text = text;
        this.isFromBot = isFromBot;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.type = type;
        this.attachmentUrl = attachmentUrl;
        this.priority = priority;
        this.repliedToMessageId = repliedToMessageId;
    }

    // Getters y Setters para cada campo
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFromBot() {
        return isFromBot;
    }

    public void setFromBot(boolean fromBot) {
        isFromBot = fromBot;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Integer getRepliedToMessageId() {
        return repliedToMessageId;
    }

    public void setRepliedToMessageId(Integer repliedToMessageId) {
        this.repliedToMessageId = repliedToMessageId;
    }

    // Método de utilidad para verificar si el mensaje tiene un archivo adjunto
    public boolean hasAttachment() {
        return this.attachmentUrl != null && !this.attachmentUrl.isEmpty();
    }

    // Método para verificar si es un mensaje de texto
    public boolean isTextMessage() {
        return "text".equals(this.type);
    }

    // Método para marcar el mensaje como leído
    public void markAsRead() {
        this.isRead = true;
    }

    // Método para retornar un resumen del mensaje
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", conversationId=" + conversationId +
                ", text='" + text + '\'' +
                ", isFromBot=" + isFromBot +
                ", timestamp=" + timestamp +
                ", isRead=" + isRead +
                ", type='" + type + '\'' +
                ", attachmentUrl='" + attachmentUrl + '\'' +
                ", priority=" + priority +
                ", repliedToMessageId=" + repliedToMessageId +
                '}';
    }
}