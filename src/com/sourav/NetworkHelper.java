package com.sourav;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NetworkHelper {
    Socket socket;
    ObjectInputStream ois;

    ObjectOutputStream oos;

    public NetworkHelper(Socket sock) throws IOException {
        socket=sock;
        oos=new ObjectOutputStream(socket.getOutputStream());
        ois=new ObjectInputStream(socket.getInputStream());
    }

    public NetworkHelper(String ip,int port) throws IOException{
        socket=new Socket(ip, port);
        oos=new ObjectOutputStream(socket.getOutputStream());
        ois=new ObjectInputStream(socket.getInputStream());
    }

    public void write(Object obj){
        try {
            oos.writeObject(obj);
        } catch (IOException ex) {
            System.out.println("Failed to write");
            ex.printStackTrace();
            //throw ex;
        }
    }

    public Object read(){
        Object obj;
        try {
            obj = ois.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed to read");
            return null;
        }
        return obj;
    }

    public Socket getSocket() {
        return socket;
    }
}
