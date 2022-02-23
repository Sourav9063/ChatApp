package com.sourav;

import java.io.IOException;
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
                String userName = (String) connectedUserName+clientCount;
                System.out.println("Connected User Name : " + connectedUserName);
                userList.put(userName, networkHelper);

                networkHelper.write(userName+"\n\"List?\" for list of users"+"\n\"Exit\" to exit"+"\n\"To:sender username:your message\" to send message");
                new ServerThreadClass(userName,networkHelper,userList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerThreadClass implements Runnable {
    private NetworkHelper networkHelper;
    HashMap<String,NetworkHelper> userList;
    String currentUser;
    Thread t;

    public ServerThreadClass(String currentUser,NetworkHelper networkHelper,HashMap<String,NetworkHelper> userList) {
        this.networkHelper = networkHelper;
        this.userList = userList;
        this.currentUser=currentUser;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {


        while (true) {
            Object object = networkHelper.read();
            if (object == null) break;
            if(object.toString().contains("List?")){
//                hm.toString();
//                networkHelper.write(userList.toString());
                String list = "User List : \n";
                for(String key:userList.keySet()){
                    list=list+key+"\n";
                }
                networkHelper.write(list);
            }

            if(object.toString().contains("To:")){
                String[] split = object.toString().split(":");
                String to = split[1];
                String message = split[2];
                userList.get(to).write(currentUser+":"+message);

            }
            if(object.toString().contains("Send File:")){
                System.out.println("Send****************************************************Called");
                String[] split = object.toString().split(":");
               for(String s:split){
                   System.out.println(s);
               }

                String name = split[1];
             ClientFileReceiveClass receiveClass=   new ClientFileReceiveClass(networkHelper,"E:\\Books\\Books study 3.1\\Distributive System\\Assaignment\\ChatApp\\Files\\Temp\\"+name);

                networkHelper.write("File Received at Server");

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