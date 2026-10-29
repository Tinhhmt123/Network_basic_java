package org.example.LAB9;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread{

    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        try {
            this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException ex) {

        }
    }

    @Override
    public void run() {
        String syntax, destFile;
        OutputStream out;
        long lenghtFile;
        while (true) {
            try {
                syntax = dis.readUTF();
                if (syntax.equalsIgnoreCase("QUIT")) {
                    break;
                }
                if (!syntax.equalsIgnoreCase("UPLOAD")) {
                    sendRespone(dos, "Comand Invaild!");
                    continue;
                }

                destFile = dis.readUTF();

                try {
                    out = new BufferedOutputStream(new FileOutputStream(destFile));
                    sendRespone(dos, "OK");
                }catch (IOException ex){
                    sendRespone(dos, "File not found!" + ex.getMessage());
                    continue;
                }

                try(OutputStream os = out) {
                        lenghtFile = dis.readLong();
                        copyFile(os, dis, lenghtFile);
                        sendRespone(dos, "OK");
                } catch (IOException ex) {
                    sendRespone(dos, "Not successfull: "+ ex.getMessage());
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

        }

        try {
            socket.close();
            dis.close();
            dos.close();
        } catch (IOException e) {
            System.err.println("Error closing socket: "+e.getMessage());
        }

    }

    private void sendRespone(DataOutputStream dos, String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean copyFile(OutputStream os, InputStream raf, long pSize) throws IOException {
        byte[] buff = new byte[102400];
        int readByte;
        long remain = pSize;
        int readMustByte;
        while (remain > 0) {
            readMustByte = (int) (remain > buff.length ? buff.length : remain);
            readByte = raf.read(buff, 0 ,readMustByte);
            if (readByte == -1) return false;
            os.write(buff, 0, readByte);
            remain -= readByte;
        }
        return true;
    }

}
