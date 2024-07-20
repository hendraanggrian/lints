package com.hanggrian.rulebook.codenarc

import com.hanggrian.rulebook.codenarc.internals.Messages
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.ast.expr.MethodCallExpression
import org.codenarc.rule.AbstractAstVisitor

/**
 * [See wiki](https://github.com/hanggrian/rulebook/wiki/Rules/#qualifier-consistency)
 */
public class QualifierConsistencyRule : Rule() {
    override fun getName(): String = "QualifierConsistency"

    override fun getAstVisitorClass(): Class<*> = Visitor::class.java

    internal companion object {
        const val MSG = "qualifier.consistency"
    }

    public class Visitor : AbstractAstVisitor() {
        private val importPaths = mutableSetOf<String>()
        private val targetNodes = mutableSetOf<ASTNode>()

        override fun visitImports(node: ModuleNode) {
            super.visitImports(node)

            // keep import list
            node.imports.forEach { importPaths += it.type.name }
        }

        override fun visitField(node: FieldNode) {
            super.visitField(node)

            // checks for violation
            process(node, node.type.name)
        }

        override fun visitMethodEx(node: MethodNode) {
            super.visitMethodEx(node)

            // checks for violation
            process(node, node.returnType.name)
            node.parameters.forEach { process(it, it.type.name) }
        }

        override fun visitMethodCallExpression(node: MethodCallExpression) {
            super.visitMethodCallExpression(node)

            // checks for violation
            val `class` = node.objectExpression
            process(`class`, `class`.text)
            process(`class`, `class`.text + '.' + node.method.text)
        }

        private fun process(node: ASTNode, text: String) {
            node
                .takeIf { it !in targetNodes && text in importPaths }
                ?: return
            targetNodes += node
            addViolation(node, Messages[MSG])
        }
    }
}
