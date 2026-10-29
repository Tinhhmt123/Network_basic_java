package org.example.LAB8_serverRMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ProductIml extends UnicastRemoteObject implements IProduct{

    Product product;
    List<Product> products;
    protected ProductIml(Product product) throws RemoteException {
        this.product = product;
    }

    @Override
    public Product getProduct() throws RemoteException {
        return this.product;
    }

    public ProductIml(List<Product> products) throws RemoteException {
        this.products = products;
    }

    @Override
    public synchronized boolean buyProduct(int quanity) throws RemoteException {
        if (quanity <= 0) { return false; }
        if (quanity > product.getQuantity()) { return false; }
        product.setQuantity(product.getQuantity() - quanity);
        return true;
    }

    @Override
    public List<Product> getProducts() throws RemoteException {
        return this.products;
    }
}



