package com.example.bank.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.bank.Entity.Login;
import com.example.bank.dao.LoginDAO;


@Database(entities = {Login.class}, version = 1)
public abstract class LoginDatabase extends RoomDatabase {
    public abstract LoginDAO loginDao();
}
