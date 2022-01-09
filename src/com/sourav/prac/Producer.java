package com.sourav.prac;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable{
    Thread t;
    BlockingQueue blockingQueue;
    String name;
    Producer(BlockingQueue blockingQueue,String name) {
        this.blockingQueue = blockingQueue;
        this.name = name;
        t = new Thread(this,name);
        t.start();
    }

    @Override
    public void run() {
        int i = 0;
        while(true){
            if(blockingQueue.size()>5){
                try {
                    System.out.println("Producer "+name+" is waiting");
                    System.out.println("Queue is full");

                    t.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    blockingQueue.put("Cake "+i);
                    System.out.println("Producer "+name+" is producing cake "+i);
                    i++;
                    t.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
