package com.example.bank.Entity;

import androidx.room.*;

@Entity
public class Account {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "account_name")
    public String account_name;

    @ColumnInfo(name = "amount")
    public String amount;

    @ColumnInfo(name = "iban")
    public String iban;

    @ColumnInfo(name = "currency")
    public String currency;

    public Account(int id, String account_name, String amount, String iban, String currency) {
        this.id = id;
        this.account_name = account_name;
        this.amount = amount;
        this.iban = iban;
        this.currency = currency;
    }

    public String toString(){
        return "id: " + id + "\nname: "+ account_name +"\nname: "+ iban+ "\ncurrency: "+currency;
    }
}
