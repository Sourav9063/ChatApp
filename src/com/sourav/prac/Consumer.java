package com.sourav.prac;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    Thread t;
    BlockingQueue blockingQueue;
    String name;

    public Consumer(BlockingQueue blockingQueue, String name) {
        this.blockingQueue = blockingQueue;
        this.name = name;
        t = new Thread(this, name);
        t.start();
    }

    @Override
    public void run() {
        if(blockingQueue.size()==0) {
            System.out.println("Queue is empty");
            try {
                t.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true) {
            try {
                System.out.println(name + " is consuming " + blockingQueue.take());
                t.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



