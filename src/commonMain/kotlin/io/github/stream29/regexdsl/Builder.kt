package io.github.stream29.regexdsl

/**
 * Builds a [Regex] object using the RegexDSL.
 * 
 * This is the main entry point for creating regular expressions with the DSL.
 * It provides a type-safe way to build complex regular expressions.
 * 
 * @param block A lambda with [RegexScope] as the receiver that defines the regular expression.
 * @return A [Regex] object representing the defined pattern.
 * 
 * @sample io.github.stream29.regexdsl.BuilderTest.testBuildSimpleRegex
 * @sample io.github.stream29.regexdsl.BuilderTest.testBuildComplexRegex
 */
public inline fun buildRegex(block: RegexScope.() -> Unit): Regex {
    val sb = StringBuilder()
    RegexScope().apply(block).appendTo(sb)
    return sb.toString().toRegex()
}
