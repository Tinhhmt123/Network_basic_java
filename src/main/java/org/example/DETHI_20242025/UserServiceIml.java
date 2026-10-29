package org.example.DETHI_20242025;

import org.example.DETHI_20242025.model.User;
import org.example.DETHI_20242025.singleton.DBContext;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UserServiceIml extends UnicastRemoteObject implements IUserService{

    DBContext dbContext;
    protected UserServiceIml() throws RemoteException {
        dbContext = DBContext.getInstance();
    }

    @Override
    public User login(String username, String password) throws RemoteException {
        return null;
    }

    @Override
    public boolean withdraw(String username, double values) throws RemoteException {
        return false;
    }

    @Override
    public boolean deposit(String username, double values) throws RemoteException {
        return false;
    }

    @Override
    public double getBlance(String username) throws RemoteException {
        return 0;
    }
}
