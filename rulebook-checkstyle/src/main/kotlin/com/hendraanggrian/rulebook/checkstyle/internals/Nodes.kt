package com.hendraanggrian.rulebook.checkstyle.internals

import com.puppycrawl.tools.checkstyle.api.DetailAST
import com.puppycrawl.tools.checkstyle.api.DetailNode
import com.puppycrawl.tools.checkstyle.api.JavadocTokenTypes.EOF

internal fun DetailNode.find(type: Int): DetailNode? = children.firstOrNull { it.type == type }

internal operator fun DetailNode.contains(type: Int): Boolean = children.any { it.type == type }

internal operator fun DetailAST.contains(type: Int): Boolean = findFirstToken(type) != null

/** In Checkstyle Javadoc, nodes with children do not have text any may contain '<EOF>'. */
internal fun DetailNode.joinText(separator: String = "", excludeType: Int = EOF): String {
    val result =
        when {
            children.isEmpty() -> text
            else ->
                buildString {
                    children.forEach {
                        if (it.type != excludeType) {
                            append(it.joinText(separator, excludeType))
                            append(separator)
                        }
                    }
                }
        }
    if (result.endsWith(separator)) {
        return result.substring(0, result.length - separator.length)
    }
    return result
}

/** In Checkstyle, nodes with children do not have text. */
internal fun DetailAST.joinText(separator: String = "", excludeType: Int = 0): String {
    val children = children().toList()
    val result =
        when {
            children.isEmpty() -> text
            else ->
                buildString {
                    for (child in children) {
                        if (child.type == excludeType) {
                            continue
                        }
                        append(child.joinText(separator, excludeType))
                        append(separator)
                    }
                }
        }
    if (result.endsWith(separator)) {
        return result.substring(0, result.length - separator.length)
    }
    return result
}

internal fun DetailAST.children(): Sequence<DetailAST> =
    generateSequence(firstChild) { node -> node.nextSibling }

internal fun DetailAST.siblings(forward: Boolean = true): Sequence<DetailAST> =
    when {
        forward -> generateSequence(nextSibling) { it.nextSibling }
        else -> generateSequence(previousSibling) { it.previousSibling }
    }
