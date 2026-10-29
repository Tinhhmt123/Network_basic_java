package org.example.DETHI_20242025;

import org.example.DETHI_20242025.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUserService extends Remote {

    public User login(String username, String password) throws RemoteException;

    public boolean withdraw(String username, double values) throws RemoteException;

    public boolean deposit(String username, double values) throws RemoteException;

    public double getBlance(String username) throws RemoteException;
}
