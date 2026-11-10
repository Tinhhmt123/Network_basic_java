package org.example.DETHI_20242025;

import org.example.DETHI_20242025.model.Report;
import org.example.DETHI_20242025.model.User;
import org.example.DETHI_20242025.singleton.DBContext;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.time.LocalDate;

public class UserServiceIml extends UnicastRemoteObject implements IUserService {

    DBContext dbContext;
    public UserServiceIml() throws RemoteException {
        dbContext = DBContext.getInstance();
    }

    @Override
    public User login(String username, String password) throws RemoteException {

        if (username == null || password == null) { return null; }
        User user = null;
        try (Connection conn = dbContext.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select * from [User] where username=? and password=?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNumberAccount(rs.getString("numberAccount"));
                user.setBalance(rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException: " + e.getMessage());
            return null;
        }
        return user;
    }

    @Override
    public boolean withdraw(String username, double values) throws RemoteException {
        if (username == null || values <= 0) { return false; }
        try (Connection conn = dbContext.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE [User] SET balance = balance - ? WHERE username = ? AND balance >= ?");
            stmt.setDouble(1, values);
            stmt.setString(2, username);
            stmt.setDouble(3, values);
            int rowEffects = stmt.executeUpdate();
            User user = new User();
            boolean isPrintreport = false;
            if (rowEffects > 0) {
                PreparedStatement stmt2 = conn.prepareStatement("select * from [User] where username = ?");
                stmt2.setString(1, username);
                ResultSet rs = stmt2.executeQuery();
                while (rs.next()) {
                    user.setUsername(rs.getString("username"));
                    user.setBalance(rs.getDouble("balance"));
                    user.setNumberAccount(rs.getString("numberAccount"));
                    user.setPassword(rs.getString("password"));
                }
                //Print report
                Report report = new Report();
                report.setType("withdraw");
                report.setDate(LocalDate.now());
                report.setValues(values);
                report.setNumberAccount(user.getNumberAccount());
                PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO Report ([numberAccount], [type], [dateCurrent], [values]) VALUES (?, ?, ?, ?)");
                stmt3.setString(1, user.getNumberAccount());
                stmt3.setString(2, report.getType());
                stmt3.setDate(3, Date.valueOf(report.getDate()));
                stmt3.setDouble(4, report.getValues());
                int rowEffects2 = stmt3.executeUpdate();
                isPrintreport = rowEffects2 > 0;
            }
            return (rowEffects > 0 && isPrintreport);
        } catch (Exception e) {
            System.out.println("Error in withdraw: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deposit(String username, double values) throws RemoteException {
        if (username == null || values <= 0) { return false; }
        try (Connection conn = dbContext.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE [User] SET balance = balance + ? WHERE username = ?");
            stmt.setDouble(1, values);
            stmt.setString(2, username);
            int rowEffects = stmt.executeUpdate();
            User user = new User();
            boolean isPrintreport = false;
            if (rowEffects > 0) {
                PreparedStatement stmt2 = conn.prepareStatement("select * from [User] where username = ?");
                stmt2.setString(1, username);
                ResultSet rs = stmt2.executeQuery();
                while (rs.next()) {
                    user.setUsername(rs.getString("username"));
                    user.setBalance(rs.getDouble("balance"));
                    user.setNumberAccount(rs.getString("numberAccount"));
                    user.setPassword(rs.getString("password"));
                }
                //Print report
                Report report = new Report();
                report.setType("deposit");
                report.setDate(LocalDate.now());
                report.setValues(values);
                report.setNumberAccount(user.getNumberAccount());
                PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO Report ([numberAccount], [type], [dateCurrent], [values]) VALUES (?, ?, ?, ?)");
                stmt3.setString(1, user.getNumberAccount());
                stmt3.setString(2, report.getType());
                stmt3.setDate(3, Date.valueOf(report.getDate()));
                stmt3.setDouble(4, report.getValues());
                int rowEffects2 = stmt3.executeUpdate();
                isPrintreport = rowEffects2 > 0;
            }
            return (rowEffects > 0 && isPrintreport);
        } catch (Exception e) {
            System.out.println("Error in deposit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double getBlance(String username) throws RemoteException {
        double result = 0;
        try (Connection conn = dbContext.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("select balance from [User] where username=?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getDouble("balance");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
