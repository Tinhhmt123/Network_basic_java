package org.example.LAB7;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.StringTokenizer;

public class Student {

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

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getbYear() {
        return bYear;
    }

    public void setbYear(int bYear) {
        this.bYear = bYear;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return this.id + "\t" + this.name + "\t" + this.bYear + "\t" + this.grade;
    }

    public void writeNameRAF(RandomAccessFile raf, int nameLenght) throws IOException {

        char c;
        for (int i = 0; i < nameLenght; i++) {
            c = i<this.name.length() ? this.name.charAt(i) : 0;
            raf.writeChar(c);
        }
    }

    public void writeRafs(RandomAccessFile raf, int nameLenght) throws IOException {
        raf.writeInt(this.id);
        this.writeNameRAF(raf, nameLenght);
        raf.writeInt(this.bYear);
        raf.writeDouble(this.grade);
    }

    public void readRAFs(RandomAccessFile raf, int nameLenght) throws IOException {
        this.setId(raf.readInt());
        this.setName(this.readNameRAF(raf, nameLenght));
        this.setbYear(raf.readInt());
        this.setGrade(raf.readDouble());
    }

    private String readNameRAF(RandomAccessFile raf, int nameLenght) throws IOException {
        String name = "";
        char c;
        for (int i = 0; i < nameLenght; i++) {
            c = raf.readChar();
            if (c != 0) name += c;
        }
        return name;
    }

    public void fromLine(String line, String del) {
        StringTokenizer tokens = new StringTokenizer(line, del);
        this.setId(Integer.parseInt(tokens.nextToken()));
        this.setName(tokens.nextToken());
        this.setbYear(Integer.parseInt(tokens.nextToken()));
    }
}
