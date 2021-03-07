package com.example.bank.Database;

import androidx.room.*;

import com.example.bank.Entity.Account;
import com.example.bank.Entity.Login;
import com.example.bank.dao.AccountDao;
import com.example.bank.dao.LoginDAO;

@Database(entities = {Account.class, Login.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();
    public abstract LoginDAO loginDao();
}
