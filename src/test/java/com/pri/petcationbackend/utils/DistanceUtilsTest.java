package com.pri.petcationbackend.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistanceUtilsTest {

    @Test
    void isTwoPointsInMaxDistance_shouldReturnTrue() {
        // when
        var result = DistanceUtils.isTwoPointsInMaxDistance(50, 3.002, 2.002, 3.003, 2.003);

        // then
        assertTrue(result);
    }

    @Test
    void isTwoPointsInMaxDistance_shouldReturnFalse() {
        // when
        var result = DistanceUtils.isTwoPointsInMaxDistance(5, 3L, 2L, 4, 8);

        // then
        assertFalse(result);
    }

}