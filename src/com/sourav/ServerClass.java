package com.sourav;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerClass {
    public static void main(String[] args) {
        HashMap<String, NetworkHelper> userList = new HashMap<>();
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server Started");
            int clientCount = 0;
            while (true) {
                Socket socket = serverSocket.accept();

                clientCount++;

                NetworkHelper networkHelper = new NetworkHelper(socket);

                Object connectedUserName = networkHelper.read();
                String userName = (String) connectedUserName + clientCount;
                System.out.println("Connected User Name : " + connectedUserName);
                userList.put(userName, networkHelper);

                networkHelper.write(userName + "\n\"List?\" for list of users" + "\n\"Exit\" to exit" + "\n\"To:sender username:your message\" to send message\n \"Send File:\" to send file.");
                new ServerThreadClass(userName, networkHelper, userList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerThreadClass implements Runnable {
    private NetworkHelper networkHelper;
    HashMap<String, NetworkHelper> userList;
    String currentUser;
    Thread t;

    public ServerThreadClass(String currentUser, NetworkHelper networkHelper, HashMap<String, NetworkHelper> userList) {
        this.networkHelper = networkHelper;
        this.userList = userList;
        this.currentUser = currentUser;
        t = new Thread(this);
        t.setName(currentUser);
        t.start();
    }

    @Override
    public void run() {


        while (true) {
            System.out.println("Waiting for input" + t.getName());
            Object object = networkHelper.read();
            if (object == null) break;
            if(object.toString().contains("download:")){
                String[] st = object.toString().split(":");

                for (String s : st) {
                    System.out.println(s);
                }
                new ClientServerFileSenderClass(networkHelper,"E:\\Books\\Books study 3.1\\Distributive System\\Assaignment\\ChatApp\\Files\\Temp\\" + st[2]);
            }

            if (object.toString().contains("List?")) {
//                hm.toString();
//                networkHelper.write(userList.toString());
                String list = "User List : \n";
                for (String key : userList.keySet()) {
                    list = list + key + "\n";
                }
                networkHelper.write(list);
            }

            if (object.toString().contains("To:")) {
                String[] split = object.toString().split(":");
                String to = split[1];
                String message = split[2];
                userList.get(to).write(currentUser + ":" + message);
                networkHelper.write("Message Sent");
            }
            if (object.toString().contains("Send File:")) {

                String[] split = object.toString().split(":");
                for (String s : split) {
                    System.out.println(s);
                }

                String name = split[1];
                String to   =split[2];
//             ClientFileReceiveClass receiveClass=   new ClientFileReceiveClass(networkHelper,"E:\\Books\\Books study 3.1\\Distributive System\\Assaignment\\ChatApp\\Files\\Temp\\"+name);
//receiveClass.run();
                try {
                    InputStream inputStream = networkHelper.socket.getInputStream();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                    File file = new File("E:\\Books\\Books study 3.1\\Distributive System\\Assaignment\\ChatApp\\Files\\Temp\\" + name);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    byte[] buffer = new byte[1024];
                    int size = (int) networkHelper.read();
                    int readd = 0;
                    int bytesRead = 0;
                    System.out.println("Size : " + size);
                    while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                        readd += bytesRead;
//                        if (readd>size) {break;}
                        System.out.println("Read : " + readd);

                        System.out.println("Bytes Read : " + bytesRead);
                        bufferedOutputStream.write(buffer, 0, bytesRead);
                        bufferedOutputStream.flush();
                        if (readd >= size) {
                            break;
                        }
                    }
                    System.out.println(bytesRead);


                    System.out.println("File Received");
                    networkHelper.write("File Sent Successfully");
                    userList.get(to).write("File Received From : " + currentUser + " : " + name);
                    userList.get(to).write("Send File:"+currentUser+":"+name);





                } catch (IOException e) {
                    e.printStackTrace();
                }
//                networkHelper.write("File Received at Server");

                System.out.println("Done");


            }


        }
        try {
            networkHelper.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}