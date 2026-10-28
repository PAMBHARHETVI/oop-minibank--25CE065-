package service;

import java.util.ArrayDeque;
import java.util.Queue;

/** Small bounded producer-consumer example using wait/notify. */
public class ProducerConsumerDemo {
    private final Queue<String> buffer = new ArrayDeque<>();
    private final int capacity = 3;
    private boolean finished;

    private void produce(String item) throws InterruptedException {
        synchronized (buffer) {
            while (buffer.size() == capacity)
                buffer.wait();
            buffer.add(item);
            System.out.println("Produced: " + item);
            buffer.notifyAll();
        }
    }

    private String consume() throws InterruptedException {
        synchronized (buffer) {
            while (buffer.isEmpty() && !finished)
                buffer.wait();
            if (buffer.isEmpty())
                return null;
            String item = buffer.remove();
            buffer.notifyAll();
            return item;
        }
    }

    public void runDemo() throws InterruptedException {
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++)
                    produce("TX-" + i);
                synchronized (buffer) {
                    finished = true;
                    buffer.notifyAll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "TransactionProducer");
        Thread consumer = new Thread(() -> {
            try {
                String item;
                while ((item = consume()) != null)
                    System.out.println("Processed: " + item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "TransactionConsumer");
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}
