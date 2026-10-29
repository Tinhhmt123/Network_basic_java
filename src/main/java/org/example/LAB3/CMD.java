package org.example.LAB3;

import java.io.*;
import java.util.Locale;
import java.util.StringTokenizer;

public class CMD {

    private String path = "D:\\My_Study_of_TINH\\LTM\\Test";
    private File fDefault;
    public CMD() {
        fDefault = new File(path);
    }

    public File getfDefault() {
        return fDefault;
    }

    public void setfDefault(File fDefault) {
        this.fDefault = fDefault;
    }

    static void main() throws IOException {
        CMD cmd = new CMD();
        cmd.run();
    }

    private void run() throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print(this.fDefault.getAbsolutePath());
            String input = reader.readLine();

            if (input == null || input.isEmpty()) continue;

            StringTokenizer token = new StringTokenizer(input.trim());
            String res = "";
            String head = token.nextToken();

            String param = token.hasMoreTokens() ? token.nextToken() : null;

            switch (head.toLowerCase()) {
                case "" : {
                    break;
                }

                case "exit" : {
                    System.out.print("Da thoat chuong trinh");
                    return;
                }

                case "dir" : {
                    printRes(res, printListFile());
                    break;
                }

                case "cd" : {
                    if (param == null || param.isEmpty()) {
                        break;
                    }

                    printRes(res, handleCD(param));
                    break;
                }

                case "del" : {

                    if (param == null || param.isEmpty()) {
                        printRes(res, "The syntax of the command is incorrect.");
                        break;
                    }

                    printRes(res, deleteFile(param));
                    break;
                }

                default: {
                    param = (param == null) ? "" : param;
                    printRes(res, "'\" + head + \" \"+param+ \"' is not recognized as an internal or external command.");
                    break;
                }
            }
        }
    }

    private void printRes(String res1, String res2) {
        res1 = res2;
        System.out.println(res1);
        System.out.println();
    }

    private String deleteFile(String param) {
        File target = new File(param);

        if (!target.isAbsolute()) {
            target = new File(this.fDefault, param);
        }

        if (!target.exists()) return "Could Not Find " + target.getAbsolutePath();
        if (target.isDirectory()) return "The syntax of the command is incorrect.";

        if (target.delete()) {
            return "";
        } else {
            return "Access is denied.";
        }
    }

    private String handleCD(String param) throws IOException {
        File target = new File(param);

        if (!target.isAbsolute()) {
            target = new File(this.fDefault, param);
        }

        if (!target.exists()) return "The system cannot find the path specified.";

        if (target.isFile()) return "The directory name is invalid.";

        if (target.isDirectory()) {
            this.setfDefault(target.getCanonicalFile());
        }

        return "";
    }

    private String printListFile() {
        StringBuilder dirs = new StringBuilder();
        StringBuilder files = new StringBuilder();

        File[] ls = this.fDefault.listFiles();

        if (ls == null) return "Access is denied.";

        for (File f : ls) {
            if (f.isDirectory()) {
                dirs.append("<DIR>\t").append(f.getName()).append("\n");
            }
            if (f.isFile()) {
                files.append("     \t").append(f.getName()).append("\n");
            }
        }
        return dirs.toString() + "" + files.toString();
    }

}
