package org.example.DETHI_20242025.serverRMI;

import org.example.DETHI_20242025.BankServiceImp;
import org.example.DETHI_20242025.IBankService;
import org.example.DETHI_20242025.IUserService;
import org.example.DETHI_20242025.UserServiceIml;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);
        IBankService iBankService = new BankServiceImp();
        registry.rebind("BankService", iBankService);
        IUserService iUserService = new UserServiceIml();
        registry.rebind("UserService", iUserService);


    }
}
