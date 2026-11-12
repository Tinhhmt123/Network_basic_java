package org.example.ServerTCP.Telnet.Authenticator;

import org.example.ServerTCP.Telnet.Authenticator.model.Student;
import org.example.ServerTCP.Telnet.Authenticator.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {

    private List<User> users;
    private List<Student> students;

    public Dao() {
        users = new ArrayList<>();
        User u1 = new User("admin", "123");
        User u2 = new User("user", "222");
        users.addAll(Arrays.asList(u1, u2));

        students = new ArrayList<>();
        Student s1 =new Student(1, "Nguyen Trong Tinh", 2006, 10);
        Student s2 = new Student(2, "Nguyen Trong Binh", 2008, 9);
        Student s3 = new Student(3, "Tran Trong Tinh", 2007, 10);
        students.addAll(Arrays.asList(s1, s2, s3));
    }

    public boolean existUser(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (!user.getName().equals(username) || !user.getPassword().equals(password)) continue;
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public User getUser(String username) {
        for (User user : users) {
            if (user.getName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<Student> getStudentsById(int id) {
        List<Student> result = new ArrayList<>();
        for (Student student : this.students) {
            if (student.getId() == id) {
                result.add(student);
            }
        }
        return result;
    }

    public List<Student> getStudentsByOnePartName(String partName) {
        List<Student> result = new ArrayList<>();
        for (Student student : this.students) {
            if (student.getName().endsWith(partName)) {
                result.add(student);
            }
        }
        return result;
    }
}
