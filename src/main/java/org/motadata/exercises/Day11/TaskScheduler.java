package org.motadata.exercises.Day11;

import org.motadata.datastructures.heap.PriorityQueueBinaryHeap;

/**
 * A task scheduler that manages execution based on task priority.
 *
 * <p>
 * Uses a {@link org.motadata.datastructures.heap.PriorityQueueBinaryHeap} to
 * ensure
 * the highest priority task is always executed next.
 * </p>
 *
 * @author prit.thakkar@motadata.com
 */
public class TaskScheduler {

    private final PriorityQueueBinaryHeap<Task> queue;

    /**
     * Creates a new scheduler with an empty task queue.
     */
    public TaskScheduler() {
        this.queue = new PriorityQueueBinaryHeap<>();
    }

    /**
     * Schedules a task for future execution based on its priority.
     *
     * @param task the task to schedule
     * @throws IllegalArgumentException if task is null
     */
    public void scheduleTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        queue.offer(task);
    }

    /**
     * Executes the next highest priority task in the queue.
     *
     * <p>
     * If no tasks are available, a message is printed to standard output.
     * </p>
     */
    public void executeNext() {
        Task task = queue.poll();
        if (task == null) {
            System.out.println("No tasks to execute.");
            return;
        }
        task.execute();
    }

    /**
     * Executes all pending tasks in the queue in order of their priority.
     */
    public void executeAll() {
        while (!queue.isEmpty()) {
            executeNext();
        }
    }

    /**
     * Returns the number of tasks currently waiting in the queue.
     *
     * @return pending task count
     */
    public int pendingTasks() {
        return queue.size();
    }
}
