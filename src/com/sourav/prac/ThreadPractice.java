package com.sourav.prac;
class NewThread implements Runnable{
    Thread t;
    NewThread(){
        t = new Thread(this,"MyThread");
        t.start();

    }

    @Override
    public void run() {
    for(int i=0;i<5;i++){
        System.out.println("Child Thread");
        try {
            t.sleep(1000);
            System.out.println(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    }
}

public class ThreadPractice {

    public static void main(String[] args) {
        NewThread nt = new NewThread();
        for(int i=0;i<5;i++){
            System.out.println("Main Thread");
            try {
                Thread.sleep(100);
                System.out.println(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    }

