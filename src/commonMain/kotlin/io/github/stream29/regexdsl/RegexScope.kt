@file:OptIn(InternalRegexDslApi::class)

package io.github.stream29.regexdsl

import io.github.stream29.regexdsl.RegexElement.Single

/**
 * The main class for building regular expressions using the DSL.
 * 
 * This class serves as the receiver for the [buildRegex] function and provides
 * methods for adding various regex elements to the pattern.
 */
@RegexDsl
public class RegexScope : RegexElement {
    @PublishedApi
    internal val components: MutableList<RegexElement> = mutableListOf()
    private var currentIndex = 0
    @PublishedApi
    internal fun nextIndex(): Int {
        currentIndex++
        return currentIndex
    }

    override fun appendTo(stringBuilder: StringBuilder) {
        components.forEach { it.appendTo(stringBuilder) }
    }
}

/**
 * Adds the start-of-string anchor to the pattern.
 * 
 * This matches the beginning of the string. Equivalent to the `^` metacharacter in regex.
 */
public fun RegexScope.matchStringBegin() {
    components += Single("^")
}

/**
 * Adds an alternation operator to the pattern.
 * 
 * This creates an "OR" condition between two patterns. Equivalent to the `|` operator in regex.
 */
public fun RegexScope.insertOr() {
    components += Single("|")
}

/**
 * Adds the end-of-string anchor to the pattern.
 * 
 * This matches the end of the string. Equivalent to the `$` metacharacter in regex.
 */
public fun RegexScope.matchStringEnd() {
    components += Single("$")
}

/**
 * Adds a word boundary assertion to the pattern.
 * 
 * This matches a position where a word character is followed or preceded by a non-word character.
 * Equivalent to the `\b` metacharacter in regex.
 */
public fun RegexScope.matchWordBoundary() {
    components += Single("\\b")
}

/**
 * Adds a non-word boundary assertion to the pattern.
 * 
 * This matches a position where the previous and next characters are either both word characters
 * or both non-word characters. Equivalent to the `\B` metacharacter in regex.
 */
public fun RegexScope.matchNonWordBoundary() {
    components += Single("\\B")
}

/**
 * Adds a character to the pattern, with an optional quantifier.
 * 
 * The character will be automatically escaped if it has special meaning in regex.
 * 
 * @param unescaped The character to match.
 * @param quantifier Optional quantifier to apply to the character.
 */
public fun RegexScope.match(unescaped: Char, quantifier: Quantifier? = null) {
    components += Single(unescaped.toString().escaped())
    if (quantifier != null)
        components += Single(quantifier.value)
}

/**
 * Adds a string to the pattern, with an optional quantifier.
 * 
 * The string will be automatically escaped if it contains characters with special meaning in regex.
 * If a quantifier is provided and the string is longer than one character, the string will be
 * wrapped in a capturing group.
 * 
 * @param unescaped The string to match.
 * @param quantifier Optional quantifier to apply to the string.
 */
public fun RegexScope.match(unescaped: String, quantifier: Quantifier? = null) {
    when {
        unescaped.length == 1 -> match(unescaped[0], quantifier)
        quantifier != null -> matchIndexedGroup(quantifier) { match(unescaped) }
        else -> components += Single(unescaped.escaped())
    }
}

/**
 * Adds a regex element to the pattern, with an optional quantifier.
 * 
 * @param regexElement The regex element to add.
 * @param quantifier Optional quantifier to apply to the element.
 */
public fun RegexScope.match(regexElement: RegexElement, quantifier: Quantifier? = null) {
    components += regexElement
    if (quantifier != null)
        components += Single(quantifier.value)
}

/**
 * Adds a character set to the pattern, with an optional quantifier.
 * 
 * A character set matches any one character from the set. For example, `[abc]` matches 'a', 'b', or 'c'.
 * 
 * @param quantifier Optional quantifier to apply to the character set.
 * @param block A lambda with [CharacterSet] as the receiver that defines the character set.
 */
public inline fun RegexScope.matchCharacterSet(quantifier: Quantifier? = null, block: CharacterSet.() -> Unit) {
    match(CharacterSet().apply(block), quantifier)
}

/**
 * Adds a negated character set to the pattern, with an optional quantifier.
 * 
 * A negated character set matches any one character NOT in the set. For example, `[^abc]` matches any character except 'a', 'b', or 'c'.
 * 
 * @param quantifier Optional quantifier to apply to the character set.
 * @param block A lambda with [NegatedCharacterSet] as the receiver that defines the character set.
 */
public inline fun RegexScope.matchNegatedCharacterSet(quantifier: Quantifier? = null, block: NegatedCharacterSet.() -> Unit) {
    match(NegatedCharacterSet().apply(block), quantifier)
}

