package org.example.LAB6;

import javax.swing.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class DBStudentRAF {

    private static byte[] getFixLenghtBytes(String str, int fixedLenght) {
        byte[] result = new byte[fixedLenght];

        if (str != null) {
            byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
            int copy = Math.min(strBytes.length, fixedLenght);
            System.arraycopy(strBytes, 0, result, 0, copy);
        }
        return result;
    }

    static void addStudent(String fileDB, String idStudent, String name, double points, boolean status) {
        try (RandomAccessFile raf = new RandomAccessFile(fileDB, "rw")) {
            raf.seek(raf.length());

            raf.write(getFixLenghtBytes(idStudent, 20));

            raf.write(getFixLenghtBytes(name, 60));

            raf.writeDouble(points);

            raf.writeBoolean(status);

            raf.write(new byte[11]);

            System.out.println("Da luu sinh vien thanh cong!");

        } catch (IOException e) {
            System.out.println("Loi khong nhap duoc thong tin! Exit chuong trinh " + e.getMessage());
        }
    }

    static void main(String[] args) {
        String fileDB = "D:\\My_Study_of_TINH\\LTM\\Test\\student.dat";

        addStudent(fileDB, "001", "Nguyen Trong Tinh", 8.5, true);

    }
}
