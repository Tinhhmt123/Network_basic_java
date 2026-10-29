package org.example.LAB5;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HandleFileOnServer {
    int port;
    public HandleFileOnServer(int port) {
        this.port = port;
    }

    void run() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Dang cho ket noi server!");

        Socket socket = serverSocket.accept();
        System.out.println("Ket noi thanh cong: "+socket.getInetAddress());

        String destFile = "D:\\My_Study_of_TINH\\LTM\\Test\\";
        DataInputStream netin = new DataInputStream(socket.getInputStream());
        FileOutputStream netout = new FileOutputStream(destFile+"TaiLieu_DaNhan.docx");

        String nameFile = netin.readUTF();
        long fileSize = netin.readLong();

        byte[] buff = new byte[102400];
        int byteRead;
        long remain = fileSize;
        int byteMustRead;

        while (remain > 0) {
            byteMustRead =(int) (remain > buff.length ? buff.length : remain);
            byteRead = netin.read(buff, 0, byteMustRead);
            if (byteRead == -1) break;
            netout.write(buff, 0, byteRead);
            remain -= byteRead;
        }

        System.out.println("Da luu tai thu muc ");
    }
}
