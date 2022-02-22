package com.sourav;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FilesToByte {
    // public static void main(String[] args) {
    // File file = new File("Files/Received/File2.txt");
    // try {
    // file.createNewFile();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    public static void SendFile(String filePath, NetworkHelper networkHelper) {

        try {
            File file = new File(filePath);

            FileInputStream fis = new FileInputStream(filePath);
            byte[] fileBytes = new byte[1024];
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(fileBytes, 0, fileBytes.length);

            networkHelper.oos.write(fileBytes, 0, fileBytes.length);
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void receiveFile(String filePath, NetworkHelper networkHelper) {
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {

            System.out.println("Connecting...");

            // receive file
            byte[] mybytearray = new byte[1024];
            InputStream is =  networkHelper.socket.getInputStream();
            fos = new FileOutputStream(filePath);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;
            for(int i = 0; i < mybytearray.length; i++) {
                System.out.println(mybytearray[i]);
            }

//            do {
//                bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
//                if (bytesRead >= 0)
//                    current += bytesRead;
////                System.out.println("Receiving...");
////                System.out.println(bytesRead);
//
//            } while (bytesRead >= -1);
bos.write(mybytearray, 0, current);
//            bos.flush();
            System.out.println("File " + filePath
                    + " downloaded (" + current + " bytes read)");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        }
    }

}

