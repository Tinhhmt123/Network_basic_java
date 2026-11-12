package org.example.ServerTCP.Telnet.Authenticator.model;

import java.io.Serializable;

public class Student implements Serializable {

    private int id;
    private String name;
    private int bYear;
    private double grade;

    public Student(int id, String name, int bYear, double grade) {
        this.id = id;
        this.name = name;
        this.bYear = bYear;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        if (this.name == null) {
            return "Unknown";
        }
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            this.name = "Unknown";
            return;
        }
        this.name = name;
    }

    public int getbYear() {
        return bYear;
    }

    public void setbYear(int bYear) {
        if (bYear < 0) {
            this.bYear = 0;
            return;
        }
        this.bYear = bYear;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        if (grade < 0 || grade > 10) {
            this.grade = 0;
            return;
        }
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bYear=" + bYear +
                ", grade=" + grade +
                '}';
    }
}
