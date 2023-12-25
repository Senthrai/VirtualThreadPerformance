package com.senthrai;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Thread platformThread = Thread.ofPlatform().unstarted(()-> System.out.println(Thread.currentThread()));
        platformThread.start();

        Thread virtualThread = Thread.ofVirtual().unstarted(()-> System.out.println(Thread.currentThread()));
        virtualThread.start();

        platformThread.join();

        virtualThread.join();

        System.out.println(virtualThread.getClass());

    }
}