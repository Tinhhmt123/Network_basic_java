package org.example.ServerTCP.Telnet.Authenticator;

import org.example.LAB9.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {

    static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("Server is running on port 2000 ...");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from " + socket.getInetAddress().getHostName());

            ClientHandler thread = new ClientHandler(socket);
            thread.start();
        }
    }
}
