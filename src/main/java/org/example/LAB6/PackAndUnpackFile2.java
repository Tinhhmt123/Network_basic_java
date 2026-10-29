package org.example.LAB6;

import java.io.*;

public class PackAndUnpackFile2 {

    public static void pack(String folder, String packedFile) throws IOException {
        File file = new File(folder);
        if (!file.exists() || !file.isDirectory()) return;
        File pFile = new File(packedFile);
        File conicalFile = pFile.getCanonicalFile();
        File[] files = file.listFiles(f -> f.isFile() && !f.equals(conicalFile));

        if (files == null) return;
        try(RandomAccessFile raf = new RandomAccessFile(pFile, "rw")) {
            int numFile = files.length;
            raf.writeInt(numFile);
            long[] posMarked = new long[numFile];

            for (int i = 0; i<numFile; i++) {
                File f = files[i];
                //Marked pos cua tung file metadata
                posMarked[i] = raf.getFilePointer();

                raf.writeLong(0L);
                raf.writeLong(f.length());
                raf.writeUTF(f.getName());
            }

            long startPos;
            byte[] buff = new byte[102400];
            int readByte;
            for (int i = 0; i<numFile; i++) {
                startPos = raf.getFilePointer();

                raf.seek(posMarked[i]);
                raf.writeLong(startPos);
                raf.seek(startPos);

                try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(files[i]))){
                    while ((readByte = is.read(buff)) != -1) {
                        raf.write(buff, 0, readByte);
                    }
                }

            }
        }

    }

    static void unPack(String packedFile, String extractFile, String dest) throws IOException {
        File pFile = new File(packedFile);
        if (!pFile.exists() || !pFile.isFile()) return;
        try(RandomAccessFile raf = new RandomAccessFile(pFile, "rw")) {
            int numFile = raf.readInt();

            for (int i = 0; i<numFile; i++) {
                long pos = raf.readLong();
                long fileSize = raf.readLong();
                String nameFile = raf.readUTF();

                if (nameFile.equalsIgnoreCase(extractFile)) {
                    raf.seek(pos);
                    try(BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(dest))) {
                        copyFile(os, raf, fileSize);
                    }
                    return;
                }


            }
        }
    }

    private static boolean copyFile(OutputStream os, RandomAccessFile raf, long pSize) throws IOException {
        byte[] buff = new byte[102400];
        int readByte;
        long remain = pSize;
        int readMustByte;
        while (remain > 0) {
            readMustByte = (int) (remain > buff.length ? buff.length : remain);
            readByte = raf.read(buff, 0 ,readMustByte);
            if (readByte == -1) return false;
            os.write(buff, 0, readByte);
            remain -= readByte;
        }
        return true;
    }

    static void main(String[] args) throws IOException {
        String folder = "D:\\My_Study_of_TINH\\LTM\\Test";
        String packedFile = "D:\\My_Study_of_TINH\\LTM\\Test\\test2\\ab.pack";

        String extractFile = "text.txt";
        String dest = "D:\\My_Study_of_TINH\\LTM\\Test\\test2\\b_copy.txt";

        //pack(folder, packedFile);
        unPack(packedFile, extractFile, dest);
    }
}
