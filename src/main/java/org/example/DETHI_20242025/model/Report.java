package org.example.DETHI_20242025.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Report implements Serializable {

    String numberAccount, type;
    LocalDate date;
    double values;

    public Report(String numberAccount, String type, double values) {
        this.numberAccount = numberAccount;
        this.type = type;
        this.date = LocalDate.now();
        this.values = values;
    }

    public Report() {
        this.date = LocalDate.now();
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getValues() {
        return values;
    }

    public void setValues(double values) {
        this.values = values;
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.numberAccount+"\t"+this.type+"\t"+this.date.format(dtf)+"\t"+this.values;
    }
}
