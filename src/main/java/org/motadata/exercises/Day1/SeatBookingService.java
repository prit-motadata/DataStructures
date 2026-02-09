package org.motadata.exercises.Day1;

import org.motadata.datastructures.array.ArrayFixedSize;

import java.util.ArrayList;
import java.util.List;

public class SeatBookingService {

    private static final int EMPTY = 0;
    private static final int BOOKED = 1;

    private final ArrayFixedSize<Integer> seats;

    public SeatBookingService(int totalSeats) {
        seats = new ArrayFixedSize<>(totalSeats);
        seats.fill(EMPTY); // initialize all seats as empty
    }

    // Book a seat by seat number (1-based)
    public boolean bookSeat(int seatNumber) {
        int index = seatNumber - 1;

        if (!isValidSeatNumber(seatNumber)) {
            throw new IllegalArgumentException(
                    "Seat number must be between 1 and " + seats.size()
            );
        }

        if (seats.get(index) == BOOKED) {
            return false; // already booked
        }

        seats.set(index, BOOKED);
        return true;
    }

    // Check if a seat is available
    public boolean isSeatAvailable(int seatNumber) {
        int index = seatNumber - 1;

        if (!isValidSeatNumber(seatNumber)) {
            throw new IllegalArgumentException(
                    "Invalid seat number"
            );
        }

        return seats.get(index) == EMPTY;
    }

    // Get available seats
    public List<Integer> getAvailableSeats() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i) == EMPTY) {
                result.add(i + 1);
            }
        }
        return result;
    }

    // Cancel a booking
    public boolean cancelSeat(int seatNumber) {
        int index = seatNumber - 1;

        if (!isValidSeatNumber(seatNumber)) {
            throw new IllegalArgumentException(
                    "Invalid seat number"
            );
        }

        if (seats.get(index) == EMPTY) {
            return false; // already empty
        }

        seats.set(index, EMPTY);
        return true;
    }

    // Helper validation method
    private boolean isValidSeatNumber(int seatNumber) {
        return seatNumber >= 1 && seatNumber <= seats.size();
    }
}
