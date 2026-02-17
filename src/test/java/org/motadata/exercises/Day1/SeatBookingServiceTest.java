package org.motadata.exercises.Day1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SeatBookingServiceTest {

    private SeatBookingService seatBookingService;

    @BeforeEach
    void setUp() {
        seatBookingService = new SeatBookingService(300);
    }

    @Test
    void shouldBookSeatSuccessfully() {
        boolean booked = seatBookingService.bookSeat(10);

        assertTrue(booked);
        assertFalse(seatBookingService.isSeatAvailable(10));
    }

    @Test
    void shouldNotBookAlreadyBookedSeat() {
        seatBookingService.bookSeat(15);

        boolean bookedAgain = seatBookingService.bookSeat(15);

        assertFalse(bookedAgain);
    }

    @Test
    void shouldThrowExceptionForInvalidSeatNumberLow() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatBookingService.bookSeat(0)
        );
    }

    @Test
    void shouldThrowExceptionForInvalidSeatNumberHigh() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatBookingService.bookSeat(301)
        );
    }

    @Test
    void shouldCancelBookedSeat() {
        seatBookingService.bookSeat(20);

        boolean cancelled = seatBookingService.cancelSeat(20);

        assertTrue(cancelled);
        assertTrue(seatBookingService.isSeatAvailable(20));
    }

    @Test
    void shouldNotCancelEmptySeat() {
        boolean cancelled = seatBookingService.cancelSeat(25);

        assertFalse(cancelled);
    }

    @Test
    void shouldCheckSeatAvailabilityCorrectly() {
        assertTrue(seatBookingService.isSeatAvailable(5));

        seatBookingService.bookSeat(5);

        assertFalse(seatBookingService.isSeatAvailable(5));
    }

    @Test
    void shouldThrowExceptionForInvalidSeatNumberInIsSeatAvailable() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatBookingService.isSeatAvailable(0)
        );
    }

    @Test
    void shouldThrowExceptionForInvalidSeatNumberInCancelSeat() {
        assertThrows(
                IllegalArgumentException.class,
                () -> seatBookingService.cancelSeat(400)
        );
    }

    @Test
    void shouldReturnAvailableSeatsCorrectly() {
        seatBookingService.bookSeat(1);
        seatBookingService.bookSeat(300);

        List<Integer> availableSeats = seatBookingService.getAvailableSeats();

        assertFalse(availableSeats.contains(1));
        assertFalse(availableSeats.contains(300));
        assertTrue(availableSeats.contains(2));
        assertEquals(298, availableSeats.size());
    }

    @Test
    void shouldHandleBoundarySeats() {
        assertTrue(seatBookingService.bookSeat(1));
        assertTrue(seatBookingService.bookSeat(300));

        assertFalse(seatBookingService.isSeatAvailable(1));
        assertFalse(seatBookingService.isSeatAvailable(300));
    }

    @Test
    void shouldBookFirstAvailableSeatWhenNoSeatNumberProvided() {
        boolean booked = seatBookingService.bookSeat();

        assertTrue(booked);
        assertFalse(seatBookingService.isSeatAvailable(1));
    }

    @Test
    void shouldNotBookSeatWhenAllSeatsAreBookedWithoutSeatNumber() {
        // Book all seats explicitly
        for (int i = 1; i <= 300; i++) {
            assertTrue(seatBookingService.bookSeat(i));
        }

        // Now try booking without specifying seat number
        boolean booked = seatBookingService.bookSeat();

        assertFalse(booked);
    }

    @Test
    void shouldAllowOnlyOneBookingForSameSeatConcurrently() throws InterruptedException {
        int seatNumber = 42;
        int threadCount = 50;

        List<Boolean> results;
        boolean finishedInTime;
        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(threadCount);

            results = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        startLatch.await();
                        boolean booked = seatBookingService.bookSeat(seatNumber);
                        synchronized (results) {
                            results.add(booked);
                        }
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            // Start all threads at once
            startLatch.countDown();

            // Wait for all threads to finish
            finishedInTime = doneLatch.await(5, TimeUnit.SECONDS);
            executorService.shutdownNow();
        }

        assertTrue(finishedInTime, "Threads did not finish in time");

        long successfulBookings = results.stream().filter(b -> b).count();

        assertEquals(1, successfulBookings, "Only one thread should successfully book the same seat");
        assertFalse(seatBookingService.isSeatAvailable(seatNumber), "Seat should be booked");
    }

    @Test
    void shouldBookAtMostTotalSeatsWhenBookingConcurrentlyWithoutSeatNumber() throws InterruptedException {
        int totalSeats = 300;
        int threadCount = 400;

        List<Boolean> results;
        boolean finishedInTime;
        try (ExecutorService executorService = Executors.newFixedThreadPool(50)) {
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(threadCount);

            results = new ArrayList<>();

            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        startLatch.await();
                        boolean booked = seatBookingService.bookSeat();
                        synchronized (results) {
                            results.add(booked);
                        }
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            // Start all threads at once
            startLatch.countDown();

            // Wait for all threads to finish
            finishedInTime = doneLatch.await(10, TimeUnit.SECONDS);
            executorService.shutdownNow();
        }

        assertTrue(finishedInTime, "Threads did not finish in time");

        long successfulBookings = results.stream().filter(b -> b).count();

        assertEquals(totalSeats, successfulBookings, "Exactly totalSeats bookings should succeed");
        assertEquals(0, seatBookingService.getAvailableSeats().size(), "No seats should be available");
    }
}
