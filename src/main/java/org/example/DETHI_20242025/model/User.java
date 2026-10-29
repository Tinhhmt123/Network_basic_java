package org.example.DETHI_20242025.model;

public class User {
    String username, password, numberAccount;
    double balance;

    public User(String username, String password, String numberAccount, double balance) {
        this.username = username;
        this.password = password;
        this.numberAccount = numberAccount;
        this.balance = balance;
    }

    public User() {}

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

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public double getBalance() {
        if (balance < 0) {
            return 0;
        }
        return balance;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            return;
        }
        this.balance = balance;
    }

    @Override
    public String toString() {
        return this.username + "\t" + this.password + "\t" + this.numberAccount + "\t" + this.balance;
    }
}
