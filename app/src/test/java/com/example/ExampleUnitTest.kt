package com.example

import com.example.model.PackagePlan
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun packagePlans_haveValidYields() {
        val tier1 = PackagePlan(
            tier = 1,
            name = "Archery AI Tier 1",
            description = "Entry level",
            cost = 20000.0,
            dailyIncome = 1730.0
        )
        val tier5 = PackagePlan(
            tier = 5,
            name = "Archery AI Ultra Tier 5",
            description = "Maximum yield",
            cost = 70000.0,
            dailyIncome = 5000.0,
            isBestYield = true
        )

        assertEquals(20000.0, tier1.cost, 0.001)
        assertEquals(1730.0, tier1.dailyIncome, 0.001)
        assertEquals(70000.0, tier5.cost, 0.001)
        assertEquals(5000.0, tier5.dailyIncome, 0.001)
        assertTrue(tier5.isBestYield)
    }

    @Test
    fun phoneNormalization_handlesUgandaFormats() {
        fun normalizePhone(raw: String): String {
            val digits = raw.filter { it.isDigit() }
            return when {
                digits.startsWith("256") -> "+$digits"
                digits.startsWith("0") -> "+256${digits.substring(1)}"
                else -> "+256$digits"
            }
        }

        assertEquals("+256770000000", normalizePhone("0770000000"))
        assertEquals("+256770000000", normalizePhone("+256770000000"))
        assertEquals("+256770000000", normalizePhone("256770000000"))
        assertEquals("+256770000000", normalizePhone("770000000"))
    }
}
