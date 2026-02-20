package org.motadata.exercises.Day11;

/**
 * A unit of work with an associated priority and identifier, executable via a {@link Runnable}.
 *
 * <p>Tasks are ordered by descending priority for use in priority-based schedulers.</p>
 *
 * @author prit.thakkar@motadata.com
 */
public class Task implements Comparable<Task> {

    private final String id;
    private final int priority;
    private final Runnable action;

    /**
     * Creates a new task.
     *
     * @param id       human-readable task identifier
     * @param priority numeric priority; higher values indicate higher priority
     * @param action   action to execute when the task runs
     * @throws IllegalArgumentException if {@code action} is {@code null}
     */
    public Task(String id, int priority, Runnable action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

        this.id = id;
        this.priority = priority;
        this.action = action;
    }

    /**
     * Executes this task's action, logging basic information to standard output.
     */
    public void execute() {
        System.out.println("Executing Task: " + id +
                " | Priority: " + priority);
        action.run();
    }

    /**
     * Compares this task with another based on priority, ordering higher priorities first.
     *
     * @param other other task to compare against
     * @return a negative integer, zero, or a positive integer as this task has
     * a higher, equal, or lower priority than the specified task
     */
    @Override
    public int compareTo(Task other) {
        // Max heap behavior
        return Integer.compare(other.priority, this.priority);
    }
}

