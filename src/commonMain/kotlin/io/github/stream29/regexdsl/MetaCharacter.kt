@file:Suppress("UnusedReceiverParameter")

package io.github.stream29.regexdsl

import kotlin.jvm.JvmInline

/**
 * Represents a metacharacter in a regular expression.
 * 
 * Metacharacters are special characters that have a predefined meaning in regex syntax.
 * 
 * @property value The string representation of the metacharacter in regex syntax.
 */
@JvmInline
public value class MetaCharacter internal constructor(public val value: String) : RegexElement {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append(value)
    }
}

/**
 * Matches any single character except line terminators.
 * 
 * Equivalent to the `.` metacharacter in regex.
 */
public val RegexScope.anyChar: MetaCharacter get() = MetaCharacter(".")

/**
 * Matches any whitespace character.
 * 
 * Equivalent to the `\s` metacharacter in regex.
 */
public val RegexScope.spaceChar: MetaCharacter get() = MetaCharacter("\\s")

/**
 * Matches any non-whitespace character.
 * 
 * Equivalent to the `\S` metacharacter in regex.
 */
public val RegexScope.nonSpaceChar: MetaCharacter get() = MetaCharacter("\\S")

/**
 * Matches any word character (alphanumeric or underscore).
 * 
 * Equivalent to the `\w` metacharacter in regex.
 */
public val RegexScope.wordChar: MetaCharacter get() = MetaCharacter("\\w")

/**
 * Matches any non-word character.
 * 
 * Equivalent to the `\W` metacharacter in regex.
 */
public val RegexScope.nonWordChar: MetaCharacter get() = MetaCharacter("\\W")

/**
 * Matches any digit character.
 * 
 * Equivalent to the `\d` metacharacter in regex.
 */
public val RegexScope.digitChar: MetaCharacter get() = MetaCharacter("\\d")

/**
 * Matches any non-digit character.
 * 
 * Equivalent to the `\D` metacharacter in regex.
 */
public val RegexScope.nonDigitChar: MetaCharacter get() = MetaCharacter("\\D")
