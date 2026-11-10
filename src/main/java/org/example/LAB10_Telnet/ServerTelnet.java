package org.example.LAB10_Telnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTelnet {

    static void main() throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("Server is starting...");
        Socket socket = serverSocket.accept();
        if (socket.isConnected()) {
            System.out.println("Accepted connection from " + socket.getInetAddress());

            ServerHandler theard = new ServerHandler(socket);
            theard.start();
        }


    }
}
