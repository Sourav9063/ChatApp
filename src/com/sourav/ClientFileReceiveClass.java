package com.sourav;

import java.io.*;

public class ClientFileReceiveClass implements   Runnable{
    Thread t;
   final NetworkHelper networkHelper;
   final String receiveLocation;
    public ClientFileReceiveClass(NetworkHelper networkHelper, String receiveLocation){
        this.networkHelper = networkHelper;
        this.receiveLocation = receiveLocation;
        t=new Thread(this);
        t.start();
    }
    @Override
    public void run() {
        try {
            InputStream inputStream = networkHelper.socket.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            int code = bufferedInputStream.read();
            if(code==1){
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(receiveLocation));
                                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                    bufferedOutputStream.flush();
                }
                System.out.println("File Received");
            }
            else{
                System.out.println("File not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
