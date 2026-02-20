package org.motadata.exercises.Day1;

import org.motadata.datastructures.array.ArrayFixedSize;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that manages concurrent seat bookings using a fixed-size backing array and per-seat locks.
 *
 * @author prit.thakkar@motadata.com
 */
public class SeatBookingService {

    private static final int EMPTY = 0;
    private static final int BOOKED = 1;

    private final ArrayFixedSize<Integer> seats;
    private final ConcurrentHashMap<Integer, Object> seatLocks = new ConcurrentHashMap<>();

    /**
     * Creates a new seat booking service with the given number of seats.
     *
     * @param totalSeats total number of seats that can be booked
     */
    public SeatBookingService(int totalSeats) {
        seats = new ArrayFixedSize<>(totalSeats);
        seats.fill(EMPTY); // initialize all seats as empty
    }

    /**
     * Books the first available seat.
     *
     * @return {@code true} if a seat was successfully booked, {@code false} if no seats were available
     * @see #bookSeat(int)
     * @see #getAvailableSeats()
     */
    public boolean bookSeat() {
        for (int seatNumber = 1; seatNumber <= seats.size(); seatNumber++) {
            Object lock = seatLocks.computeIfAbsent(seatNumber, key -> new Object());
            int index = seatNumber - 1;

            synchronized (lock) {
                if (seats.get(index) == EMPTY) {
                    seats.set(index, BOOKED);
                    return true;
                }
            }
        }
        return false; // no seats available
    }

    /**
     * Books a specific seat by its 1-based seat number.
     *
     * @param seatNumber the 1-based seat number to book
     * @return {@code true} if the seat was successfully booked, {@code false} if it was already booked
     * @throws IllegalArgumentException if the seat number is outside the valid range
     * @see #isSeatAvailable(int)
     */
    public boolean bookSeat(int seatNumber) {
        if (!isValidSeatNumber(seatNumber)) {
            throw new IllegalArgumentException(
                    "Seat number must be between 1 and " + seats.size()
            );
        }

        Object lock = seatLocks.computeIfAbsent(seatNumber, key -> new Object());
        int index = seatNumber - 1;

        synchronized (lock) {
            if (seats.get(index) == BOOKED) {
                return false; // already booked
            }

            seats.set(index, BOOKED);
            return true;
        }
    }

    /**
     * Checks whether the given 1-based seat number is currently available.
     *
     * @param seatNumber the seat number to check
     * @return {@code true} if the seat is available, {@code false} otherwise
     * @throws IllegalArgumentException if the seat number is outside the valid range
     */
    public boolean isSeatAvailable(int seatNumber) {
        int index = seatNumber - 1;

        if (!isValidSeatNumber(seatNumber)) {
            throw new IllegalArgumentException(
                    "Invalid seat number"
            );
        }

        return seats.get(index) == EMPTY;
    }

    /**
     * Returns a list of all currently available seat numbers.
     *
     * @return list of 1-based seat numbers that are available for booking
     * @see #bookSeat()
     */
    public List<Integer> getAvailableSeats() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i) == EMPTY) {
                result.add(i + 1);
            }
        }
        return result;
    }

    /**
     * Cancels an existing booking for the given 1-based seat number.
     *
     * @param seatNumber the seat number whose booking should be cancelled
     * @return {@code true} if the booking was cancelled, {@code false} if the seat was not booked
     * @throws IllegalArgumentException if the seat number is outside the valid range
     * @see #bookSeat(int)
     */
    public boolean cancelSeat(int seatNumber) {

        if (!isValidSeatNumber(seatNumber)) {
            throw new IllegalArgumentException("Invalid seat number");
        }

        Object lock = seatLocks.computeIfAbsent(seatNumber, key -> new Object());
        int index = seatNumber - 1;

        synchronized (lock) {
            if (seats.get(index) == EMPTY) {
                return false;
            }

            seats.set(index, EMPTY);
            return true;
        }
    }

    /**
     * Validates whether the given seat number is within the allowed range.
     *
     * @param seatNumber the seat number to validate
     * @return {@code true} if the seat number is between 1 and the total number of seats (inclusive),
     * {@code false} otherwise
     */
    private boolean isValidSeatNumber(int seatNumber) {
        return seatNumber >= 1 && seatNumber <= seats.size();
    }
}
