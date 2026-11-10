package org.example.DETHI_20242025.client;

import org.example.DETHI_20242025.IBankService;
import org.example.DETHI_20242025.IUserService;
import org.example.DETHI_20242025.model.Report;
import org.example.DETHI_20242025.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.StringTokenizer;

public class ClientRMI1 {

    public ClientRMI1() {}

    void run() throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        IBankService bankService = (IBankService) registry.lookup("BankService");
        IUserService userService = (IUserService) registry.lookup("UserService");
        if (userService == null) { System.out.println("UserService not found"); return; }
        if (bankService == null) { System.out.println("BankService not found"); return; }
        System.out.println(bankService.getBanner());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String username = null, password = null;
            double valueDeposit, valueWithdraw;
            boolean isLogin = false;
            User user = null;
            //While for login
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    return;
                }
                line = line.trim();
                if (line.equalsIgnoreCase("QUIT")) {
                    return;
                }
                StringTokenizer tokens = new StringTokenizer(line, " ");
                if (tokens.countTokens() < 1) {
                    System.out.println("Please, fill => one parameters");
                    continue;
                }
                String syntax = tokens.nextToken().toUpperCase();
                String values = null;
                if (tokens.hasMoreTokens()) {
                    values = tokens.nextToken();
                }
                switch (syntax) {
                    case "USERNAME": {
                        if (values == null) {
                            System.out.println("Please enter username! (Ex: USERNAME user1)");
                            continue;
                        }
                        username = values;
                        break;
                    }
                    case "PASSWORD": {
                        if (values == null) {
                            System.out.println("Please enter password! (Ex: PASSWORD 123)");
                            continue;
                        }
                        password = values;
                        break;
                    }
                    default: {
                        System.out.println("Invalid syntax! Please login first (USERNAME <user>, PASSWORD <pass>, or QUIT)");
                        continue;
                    }
                }
                if (username == null || password == null) { continue; }
                user = userService.login(username, password);
                if (user == null) {
                    System.out.println("Login failed");
                    username = null;
                    password = null;
                    isLogin = false;
                    continue;
                }
                System.out.println("Login successful: " + user);
                isLogin = true;
                break;
            }
            //Session command
            if (isLogin) {
                System.out.println("Please, enter your comand: ...");
                while (true) {
                    String line = br.readLine();
                    if (line == null) { return; }
                    line = line.trim();
                    if (line.equalsIgnoreCase("QUIT")) { return; }

                    StringTokenizer tokens = new StringTokenizer(line, " ");
                    if (tokens.countTokens() < 1) {
                        System.out.println("Please, fill => one parameters");
                        continue;
                    }
                    String syntax = tokens.nextToken().toUpperCase();
                    String values = null;
                    if (tokens.hasMoreTokens()) {
                        values = tokens.nextToken();
                    }
                    switch (syntax) {
                        case "DEPOSIT": {
                            if (values == null) {
                                System.out.println("Please enter deposit amount! (Ex: DEPOSIT 500)");
                                break;
                            }
                            try {
                                valueDeposit = Double.parseDouble(values);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid amount!");
                                break;
                            }
                            boolean isSucessful = userService.deposit(user.getUsername(), valueDeposit);
                            if (!isSucessful) { System.out.println("Deposit failed"); break; }
                            System.out.println("Deposit successful: " + userService.getBlance(user.getUsername()));
                            break;
                        }

                        case "WITHDRAW": {
                            if (values == null) {
                                System.out.println("Please enter withdraw amount! (Ex: WITHDRAW 500)");
                                break;
                            }
                            try {
                                valueWithdraw = Double.parseDouble(values);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid amount!");
                                break;
                            }
                            boolean isSucessful = userService.withdraw(user.getUsername(), valueWithdraw);
                            if (!isSucessful) { System.out.println("Withdraw failed"); break; }
                            System.out.println("Withdraw successful: " + userService.getBlance(user.getUsername()));
                            break;
                        }

                        case "BALANCE": {
                            System.out.println("Balance user: " + userService.getBlance(user.getUsername()));
                            break;
                        }
                        case "REPORT": {
                            List<Report> reports = bankService.getReports(user.getNumberAccount());
                            if (reports == null || reports.isEmpty()) {
                                System.out.println("No transactions found.");
                            } else {
                                for (Report report : reports) {
                                    System.out.println(report);
                                }
                            }
                            break;
                        }
                        default: {
                            System.out.println("Invalid command! (Use: DEPOSIT <amount>, WITHDRAW <amount>, BALANCE, REPORT, QUIT)");
                            break;
                        }
                    }
                }
            }

        }

    }
    static void main() throws NotBoundException, IOException {
        ClientRMI1 client = new ClientRMI1();
        client.run();
    }
}
