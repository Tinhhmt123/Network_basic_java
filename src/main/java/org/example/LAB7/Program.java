package org.example.LAB7;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Program {

    private final static int NAME_LENGHT = 50;

    public void addStudent(String path, Student st) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(path, "rw")) {
            int numStudent = 0;
            int recSize = 4 +  NAME_LENGHT*2 + 4 + 8;
            if (raf.length() > 0) numStudent = raf.readInt();
            raf.seek(0);
            //Write header
            raf.writeInt(numStudent + 1);
            raf.writeInt(recSize);
            //Write body
            raf.seek(8 + recSize * numStudent);
            st.writeRafs(raf, NAME_LENGHT);
        }
    }

    public Student getStudentByIndex(String path, int index) throws IOException {
        File sfile = new File(path);
        if (!sfile.exists() || !sfile.isFile() || index < 0) return null;
        try (RandomAccessFile raf = new RandomAccessFile(sfile, "rw")) {
            int numStudent = raf.readInt();
            int recSize = 4 +  NAME_LENGHT*2 + 4 + 8;
            raf.seek(8+recSize * index);
            Student result = new Student();
            result.readRAFs(raf, NAME_LENGHT);
            return result;
        }
    }

    public void updateStudent(String path, Student newSt, int index) throws IOException {
        File sfile = new File(path);
        if (!sfile.exists() || !sfile.isFile() || index < 0 || newSt == null) return;
        try (RandomAccessFile raf = new RandomAccessFile(sfile, "rw")) {
            int numStudent = raf.readInt();
            int recSize = 4 +  NAME_LENGHT*2 + 4 + 8;
            raf.seek(8+recSize * index);
            newSt.writeRafs(raf, NAME_LENGHT);
            return;
        }
    }

    public Student findById(String path, int id) throws IOException {
        File sfile = new File(path);
        if (!sfile.exists() || !sfile.isFile() || id < 0) return null;

        try (RandomAccessFile raf = new RandomAccessFile(sfile, "rw")) {
            int numStudent = raf.readInt();
            int recSize = 4 +  NAME_LENGHT*2 + 4 + 8;
            for (int i = 0; i<numStudent; i++) {
                raf.seek(8+recSize * i);
                int idSt = raf.readInt();
                if (idSt == id) {
                    raf.seek(8+recSize * i);
                    Student result = new Student();
                    result.readRAFs(raf, NAME_LENGHT);
                    return result;
                }
            }
        }

        return null;
    }

    public List<Student> loadData(String fileSV, String fileDiem) throws IOException {
        Map<Integer, Double> map = new HashMap<>();
        List<Student> students = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileSV), StandardCharsets.UTF_16BE))) {
            String line;
            String del = "\t";
            Student st;
            br.read();
            while ((line = br.readLine()) != null) {
                st = new Student();
                st.fromLine(line, del);
                students.add(st);
            }
        }

        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDiem), StandardCharsets.UTF_16BE))) {
            String line;
            String del = "\t";
            br.read();
            while ((line = br.readLine()) != null) {
                double sumGrade = 0;
                int numGrade = 0;
                StringTokenizer tokens = new StringTokenizer(line, del);
                int idSt = Integer.parseInt(tokens.nextToken());
                while(tokens.hasMoreTokens()) {
                    numGrade++;
                    sumGrade += Double.parseDouble(tokens.nextToken());
                }
                double dtb = (double) sumGrade / numGrade;
                map.put(idSt, dtb);
            }
        }

        for (Student st : students) {
            st.setGrade(map.get(st.getId()));
        }

        return  students;
    }

    static void main(String[] args) throws IOException {
        Program program = new Program();
        Student s1 = new Student(111, "Nguyen Trong Tinh", 2006, 10);
        Student s2 = new Student(222, "Do Tan Dat", 2005, 8);
        Student s3 = new Student(333, "Mai Khac Hai Nguyen", 2006, 9);
        Student s4 = new Student(444, "Le Duong Thanh", 2006, 9.5);


        String path = "D:\\My_Study_of_TINH\\LTM\\Test\\student.txt";
        String fileSv = "D:\\My_Study_of_TINH\\LTM\\Test\\dssv.txt";
        String fileDiem = "D:\\My_Study_of_TINH\\LTM\\Test\\DSdiem.txt";

        //program.updateStudent(path, s4, 1);
        //program.addStudent(path, s3);
        //System.out.println(program.getStudentByIndex(path, 1));
        //System.out.println(program.findById(path, 111));

        List<Student> students = program.loadData(fileSv, fileDiem);
        for (Student s : students) {
            System.out.println(s);
        }
    }
}
