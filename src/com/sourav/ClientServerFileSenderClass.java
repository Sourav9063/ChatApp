package com.sourav;

import java.io.*;

public class ClientServerFileSenderClass implements Runnable {
   final  NetworkHelper networkHelper;
  final String fileName;
    Thread t;
    BufferedReader bufferedReader;

    BufferedInputStream bufferedInputStream;
    BufferedOutputStream bufferedOutputStream;
    ClientServerFileSenderClass(NetworkHelper networkHelper, String fileName){
        this.fileName=fileName;
        this.networkHelper=networkHelper;
        t=new Thread(this);
        t.start();
    }


    @Override
    public void run() {
        try {
            bufferedOutputStream=new BufferedOutputStream(networkHelper.getSocket().getOutputStream());
            File file=new File(fileName);
            if(!file.exists()){
                System.out.println("File not found");
                bufferedOutputStream.write((byte) 0);
            }
            else{
                bufferedOutputStream.write((byte) 1);
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                byte[] buffer = new byte[1024];
                int bytesRead=0;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                    bufferedOutputStream.flush();
                }

            }
//            closeConnection();

        }
        catch (Exception e){

        }

    }

    void closeConnection(){
        try {
            bufferedOutputStream.close();
            bufferedInputStream.close();
//            networkHelper.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
