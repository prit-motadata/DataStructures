package org.motadata.exercises.Day1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
}
