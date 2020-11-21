package com.example.mapapplication.data.user;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_table")
public class UserEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Long id;
    public String email;
    public String username;
    public String password;

    public UserEntity() {
        // Default constructor required for calls to DataSnapshot.getValue(UserEntity.class)
    }

    public UserEntity(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
