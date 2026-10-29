package org.example.DETHI_20242025;

import org.example.DETHI_20242025.model.Report;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IBankService extends Remote {

    public String getBanner() throws RemoteException;

    public List<Report> getReports(String numberAccountUser) throws RemoteException;
}
