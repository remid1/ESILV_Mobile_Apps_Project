package com.example.bank.dao;

import androidx.room.*;

import com.example.bank.Entity.Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Query("SELECT * FROM account")
    List<Account> getAll();

    @Query("SELECT * FROM account WHERE id IN (:userIds)")
    List<Account> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM account WHERE account_name LIKE :first AND " + "amount LIKE :last LIMIT 1")
    Account findByName(String first, String last);

    @Insert
    void insertAll(Account... accounts);

    @Insert
    void insert(Account account);

    //@Update
    //fun updateAll(vararg accounts: Account)

    //@Update
    //fun update(account: Account)

    @Query("DELETE FROM account")
    void delete();
}
