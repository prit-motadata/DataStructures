package org.motadata.exercises.Day11;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskSchedulerTest {

    private TaskScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new TaskScheduler();
    }

    @Test
    void testScheduleAndExecuteSingleTask() {
        Task task = new Task("T1", 1, () -> {});
        scheduler.scheduleTask(task);

        assertEquals(1, scheduler.pendingTasks());

        scheduler.executeNext();

        assertEquals(0, scheduler.pendingTasks());
    }

    @Test
    void testExecuteInPriorityOrder() {
        StringBuilder result = new StringBuilder();

        scheduler.scheduleTask(new Task("Low", 1, () -> result.append("L")));
        scheduler.scheduleTask(new Task("High", 10, () -> result.append("H")));
        scheduler.scheduleTask(new Task("Medium", 5, () -> result.append("M")));

        scheduler.executeAll();

        // Assuming max heap behavior (higher number = higher priority)
        assertEquals("HML", result.toString());
        assertEquals(0, scheduler.pendingTasks());
    }

    @Test
    void testExecuteNextWhenEmpty() {
        assertEquals(0, scheduler.pendingTasks());

        // Should not throw exception
        scheduler.executeNext();

        assertEquals(0, scheduler.pendingTasks());
    }

    @Test
    void testExecuteAllWhenEmpty() {
        assertEquals(0, scheduler.pendingTasks());

        scheduler.executeAll(); // Should not crash

        assertEquals(0, scheduler.pendingTasks());
    }

    @Test
    void testScheduleNullTaskThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> scheduler.scheduleTask(null));
    }

    @Test
    void testPendingTasks() {
        scheduler.scheduleTask(new Task("T1", 1, () -> {}));
        scheduler.scheduleTask(new Task("T2", 2, () -> {}));

        assertEquals(2, scheduler.pendingTasks());

        scheduler.executeNext();

        assertEquals(1, scheduler.pendingTasks());
    }
}
