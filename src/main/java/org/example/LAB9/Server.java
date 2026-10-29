package org.example.LAB9;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(2000)) {
            while (true) {
                Socket socket = server.accept();
                System.out.println("Connected with ip: "+socket.getInetAddress().getHostName());

                ClientHandler handler = new ClientHandler(socket);
                handler.start();

            }
        }

    }
}
