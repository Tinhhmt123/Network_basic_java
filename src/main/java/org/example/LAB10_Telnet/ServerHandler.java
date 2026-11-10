package org.example.LAB10_Telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerHandler extends Thread {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    double op1, op2;
    char operator;
    public ServerHandler(Socket socket) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.socket = socket;
    }

    @Override
    public void run() {
        sendRespone(out, "Hello World!");
        String op1Value, op2Value;
        double result = 0;
        while (true) {
            sendRespone(out, "> ");
            try {
                String line = in.readLine();
                if (line.equalsIgnoreCase("quit")) {
                    sendRespone(out, "Bye!");
                    in.close();
                    out.close();
                    socket.close();
                    return;
                }
                StringTokenizer tokens = new StringTokenizer(line, "+-*/");
                if (tokens.countTokens() != 2) {
                    sendRespone(out, "Please fill two values ...");
                    continue;
                }
                try {
                    op1Value = tokens.nextToken();
                    op2Value = tokens.nextToken();
                    op1 = Double.parseDouble(op1Value);
                    op2 = Double.parseDouble(op2Value);
                } catch (NumberFormatException e) {
                    sendRespone(out, "op1 & op2 is an double!");
                    continue;
                }
                operator = line.charAt(op1Value.length());
                switch (operator) {
                    case '+': {
                        result = op1 + op2;
                        break;
                    }
                    case '-': {
                        result = op1 - op2;
                        break;
                    }
                    case '*': {
                        result = op1 * op2;
                        break;
                    }
                    case '/': {
                        if (op2 == 0) {
                            sendRespone(out, "Can't divide by zero!");
                            continue;
                        }
                        result = op1 / op2;
                        break;
                    }
                }
                sendRespone(out, op1Value +" "+ operator +" "+ op2Value +" = "+ result);

            } catch (IOException e) {
                sendRespone(out, "Error: " + e.getMessage());
            }
        }

    }

    private void sendRespone(PrintWriter out, String s) {
        out.println(s);
        out.flush();
    }
}
