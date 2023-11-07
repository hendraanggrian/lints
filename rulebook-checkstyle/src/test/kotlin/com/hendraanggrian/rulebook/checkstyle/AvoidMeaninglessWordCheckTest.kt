package com.hendraanggrian.rulebook.checkstyle

import kotlin.test.Test
import kotlin.test.assertEquals

class AvoidMeaninglessWordCheckTest {
    private val checker = prepareChecker(AvoidMeaninglessWordCheck::class)

    @Test
    fun `Meaningful class names`() =
        assertEquals(0, checker.process(prepareFiles("AvoidMeaninglessWord1")))

    @Test
    fun `Meaningless class names`() =
        assertEquals(4, checker.process(prepareFiles("AvoidMeaninglessWord2")))

    @Test
    fun `Violating both ends`() =
        assertEquals(2, checker.process(prepareFiles("AvoidMeaninglessWord3")))
}
