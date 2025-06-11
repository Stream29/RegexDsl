package io.github.stream29.regexdsl

import kotlin.jvm.JvmInline

@JvmInline
public value class IndexedGroupRef(
    public val index: Int,
) : RegexElement {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append("\\$index")
    }
}

@JvmInline
public value class NamedGroupRef(
    public val name: String,
) : RegexElement {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append("\\k<$name>")
    }
}