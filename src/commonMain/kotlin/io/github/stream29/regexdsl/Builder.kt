package io.github.stream29.regexdsl

/**
 * Builds a [Regex] object using the RegexDSL.
 * 
 * This is the main entry point for creating regular expressions with the DSL.
 * It provides a type-safe way to build complex regular expressions.
 * 
 * @param block A lambda with [RegexScope] as the receiver that defines the regular expression.
 * @return A [Regex] object representing the defined pattern.
 */
public inline fun buildRegex(block: RegexScope.() -> Unit): Regex = Regex(buildRegexString(block))

/**
 * Builds a string representation of a [Regex] object using the RegexDSL.
 *
 * This is a convenience function for building regular expressions without creating a [Regex] object.
 * It is useful for passing regular expressions to APIs that expect a string representation.
*/
public inline fun buildRegexString(block: RegexScope.() -> Unit): String {
    val sb = StringBuilder()
    RegexScope().apply(block).appendTo(sb)
    return sb.toString()
}
