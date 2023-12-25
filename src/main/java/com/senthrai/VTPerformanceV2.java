package com.senthrai;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class VTPerformanceV2 {
    public static void main(String[] args) throws InterruptedException {
        Set<String> poolNames = ConcurrentHashMap.newKeySet();
        Set<String> pThreadsNames = ConcurrentHashMap.newKeySet();

        List<Thread> threads = IntStream.range(0,10000000).mapToObj(i-> Thread.ofVirtual().unstarted(()->{
            String poolName = readPoolName();
            poolNames.add(poolName);
            String workerName = readWorkerName();
            pThreadsNames.add(workerName);
        })).toList();

        Instant begin = Instant.now();
        threads.forEach(Thread::start);

        for (Thread thread : threads){
            thread.join();
        }

        Instant end = Instant.now();
        System.out.println("Time = " + Duration.between(begin,end).toMillis() + "ms");
        System.out.println("# core: " + Runtime.getRuntime().availableProcessors());
        System.out.println("# pools: " + poolNames.size());
        System.out.println("# platform threads: " + pThreadsNames.size());
    }

    private static String readPoolName() {
        String name = Thread.currentThread().toString();
        int i1 = name.indexOf("@ForkJoinPool");
        int i2 = name.indexOf("worker");
        return name.substring(i1,i2);
    }

    private static String readWorkerName() {
        String name = Thread.currentThread().toString();
        int i1 = name.indexOf("worker");
        return name.substring(i1);
    }
}
