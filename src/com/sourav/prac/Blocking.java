package com.sourav.prac;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Blocking {
    public static void main(String[] args) {
        BlockingQueue blockingQueue= new ArrayBlockingQueue(5);
        Producer producer1=new Producer(blockingQueue,"Producer1");
        Consumer consumer1=new Consumer(blockingQueue,"Consumer1");
        Consumer consumer2=new Consumer(blockingQueue,"Consumer2");

    }
}
