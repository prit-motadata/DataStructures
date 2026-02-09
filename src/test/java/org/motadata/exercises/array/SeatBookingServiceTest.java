package org.motadata.exercises.array;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
