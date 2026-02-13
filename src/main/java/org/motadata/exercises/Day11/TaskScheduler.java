package org.motadata.exercises.Day11;

import org.motadata.datastructures.heap.PriorityQueueBinaryHeap;

public class TaskScheduler {

    private final PriorityQueueBinaryHeap<Task> queue;

    public TaskScheduler() {
        this.queue = new PriorityQueueBinaryHeap<>();
    }

    public void scheduleTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        queue.offer(task);
    }

    public void executeNext() {
        Task task = queue.poll();
        if (task == null) {
            System.out.println("No tasks to execute.");
            return;
        }
        task.execute();
    }

    public void executeAll() {
        while (!queue.isEmpty()) {
            executeNext();
        }
    }

    public int pendingTasks() {
        return queue.size();
    }
}

