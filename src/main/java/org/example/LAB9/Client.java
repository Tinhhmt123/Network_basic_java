package org.example.LAB9;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class Client {

    DataInputStream dis;
    DataOutputStream dos;
    Socket socket;
    BufferedReader br;
    public Client(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    void run() throws IOException {
        String line, syntax, sourceFile, destFile;
        BufferedInputStream is;
        while (true) {
            System.out.print("> ");
            line = br.readLine();
            if (line.trim().isEmpty()) continue;
            if (line.equalsIgnoreCase("QUIT")) {
                sendRequest(dos, "QUIT");
                break;
            }
            StringTokenizer tokens = new StringTokenizer(line.trim(), " ");
            int countTokens = tokens.countTokens();
            if (countTokens != 3) {
                System.out.println("Vui long nhap 3 tham so!");
                continue;
            }
            syntax = tokens.nextToken();
            sourceFile = tokens.nextToken();
            destFile = tokens.nextToken();

            if (!syntax.equalsIgnoreCase("UPLOAD")) {
                System.out.println("Khong thuoc lenh cua chuong trinh");
                continue;
            }

            sendRequest(dos, syntax);
            sendRequest(dos, destFile);
            if (!dis.readUTF().equalsIgnoreCase("OK")) {
                System.out.println("Khong doc duoc file dich!");
                continue;
            }
            File file = new File(sourceFile);
            if (!file.exists() || !file.isFile()) {
                System.out.println("File khong hop le hoac khong ton tai");
                continue;
            }
            dos.writeLong(file.length());
            dos.flush();

           try {
               is = new BufferedInputStream(new FileInputStream(file));
               byte[]  buffer = new byte[102400];
               int bytesRead;
               while ((bytesRead = is.read(buffer)) != -1) {
                   dos.write(buffer, 0, bytesRead);
               }
               dos.flush();
           } catch (IOException e) {
               System.out.println("Loi thuc thi file: "+e.getMessage());
           }

           if (!dis.readUTF().equalsIgnoreCase("OK")) {
               System.out.println("Loi UPLOAD");
               continue;
           }
        }

        socket.close();
        dis.close();
        dos.close();
        br.close();
    }
    static void main() throws IOException {
        Socket socket = new Socket("localhost", 2000);
        Client client = new Client(socket);
        client.run();
    }

    private void sendRequest(DataOutputStream dos, String message) {
        try {
            dos.writeUTF(message);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
