package org.example.LAB5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class My_Server {
    ServerSocket serverSocket = new ServerSocket(7);
    Socket socket = serverSocket.accept();


    public My_Server() throws IOException {
    }

    void run() throws IOException {
        BufferedReader netin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter netout = new PrintWriter(socket.getOutputStream(), true);

        netout.println("Hello World");
        String line;

        while (true) {
            line = netin.readLine();
            if (line.equalsIgnoreCase("QUIT")) break;
            netout.println("Echo: "+line);
        }

        socket.close();
    }

    static void main() throws IOException {
        My_Server m1 = new My_Server();
        m1.run();
    }
}
