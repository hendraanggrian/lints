package com.hanggrian.rulebook.ktlint

import com.hanggrian.rulebook.ktlint.EmptyCodeBlockUnwrappingRule.Companion.MSG
import com.hanggrian.rulebook.ktlint.internals.Messages
import com.pinterest.ktlint.test.KtLintAssertThat.Companion.assertThatRule
import com.pinterest.ktlint.test.LintViolation
import kotlin.test.Test

class EmptyCodeBlockUnwrappingRuleTest {
    private val assertThatCode = assertThatRule { EmptyCodeBlockUnwrappingRule() }

    @Test
    fun `Rule properties`() = EmptyCodeBlockUnwrappingRule().assertProperties()

    @Test
    fun `Wrapped empty block`() =
        assertThatCode(
            """
            class Foo {}

            fun bar() {}
            """.trimIndent(),
        ).hasNoLintViolations()

    @Test
    fun `Unwrapped empty block`() =
        assertThatCode(
            """
            class Foo { }

            fun bar() {
            }
            """.trimIndent(),
        ).hasLintViolationsWithoutAutoCorrect(
            LintViolation(1, 12, Messages[MSG]),
            LintViolation(3, 12, Messages[MSG]),
        )

    @Test
    fun `Control flows with multi-blocks`() =
        assertThatCode(
            """
            fun foo() {
                try {
                } catch (e: Exception) {
                }

                if (true) {
                } else if (false) {
                } else {
                }
            }
            """.trimIndent(),
        ).hasNoLintViolations()

    @Test
    fun `Allow code block with comment`() =
        assertThatCode(
            """
            class Foo {
                // Lorem ipsum.
            }
            """.trimIndent(),
        ).hasNoLintViolations()

    @Test
    fun `Code block in parameter default value declaration`() =
        assertThatCode(
            """
            class Foo(
                val bar: (Int) -> Unit = {},
                val baz: (Int) -> Unit = { },
            )
            """.trimIndent(),
        ).hasLintViolationWithoutAutoCorrect(3, 31, Messages[MSG])
}
