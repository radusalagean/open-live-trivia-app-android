package com.busytrack.openlivetrivia.extension

import org.junit.Assert.*
import org.junit.Test

class DoubleExtensionKtTest {

    @Test
    fun fourDecimalsInput_cutToTwoDecimals_resultIsRoundedUp() {
        val result = 4.3568.cutTo(2)

        assertEquals(4.36, result, 0.0)
    }

    @Test
    fun fourDecimalsInput_cutToTwoDecimals_resultIsRoundedDown() {
        val result = 4.3548.cutTo(2)

        assertEquals(4.35, result, 0.0)
    }

    @Test
    fun fourDecimalsInput_cutToFiveDecimals_correctAnswer() {
        val result = 4.3548.cutTo(5)

        assertEquals(4.3548, result, 0.0)
    }

    @Test
    fun fourDecimalsInput_cutToZeroDecimals_correctAnswer() {
        val result = 4.3548.cutTo(0)

        assertEquals(4.0, result, 0.0)
    }

    @Test
    fun zeroDecimalsInput_cutToOneDecimal_correctAnswer() {
        val result = 4.0.cutTo(1)

        assertEquals(4.0, result, 0.0)
    }
}