package com.hanggrian.rulebook.ktlint

import com.hanggrian.rulebook.ktlint.ElseFlatteningRule.Companion.MSG
import com.hanggrian.rulebook.ktlint.internals.Messages
import com.pinterest.ktlint.test.KtLintAssertThat.Companion.assertThatRule
import kotlin.test.Test

class ElseFlatteningRuleTest {
    private val assertThatCode = assertThatRule { ElseFlatteningRule() }

    @Test
    fun `Rule properties`() = ElseFlatteningRule().assertProperties()

    @Test
    fun `No throw or return in if`() =
        assertThatCode(
            """
            fun foo() {
                if (true) {
                    baz()
                } else if (false) {
                    baz()
                } else {
                    baz()
                }
            }
            """.trimIndent(),
        ).hasNoLintViolations()

    @Test
    fun `Lift else when if has return`() =
        assertThatCode(
            """
            fun foo() {
                if (true) {
                    throw Exception()
                } else if (false) {
                    return
                } else {
                    baz()
                }
            }
            """.trimIndent(),
        ).hasLintViolationWithoutAutoCorrect(6, 7, Messages[MSG])

    @Test
    fun `Skip if not all if blocks have jump statement`() =
        assertThatCode(
            """
            fun foo() {
                if (true) {
                    return
                } else if (false) {
                    baz()
                } else {
                    baz()
                }
            }
            """.trimIndent(),
        ).hasNoLintViolations()

    @Test
    fun `Consider if-else without blocks`() =
        assertThatCode(
            """
            fun foo() {
                if (true) throw Exception()
                else if (false) return
                else baz()
            }
            """.trimIndent(),
        ).hasLintViolationWithoutAutoCorrect(4, 5, Messages[MSG])
}