/**
 * Adds a positive lookahead group to the regular expression.
 * A lookahead group matches a group of characters only if they are followed by a certain pattern,
 * without including that pattern in the match result.
 *
 * @param block A lambda with [RegexScope] as the receiver that defines the lookahead pattern.
 */
public inline fun RegexScope.lookahead(block: RegexScope.() -> Unit) {
    components += Single("(?=")
    components += RegexScope().apply(block)
    components += Single(")")
}

/**
 * Adds a negative lookahead group to the regular expression.
 * A lookahead group matches a group of characters only if they are NOT followed by a certain pattern,
 * without including that pattern in the match result.
 *
 * @param block A lambda with [RegexScope] as the receiver that defines the lookahead pattern.
 */
public inline fun RegexScope.lookaheadNegative(block: RegexScope.() -> Unit) {
    components += Single("(?!")
    components += RegexScope().apply(block)
    components += Single(")")
}

/**
 * Adds a positive lookbehind group to the regular expression.
 * A lookbehind group matches a group of characters only if they are preceded by a certain pattern,
 * without including that pattern in the match result.
 *
 * Please mention that variable-length lookbehind may be not supported for some regex engine.
 *
 * @param block A lambda with [RegexScope] as the receiver that defines the lookbehind pattern.
 */
public inline fun RegexScope.lookbehind(block: RegexScope.() -> Unit) {
    components += Single("(?<=")
    components += RegexScope().apply(block)
    components += Single(")")
}

/**
 * Adds a negative lookbehind group to the regular expression.
 * A lookbehind group matches a group of characters only if they are NOT preceded by a certain pattern,
 * without including that pattern in the match result.
 *
 * Please mention that variable-length lookbehind may be not supported for some regex engine.
 *
 * @param block A lambda with [RegexScope] as the receiver that defines the lookbehind pattern.
*/
public inline fun RegexScope.lookbehindNegative(block: RegexScope.() -> Unit) {
    components += Single("(?<!")
    components += RegexScope().apply(block)
    components += Single(")")
}

/**
 * Adds a non-capturing group to the pattern, with an optional quantifier.
 * 
 * A non-capturing group groups the contained elements without creating a capturing group.
 * Equivalent to the `(?:...)` syntax in regex.
 * 
 * @param quantifier Optional quantifier to apply to the group.
 * @param block A lambda with [RegexScope] as the receiver that defines the group content.
 */
public inline fun RegexScope.matchUncapturedGroup(quantifier: Quantifier? = null, block: RegexScope.() -> Unit) {
    components += Single("(?:")
    components += RegexScope().apply(block)
    components += Single(")")
    if (quantifier != null)
        components += Single(quantifier.value)
}

@PublishedApi
internal val identifierRegex: Regex = Regex("[a-zA-Z][a-zA-Z0-9_-]*")

/**
 * Adds a named capturing group to the pattern, with an optional quantifier.
 * 
 * A named capturing group allows you to refer to the captured content by name.
 * Equivalent to the `(?<name>...)` syntax in regex.
 * 
 * @param name The name of the capturing group.
 * @param quantifier Optional quantifier to apply to the group.
 * @param block A lambda with [RegexScope] as the receiver that defines the group content.
 * @return A [NamedGroupRef] that can be used to reference this group later in the pattern.
 * @throws IllegalArgumentException If the name is not a valid identifier.
 */
public inline fun RegexScope.matchNamedGroup(
    name: String,
    quantifier: Quantifier? = null,
    block: RegexScope.() -> Unit
): NamedGroupRef {
    require(identifierRegex.matches(name)) { "Group name must be a valid identifier: $name" }
    nextIndex()
    components += Single("(?<$name>")
    components += RegexScope().apply(block)
    components += Single(")")
    if (quantifier != null)
        components += Single(quantifier.value)
    return NamedGroupRef(name)
}

/**
 * Adds a numbered capturing group to the pattern, with an optional quantifier.
 * 
 * A numbered capturing group allows you to refer to the captured content by index.
 * Equivalent to the `(...)` syntax in regex.
 * 
 * @param quantifier Optional quantifier to apply to the group.
 * @param block A lambda with [RegexScope] as the receiver that defines the group content.
 * @return An [IndexedGroupRef] that can be used to reference this group later in the pattern.
 */
public inline fun RegexScope.matchIndexedGroup(quantifier: Quantifier? = null, block: RegexScope.() -> Unit): IndexedGroupRef {
    components += Single("(")
    components += RegexScope().apply(block)
    components += Single(")")
    if (quantifier != null)
        components += Single(quantifier.value)
    return IndexedGroupRef(nextIndex())
}
