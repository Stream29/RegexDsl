package io.github.stream29.regexdsl

import kotlin.jvm.JvmInline

/**
 * Base interface for all elements that can be part of a regular expression.
 * 
 * This interface is implemented by all components of the RegexDSL, including
 * metacharacters, character sets, groups, and other regex elements.
 */
public sealed interface RegexElement {
    /**
     * Appends the string representation of this element to the given StringBuilder.
     * 
     * @param stringBuilder The StringBuilder to append to.
     */
    public fun appendTo(stringBuilder: StringBuilder)

    /**
     * A simple implementation of RegexElement that wraps a string.
     * 
     * This is used internally by the DSL to represent simple regex elements.
     * 
     * @property string The string representation of the element.
     */
    @InternalRegexDslApi
    @JvmInline
    public value class Single(internal val string: String) : RegexElement {
        override fun appendTo(stringBuilder: StringBuilder) {
            stringBuilder.append(string)
        }
    }
}
