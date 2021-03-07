package com.example.bank.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.bank.Entity.Login;

import java.util.List;

@Dao
public interface LoginDAO {
    @Query("SELECT * FROM Login")
    List<Login> getAll();

    @Insert
    void insert(Login login);
}
