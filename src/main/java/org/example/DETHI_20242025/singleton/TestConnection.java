package org.example.DETHI_20242025.singleton;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    DBContext dbContext;
    public TestConnection() throws RemoteException {
        dbContext = DBContext.getInstance();
    }

    void run() {
        try {
            Connection conn = dbContext.getConnection();
            if (conn != null) {
                System.out.println(" " + conn.getMetaData().getURL());
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    static void main() throws RemoteException {
        TestConnection testConnection = new TestConnection();

        testConnection.run();
    }
}
