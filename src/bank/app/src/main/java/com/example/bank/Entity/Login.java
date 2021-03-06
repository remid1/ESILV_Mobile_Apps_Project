package com.example.bank.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Login {
    @NonNull
    @PrimaryKey
    public String hash;

    public Login(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
}
