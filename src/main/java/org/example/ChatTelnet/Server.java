package org.example.ChatTelnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    static void main() throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        Socket socket = serverSocket.accept();
        System.out.println("Accepted connection from " + socket.getInetAddress());


    }
}
