package org.example.LAB8_serverRMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

public class ServerRMI {
    
    ServerRMI() {}
    
    void run() throws RemoteException {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);

            Product product4 = new Product("product 4", 10,20, "abc");
            Product product5 = new Product("product 5", 10,20, "abc");
            Product product6 = new Product("product 6", 10,20, "abc");
            Product product7 = new Product("product 7", 10,20, "abc");

            List<Product> products = Arrays.asList(product4, product5, product6, product7);

            IProduct p1 = new ProductIml(new Product("product 1", 8.9, 10, "dce"));
            IProduct p2 = new ProductIml(new Product("product 2", 8.9, 10, "bcd"));
            IProduct p3 = new ProductIml(new Product("product 3", 8.9, 10, "abc"));

            IProduct productsIML = new ProductIml(products);

            registry.rebind("product1", p1);
            registry.rebind("product2", p2);
            registry.rebind("product3", p3);

            registry.rebind("products", productsIML);

            System.out.println("Server RMI Server Started ...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void main(String[] args) throws RemoteException {
        ServerRMI server = new ServerRMI();
        ServerRMI server2 = server;
        server2.run();
    }
}
