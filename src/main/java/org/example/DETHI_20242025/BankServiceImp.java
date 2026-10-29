package org.example.DETHI_20242025;

import org.example.DETHI_20242025.model.Report;
import org.example.DETHI_20242025.singleton.DBContext;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankServiceImp extends UnicastRemoteObject implements IBankService {

    DBContext dbContext;

    public BankServiceImp() throws RemoteException {
        dbContext = DBContext.getInstance();
    }

    @Override
    public String getBanner() throws RemoteException {
        return "Welcome to NLU e-Bank. " + "\n Process login: ";
    }

    @Override
    public List<Report> getReports(String numberAccountUser) throws RemoteException {
        List<Report> reports = new ArrayList<Report>();
        try(Connection conn = dbContext.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Report WHERE numberAccount = ?");
            stmt.setString(1, numberAccountUser);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Report report = new Report();
                report.setNumberAccount(rs.getString(2));
                report.setType(rs.getString(3));
                report.setDate(rs.getDate(4).toLocalDate());
                report.setValues(rs.getDouble(5));
                reports.add(report);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return reports;
    }
}
