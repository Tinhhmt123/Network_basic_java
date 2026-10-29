package org.example.LAB8_serverRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IProduct extends Remote {
    public Product getProduct() throws RemoteException;
    public boolean buyProduct(int quanity) throws RemoteException;
    public List<Product> getProducts() throws RemoteException;
}
