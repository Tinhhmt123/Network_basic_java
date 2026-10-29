package org.example.LAB6;

import java.io.*;

public class PackAndUnpackFile {

    static void pack(String folder, String packedFile) throws IOException {
        File file = new File(folder);
        //Check input
        if (!file.exists() || !file.isDirectory()) return;
        File pFile = new File(packedFile);
        File connicalFile = pFile.getCanonicalFile();
        //Filter file : isFile and unsame pFile
        File[] files = file.listFiles(f -> f.isFile() && !f.equals(connicalFile));
        int numFile = files.length;
        try(RandomAccessFile raf = new RandomAccessFile(pFile, "rw")) {
            raf.setLength(0);
            //Write follow structure
            //Step 1. Write num file
            raf.writeInt(numFile);
            //Step 2. Write pos files, placeholder
            for (int i = 0; i< numFile; i++) {
                raf.writeLong(0L);
            }
            byte[] buff = new byte[102400];
            int byteRead;
            long pos, startPos;
            //Step 3. Write metadata and data file
            for (int i = 0; i<numFile; i++){
                //Get pos file i
                startPos = raf.getFilePointer();
                //Create var to seek(pos)
                pos = (long) 4+8*i;
                //Attach file
                 File f = files[i];
                 //Write metadata: Name file and Size
                 raf.writeUTF(f.getName());
                 raf.writeLong(f.length());
                 //Write data file
                 try(BufferedInputStream fis = new BufferedInputStream(new FileInputStream(f))) {
                     while ((byteRead = fis.read(buff)) != -1) {
                         raf.write(buff, 0, byteRead);
                     }
                 }
                 //After write metadata and data file, create var to save endPos raf
                 long endPos = raf.getFilePointer();
                 //Move (seek) to pos to overwrite long
                 raf.seek(pos);
                 raf.writeLong(startPos);
                 raf.seek(endPos);
            }
        }
    }

    static void unPack(String packedFile, String extractFile, String destFile) throws IOException {
        File pFile = new File(packedFile);
        if (!pFile.exists() || !pFile.isFile()) return;
        try(RandomAccessFile raf = new RandomAccessFile(pFile, "rw")) {
            int numFile = raf.readInt();
            long[] pos = new long[numFile];
            for (int i = 0; i<numFile; i++) {
               pos[i] = raf.readLong();
            }
            String nameFile;
            long lenghtFile;

            for (int i = 0; i<numFile; i++) {
                raf.seek(pos[i]);
                nameFile = raf.readUTF();
                if (nameFile.equalsIgnoreCase(extractFile)) {
                    lenghtFile = raf.readLong();
                    try (BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(destFile))){
                        copyFile(os, raf, lenghtFile);
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
        String packedFile = "D:\\My_Study_of_TINH\\LTM\\Test\\test2\\abc.pack";

        String extractFile = "a.docx";
        String destFile = "D:\\My_Study_of_TINH\\LTM\\Test\\test2\\a_copy.docx";
        //pack(folder, packedFile);
        unPack(packedFile, extractFile, destFile);
    }
}
