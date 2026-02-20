package org.motadata.exercises.Day9;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {

    @Test
    void testApplyDiscount() {
        Discount d = new Discount("SAVE10", 10);

        double result = d.apply(1000);

        assertEquals(900, result);
        assertEquals("SAVE10", d.code());
        assertTrue(d.toString().contains("SAVE10"));
    }
}
