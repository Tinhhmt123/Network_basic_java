package org.example;

import java.io.File;

public class FileHandle {


    static void main(String[] args) {
        //Test cho delete
        String path = "";

        delete(path);
        findAllByExts(path, ".txt", ".pdf", ".docx");
    }

    private static void findAllByExts(String path, String...exts) {
        //Kiem tra path va extension co null hay khong
        if (path == null || exts == null) return;
        File f = new File(path);
        //Kiem tra file co ton tai hay khong
        if (f.exists()) return;
        //Kiem tra file la thu muc hay khong
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            //Kiem tra f co quyen hay khong? Khong thi files = null
            if (files != null) {
                for (File file : files) {
                    findAllByExts(path, exts);
                }
            }
        }
        //Hanh dong chinh kiem tra no la file va co suoi mo rong phu hop
        if (f.isFile()) {
            for (String extension : exts) {
                if (f.getName().endsWith(extension)) System.out.println(f.getAbsolutePath());
            }
        }
    }

    private static boolean delete(String path) {
        //Kiem tra path
        if (path == null) return  false;
        File f = new File(path);
        //Kiem tra path co ton tai khong
        if (!f.exists()) return false;
        //Neu la thu muc thi kiem tra rong hay khong sau do de quy
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            //Kiem tra f co quyen hay khong? Khong thi files = null
            if (files != null) {
                for (File file : files) {
                    delete(file.getAbsolutePath());
                }
            }
        }
        //Neu la file thi xoa
        return f.delete();
    }


}
