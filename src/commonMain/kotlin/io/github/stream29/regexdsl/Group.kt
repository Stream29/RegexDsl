package io.github.stream29.regexdsl

import kotlin.jvm.JvmInline

/**
 * Represents a reference to a capturing group by index.
 * 
 * This class is used to create backreferences to previously captured groups in the pattern.
 * For example, `\1` refers to the first capturing group.
 * 
 * @property index The index of the capturing group to reference.
 */
@JvmInline
public value class IndexedGroupRef(
    public val index: Int,
) : RegexElement {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append("\\$index")
    }
}

/**
 * Represents a reference to a capturing group by name.
 * 
 * This class is used to create backreferences to previously captured named groups in the pattern.
 * For example, `\k<name>` refers to the capturing group named "name".
 * 
 * @property name The name of the capturing group to reference.
 */
@JvmInline
public value class NamedGroupRef(
    public val name: String,
) : RegexElement {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append("\\k<$name>")
    }
}
