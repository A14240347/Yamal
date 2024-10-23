package com.example.advancedchatbot.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Index;

@Entity(tableName = "users",
        indices = {@Index(value = {"username"}, unique = true)})
public class User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "displayName")
    private String displayName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "profilePictureUrl")
    private String profilePictureUrl;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "lastSeen")
    private long lastSeen;

    // Constructor completo
    public User(String username, String displayName, String email, String profilePictureUrl, String status, long lastSeen) {
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    // Método de utilidad para verificar si el usuario está en línea
    public boolean isOnline() {
        return System.currentTimeMillis() - lastSeen < 300000; // 5 minutos
    }
}