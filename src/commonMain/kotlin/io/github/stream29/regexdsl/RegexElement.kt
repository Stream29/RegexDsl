package io.github.stream29.regexdsl

import kotlin.jvm.JvmInline

public sealed interface RegexElement {
    public fun appendTo(stringBuilder: StringBuilder)

    @InternalRegexDslApi
    @JvmInline
    public value class Single(internal val string: String) : RegexElement {
        override fun appendTo(stringBuilder: StringBuilder) {
            stringBuilder.append(string)
        }
    }
}