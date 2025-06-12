@file:OptIn(InternalRegexDslApi::class)

package io.github.stream29.regexdsl

import io.github.stream29.regexdsl.RegexElement.Single

/**
 * Base class for character sets in regular expressions.
 * 
 * Character sets are used to match any one of a set of characters.
 */
public sealed class AbstractCharacterSet: RegexElement {
    internal val elements: MutableList<RegexElement> = mutableListOf()
}

/**
 * Represents a positive character set in a regular expression.
 * 
 * A positive character set matches any one character that is contained in the set.
 * For example, `[abc]` matches 'a', 'b', or 'c'.
 */
public class CharacterSet : AbstractCharacterSet() {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append('[')
        elements.forEach { it.appendTo(stringBuilder) }
        stringBuilder.append(']')
    }
}

/**
 * Represents a negated character set in a regular expression.
 * 
 * A negated character set matches any one character that is NOT contained in the set.
 * For example, `[^abc]` matches any character except 'a', 'b', or 'c'.
 */
public class NegatedCharacterSet : AbstractCharacterSet() {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append("[^")
        elements.forEach { it.appendTo(stringBuilder) }
        stringBuilder.append(']')
    }
}

/**
 * Adds a character range to the character set.
 * 
 * @param from The first character in the range.
 * @param to The last character in the range.
 */
public fun AbstractCharacterSet.range(from: Char, to: Char) {
    elements += Single("$from-$to")
}

/**
 * Adds a single character to the character set.
 * 
 * @param char The character to add.
 */
public fun AbstractCharacterSet.add(char: Char) {
    elements += Single("$char")
}

/**
 * Adds a metacharacter to the character set.
 * 
 * @param metaCharacter The metacharacter to add.
 */
public fun AbstractCharacterSet.add(metaCharacter: MetaCharacter) {
    elements += metaCharacter
}
