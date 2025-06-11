@file:OptIn(InternalRegexDslApi::class)

package io.github.stream29.regexdsl

import io.github.stream29.regexdsl.RegexElement.Single

public sealed class AbstractCharacterSet: RegexElement {
    internal val elements: MutableList<RegexElement> = mutableListOf()
}

public class CharacterSet : AbstractCharacterSet() {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append('[')
        elements.forEach { it.appendTo(stringBuilder) }
        stringBuilder.append(']')
    }
}

public class NegatedCharacterSet : AbstractCharacterSet() {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append("[^")
        elements.forEach { it.appendTo(stringBuilder) }
        stringBuilder.append(']')
    }
}

public fun AbstractCharacterSet.range(from: Char, to: Char) {
    elements += Single("$from-$to")
}

public fun AbstractCharacterSet.add(char: Char) {
    elements += Single("$char")
}

public fun AbstractCharacterSet.add(metaCharacter: MetaCharacter) {
    elements += metaCharacter
}