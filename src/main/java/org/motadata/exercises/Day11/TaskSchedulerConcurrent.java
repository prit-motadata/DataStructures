package org.motadata.exercises.Day11;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Concurrent task scheduler backed by a {@link PriorityBlockingQueue} and a fixed thread pool.
 *
 * <p>Tasks are ordered by their priority as defined in {@link Task} and executed by worker threads.</p>
 *
 * @author prit.thakkar@motadata.com
 */
public class TaskSchedulerConcurrent {

    private final PriorityBlockingQueue<Task> queue =
            new PriorityBlockingQueue<>();

    private final ExecutorService workers;
    private volatile boolean running = true;

    /**
     * Creates a scheduler with the given number of worker threads.
     *
     * @param workerCount number of worker threads to use
     * @throws IllegalArgumentException if {@code workerCount} is not positive
     */
    public TaskSchedulerConcurrent(int workerCount) {
        if (workerCount <= 0) {
            throw new IllegalArgumentException("Worker count must be > 0");
        }

        workers = Executors.newFixedThreadPool(workerCount);

        for (int i = 0; i < workerCount; i++) {
            workers.submit(this::processTasks);
        }
    }

    /**
     * Enqueues a task for execution according to its priority.
     *
     * @param task task to schedule
     * @throws IllegalArgumentException if {@code task} is {@code null}
     * @see Task
     */
    public void scheduleTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        queue.offer(task);
    }

    /**
     * Internal worker loop that pulls tasks from the queue and executes them while the scheduler is running.
     */
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

    /**
     * Returns the number of tasks that are still pending in the queue.
     *
     * @return pending task count
     */
    public int pendingTasks() {
        return queue.size();
    }

    /**
     * Attempts to gracefully shut down all worker threads, forcing termination if they do not stop in time.
     */
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