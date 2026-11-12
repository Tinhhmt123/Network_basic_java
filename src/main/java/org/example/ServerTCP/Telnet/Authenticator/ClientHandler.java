package org.example.ServerTCP.Telnet.Authenticator;

import org.example.ServerTCP.Telnet.Authenticator.model.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

public class ClientHandler extends Thread{

    Socket socket;
    BufferedReader in;
    PrintWriter out;
    Dao dao;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.dao = new Dao();
    }

    @Override
    public void run() {
        sendRespone(out, "Welcome to application");
        String line, syntax = "", param, username = null, password;
        boolean isLoggedIn = false;
        while(!isLoggedIn){
            sendRespone(out, "> ");
            try {
                line = in.readLine();
            } catch (IOException e) {
                sendRespone(out, "Error: " + e.getMessage());
                continue;
            }
            if(line.equalsIgnoreCase("quit")){
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            if (line.equals("")) continue;
            StringTokenizer tokens = new  StringTokenizer(line);
            if (tokens.hasMoreTokens()) {
                syntax = tokens.nextToken();
            }
            param = line.substring(syntax.length()).trim();

            switch(syntax){
                case "USER" : {
                    if (param == null || param.isBlank()) {
                        sendRespone(out, "Username isn't blank!");
                        continue;
                    }
                    boolean exist = dao.existUser(param);
                    if(!exist){
                        username = null;
                        sendRespone(out, "User does not exist");
                        continue;
                    }
                    username = param;
                    break;
                }

                case "PASSWORD" : {
                    if (username == null){
                        sendRespone(out, "Username is empty, Please enter username first! ...");
                        continue;
                    }

                    isLoggedIn = dao.login(username, param);
                    if(!isLoggedIn){
                        sendRespone(out, "Password is incorrect! ...");
                        username = null;
                        param = null;
                        isLoggedIn = false;
                        continue;
                    }
                    sendRespone(out, "Login successful!");
                    isLoggedIn = true;
                    break;
                }
                default: {
                    sendRespone(out, "Invalid syntax: " + syntax);
                    continue;
                }
            }
        }
        int idStudent = -1;
        while (isLoggedIn) {
            sendRespone(out, "> ");
            try {
                line = in.readLine();
            } catch (IOException e) {
                sendRespone(out, "Error: " + e.getMessage());
                continue;
            }
            if(line.equalsIgnoreCase("quit")){
                try {
                    in.close();
                    out.close();
                    socket.close();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            if (line.equals("")) continue;
            StringTokenizer tokens = new StringTokenizer(line);
            if (tokens.hasMoreTokens()) {
                syntax = tokens.nextToken();
            }
            param = line.substring(syntax.length()).trim();
            switch(syntax){
                case "FINDID" : {
                    if (param == null || param.isBlank()) {
                        sendRespone(out, "Please enter parameter ...");
                        break;
                    }
                    try {
                         idStudent = Integer.parseInt(param);
                    } catch (NumberFormatException e) {
                        sendRespone(out, "Invalid ID! Please enter a valid ID!");
                        continue;
                    }
                    printListStudent(dao.getStudentsById(idStudent));
                    break;
                }
                case "FINDNAME" : {
                    if (param == null || param.isBlank()) {
                        sendRespone(out, "Please enter parameter ...");
                        break;
                    }
                    printListStudent(dao.getStudentsByOnePartName(param));
                    break;
                }

                default: {
                    sendRespone(out, "Invalid syntax: " + syntax);
                    continue;
                }
            }
        }
    }

    private void printListStudent(List<Student> students) {
        if(students == null || students.isEmpty()){
            sendRespone(out, "List is empty! ...");
            return;
        }
        for (Student student : students) {
            sendRespone(out, student.toString() + "\n");
        }
    }

    private void sendRespone(PrintWriter out, String message) {
        out.println(message);
        out.flush();
    }
}
