package org.example.LAB8_clientRMI;

import org.example.LAB8_serverRMI.IProduct;
import org.example.LAB8_serverRMI.Product;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientRMI {
    static void main() throws RemoteException, NotBoundException {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            IProduct stubs2 = (IProduct) registry.lookup("product2");
            IProduct stubs3 = (IProduct) registry.lookup("product3");
            Product product2 = stubs2.getProduct();
            Product product3 = stubs3.getProduct();

            IProduct stub4 = (IProduct) registry.lookup("products");

            List<Product> products = stub4.getProducts();

            if (products != null) {
                for (Product p : products) {
                    System.out.println(p);
                }
            }

//           if(product2 != null){
//               System.out.println(product2);
//           }
//
//           if(product3 != null){
//               System.out.println(product3);
//           }
//
//            System.out.println(stubs2.buyProduct(10));
//            System.out.println(stubs3.buyProduct(10));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
