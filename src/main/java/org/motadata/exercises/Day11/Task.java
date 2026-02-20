package org.motadata.exercises.Day11;

public class Task implements Comparable<Task> {

    private final String id;
    private final int priority;
    private final Runnable action;

    public Task(String id, int priority, Runnable action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

        this.id = id;
        this.priority = priority;
        this.action = action;
    }

    public void execute() {
        System.out.println("Executing Task: " + id +
                " | Priority: " + priority);
        action.run();
    }

    @Override
    public int compareTo(Task other) {
        // Max heap behavior
        return Integer.compare(other.priority, this.priority);
    }
}

