package com.sourav;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientClass {
    public static void main(String[] args) {
        String userName;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        userName = scanner.nextLine();
        System.out.println("Client Class");

        try {
            NetworkHelper networkHelper = new NetworkHelper("127.0.0.1", 1234);
            System.out.println("Connected to server");
            networkHelper.write(userName);
            Object response = networkHelper.read();
            System.out.println("Your user name is: " + response.toString());

            new Thread(new WriterThread(networkHelper)).start();
            new Thread(new ReaderThread(networkHelper)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        scanner.close();
    }
}

class ReaderThread implements Runnable {
    NetworkHelper networkHelper;

    public ReaderThread(NetworkHelper networkHelper) {
        this.networkHelper = networkHelper;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Object returnMsg = networkHelper.ois.readObject();
                if (returnMsg == null)
                    break;
                System.out.println("Server response:\n" + returnMsg);

            } catch (IOException e) {

                e.printStackTrace();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}

class WriterThread implements Runnable {
    NetworkHelper networkHelper;

    public WriterThread(NetworkHelper networkHelper) {
        this.networkHelper = networkHelper;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                networkHelper.write(null);
                break;
            }
          else if(message.contains("Send File:")){
//                E:\\Books\\Books study 3.1\\Distributive System\\Assaignment\\ChatApp\\Files\\Received\\File2.txt
                System.out.println("Input Location");
                String location = scanner.nextLine();
                System.out.println("Input File Name");
                String fileName = scanner.nextLine();

                System.out.println("Send To?");
                String sendTo = scanner.nextLine();

                new ClientServerFileSenderClass(networkHelper,location);
                networkHelper.write("Send File:"+fileName);

            }
            else {
                networkHelper.write(message);
            }

        }
        try {
            networkHelper.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        scanner.close();
    }
}