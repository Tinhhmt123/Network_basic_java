package org.example.LAB4;

import java.io.*;

public class SplitAndJion {

    public static void split(String source, long pSize) throws IOException {
        if (pSize < 0) {
            System.out.println("So byte can chia phai lon khong");
            return;
        }

        File filleSource = new File(source);
        if (!filleSource.exists() || !filleSource.isFile()) {
            System.out.println("File khong ton tai hoac file khong hop le");
            return;
        }

        InputStream is = new BufferedInputStream(new FileInputStream(filleSource));
        OutputStream os;
        String destFile;
        int count = 1;

        while (true) {
            destFile = source + createEXT(count);
            os = new BufferedOutputStream(new FileOutputStream(destFile));
            boolean hasMore = copyFile(os, is, pSize);
            os.close();
            if (!hasMore) break;
            count++;
        }
        is.close();

    }

    public static void jion(String source, String destFile) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(destFile));
        InputStream is;
        int count = 1;
        while (true) {
            String partFileName = source + createEXT(count);
            File partFile = new File(partFileName);
            if (!partFile.exists()) break;
            is = new BufferedInputStream(new FileInputStream(partFile));
            copyFile(os, is, partFile.length());
            is.close();
            count++;
        }
        os.close();
    }

    private static boolean copyFile(OutputStream os, InputStream is, long pSize) throws IOException {
        byte[] buff = new byte[102400];
        int readByte;
        long remain = pSize;
        int readMustByte;
        while (remain > 0) {
            readMustByte = (int) (remain > buff.length ? buff.length : remain);
            readByte = is.read(buff, 0 ,readMustByte);
            if (readByte == -1) return false;
            os.write(buff, 0, readByte);
            remain -= readByte;
        }
        return true;
    }

    private static String createEXT(int count) {
        if (count < 10) {
            return ".00"+count;
        }
        if (count < 100) {
            return ".0"+count;
        }
        return "."+count;
    }

    static void main() throws IOException {
        String path = "D:\\My_Study_of_TINH\\LTM\\Test\\tetsplit_jionfile\\SaoTaNguocLoi-DinhDung-7052177.mp3";
        long pSize = 1 * 1024 * 1024;
       // split(path, pSize);
        String destFileName = "D:\\My_Study_of_TINH\\LTM\\Test\\tetsplit_jionfile\\SaoTaNguocLoi-DinhDung-7052177_copy.mp3";
        jion(path, destFileName);
    }
}
