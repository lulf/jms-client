package org.apache.qpid.jms.example;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Main {
    public static void main(String [] args) throws InterruptedException {
        if (args.length < 4) {
            throw new IllegalArgumentException("usage: progname <destination> <count> <brokerId> <brokerId>");
        } else {
            String destination = args[0];
            int count = Integer.parseInt(args[1]);
            String broker1 = args[2];
            String broker2 = args[3];

            ExecutorService executor = Executors.newFixedThreadPool(3);
            executor.execute(() -> Receiver.receive(destination, broker1, count));
            executor.execute(() -> Receiver.receive(destination, broker2, count));

            System.out.println("Started subscribers");
            Thread.sleep(5000);
            System.out.println("Starting publisher");
            executor.execute(() -> Sender.send(destination, broker1, count));

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.DAYS);
        }
    }
}
