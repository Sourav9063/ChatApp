package com.sourav;

import java.io.*;
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
                System.out.println("Server response:" + returnMsg);
                if(returnMsg.toString().contains("Send File:")){
                    Scanner scanner = new Scanner(System.in);
                    String[] split = returnMsg.toString().split(":");
                    String fileName = split[2];
                    String sender = split[1];

                    System.out.println("Do you want to receive file? (y/n)");

                    String yn = scanner.nextLine();
                    System.out.println("You entered: " + yn);
                    if(yn.equals("y")) {

                        System.out.println("Input download location: ");
                        String downloadLocation = scanner.nextLine();

                        networkHelper.write("download:"+sender+":"+fileName);

                        try {
                            InputStream inputStream = networkHelper.socket.getInputStream();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                            File file = new File(downloadLocation + fileName);
                            System.out.println(file.getPath());
                            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                            byte[] buffer = new byte[1024];
                            int size = (int) networkHelper.read();
                            int readd = 0;
                            int bytesRead = 0;
                            System.out.println("Size : " + size);
                            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                                readd += bytesRead;
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


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        networkHelper.write("n");

                    }

                }

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
                networkHelper.write("Send File:"+fileName+":"+sendTo);
                new ClientServerFileSenderClass(networkHelper,location);


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
//class  ConsoleInput{
//    private Scanner scanner;
//    private static ConsoleInput consoleInput=null;
//    private ConsoleInput(){
//        scanner=new Scanner(System.in);
//    }
//    public static ConsoleInput getInstance(){
//        if(consoleInput==null){
//            consoleInput=new ConsoleInput();
//        }
//        return consoleInput;
//    }
//    public synchronized String consoleInput(){
//        return scanner.nextLine();
//    }
//}
//
