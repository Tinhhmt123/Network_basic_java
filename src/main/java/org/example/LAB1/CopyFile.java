package org.example.LAB1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFile {


    public static void main(String[] args) throws IOException {

        String sf = "src/main/java/org/example/LAB1/RobloxPlayerInstaller-X7KRF28YVP.exe";
        String df = "src/main/java/org/example/LAB1/RobloxLOO.exe";

        copyFile(sf, df);
    }

    private static void copyFile(String sf, String df) throws IOException {
        //Mo file nguon
        FileInputStream sfo = new FileInputStream(sf);
        //Tao file dich
        FileOutputStream dfo = new FileOutputStream(df);
        //Ghi byte tuan tu hoac ghi theo khoi byte (toi uu hieu nang)
        byte[] data = new byte [4069] ;
        int count;
        while ((count = sfo.read(data)) != -1) {
            dfo.write(data, 0, count);
        }
            //Dong file nguon
        sfo.close();
        //Dong file dich
        dfo.close();


    }
}
