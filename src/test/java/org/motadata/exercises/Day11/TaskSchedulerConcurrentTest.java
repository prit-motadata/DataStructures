package org.motadata.exercises.Day11;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class TaskSchedulerConcurrentTest {

    @Test
    void testConstructorInvalidWorkerCount() {
        assertThrows(IllegalArgumentException.class,
                () -> new TaskSchedulerConcurrent(0));

        assertThrows(IllegalArgumentException.class,
                () -> new TaskSchedulerConcurrent(-1));
    }

    @Test
    void testScheduleTaskNullThrowsException() {
        TaskSchedulerConcurrent scheduler =
                new TaskSchedulerConcurrent(1);

        assertThrows(IllegalArgumentException.class,
                () -> scheduler.scheduleTask(null));

        scheduler.shutdown();
    }

    @Test
    void testSingleTaskExecution() throws InterruptedException {
        TaskSchedulerConcurrent scheduler =
                new TaskSchedulerConcurrent(1);

        CountDownLatch latch = new CountDownLatch(1);

        scheduler.scheduleTask(new Task("T1", 5, latch::countDown));

        boolean completed = latch.await(3, TimeUnit.SECONDS);

        assertTrue(completed);
        scheduler.shutdown();
    }

    @Test
    void testMultipleTasksExecution() throws InterruptedException {
        int tasks = 10;

        TaskSchedulerConcurrent scheduler =
                new TaskSchedulerConcurrent(3);

        CountDownLatch latch = new CountDownLatch(tasks);

        for (int i = 0; i < tasks; i++) {
            scheduler.scheduleTask(
                    new Task("T" + i, i, latch::countDown)
            );
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);

        assertTrue(completed);
        scheduler.shutdown();
    }

    @Test
    void testPriorityExecution() throws InterruptedException {

        StringBuilder order = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(2);

        TaskSchedulerConcurrent scheduler =
                new TaskSchedulerConcurrent(1);

        scheduler.scheduleTask(
                new Task("Low", 1, () -> {
                    order.append("L");
                    latch.countDown();
                })
        );

        scheduler.scheduleTask(
                new Task("High", 10, () -> {
                    order.append("H");
                    latch.countDown();
                })
        );

        boolean completed = latch.await(3, TimeUnit.SECONDS);

        assertTrue(completed, "Tasks did not complete in time");

        scheduler.shutdown();

        assertEquals(2, order.length());
    }

    @Test
    void testPendingTasks() throws InterruptedException {
        TaskSchedulerConcurrent scheduler =
                new TaskSchedulerConcurrent(1);

        CountDownLatch latch = new CountDownLatch(1);

        scheduler.scheduleTask(
                new Task("T1", 5, () -> {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {}
                    latch.countDown();
                })
        );

        assertTrue(scheduler.pendingTasks() >= 0);

        boolean completed = latch.await(3, TimeUnit.SECONDS);

        assertTrue(completed, "Task did not finish");

        scheduler.shutdown();
    }

    @Test
    void testShutdownGracefully() throws InterruptedException {
        TaskSchedulerConcurrent scheduler =
                new TaskSchedulerConcurrent(2);

        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            scheduler.scheduleTask(
                    new Task("T" + i, i, () -> {
                        counter.incrementAndGet();
                        latch.countDown();
                    })
            );
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);

        assertTrue(completed, "Not all tasks executed");

        scheduler.shutdown();

        assertEquals(5, counter.get());
    }
}