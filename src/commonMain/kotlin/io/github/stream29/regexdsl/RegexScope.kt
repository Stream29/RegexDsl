@file:OptIn(InternalRegexDslApi::class)

package io.github.stream29.regexdsl

import io.github.stream29.regexdsl.RegexElement.Single

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

public fun RegexScope.matchStringBegin() {
    components += Single("^")
}

public fun RegexScope.insertOr() {
    components += Single("|")
}

public fun RegexScope.matchStringEnd() {
    components += Single("$")
}

public fun RegexScope.matchWordBoundary() {
    components += Single("\\b")
}

public fun RegexScope.matchNonWordBoundary() {
    components += Single("\\B")
}

public fun RegexScope.match(unescaped: String, quantifier: Quantifier? = null) {
    if (quantifier != null)
        matchIndexedGroup(quantifier) { match(unescaped) }
    else
        components += Single(unescaped.escaped())
}

public fun RegexScope.match(regexElement: RegexElement, quantifier: Quantifier? = null) {
    components += regexElement
    if (quantifier != null)
        components += Single(quantifier.value)
}

public inline fun RegexScope.matchCharacterSet(quantifier: Quantifier? = null, block: CharacterSet.() -> Unit) {
    match(CharacterSet().apply(block), quantifier)
}

public inline fun RegexScope.matchNegatedCharacterSet(quantifier: Quantifier? = null, block: NegatedCharacterSet.() -> Unit) {
    match(NegatedCharacterSet().apply(block), quantifier)
}

public inline fun RegexScope.matchUncapturedGroup(quantifier: Quantifier? = null, block: RegexScope.() -> Unit) {
    components += Single("(?:")
    components += RegexScope().apply(block)
    components += Single(")")
    if (quantifier != null)
        components += Single(quantifier.value)
}

@PublishedApi
internal val identifierRegex: Regex = Regex("[a-zA-Z][a-zA-Z0-9_-]*")

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

public inline fun RegexScope.matchIndexedGroup(quantifier: Quantifier? = null, block: RegexScope.() -> Unit): IndexedGroupRef {
    components += Single("(")
    components += RegexScope().apply(block)
    components += Single(")")
    if (quantifier != null)
        components += Single(quantifier.value)
    return IndexedGroupRef(nextIndex())
}