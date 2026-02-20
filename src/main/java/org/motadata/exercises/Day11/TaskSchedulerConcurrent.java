package org.motadata.exercises.Day11;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskSchedulerConcurrent {

    private final PriorityBlockingQueue<Task> queue =
            new PriorityBlockingQueue<>();

    private final ExecutorService workers;
    private volatile boolean running = true;

    public TaskSchedulerConcurrent(int workerCount) {
        if (workerCount <= 0) {
            throw new IllegalArgumentException("Worker count must be > 0");
        }

        workers = Executors.newFixedThreadPool(workerCount);

        for (int i = 0; i < workerCount; i++) {
            workers.submit(this::processTasks);
        }
    }

    public void scheduleTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        queue.offer(task);
    }

    private void processTasks() {
        while (running || !queue.isEmpty()) {
            try {
                Task task = queue.poll(1, TimeUnit.SECONDS);
                if (task != null) {
                    task.execute();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int pendingTasks() {
        return queue.size();
    }

    public void shutdown() {
        running = false;
        workers.shutdown();
        try {
            boolean terminated =
                    workers.awaitTermination(5, TimeUnit.SECONDS);

            if (!terminated) {
                workers.shutdownNow();
            }
        } catch (InterruptedException e) {
            workers.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}