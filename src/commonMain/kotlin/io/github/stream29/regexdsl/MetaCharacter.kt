@file:Suppress("UnusedReceiverParameter")

package io.github.stream29.regexdsl

import kotlin.jvm.JvmInline

@JvmInline
public value class MetaCharacter internal constructor(public val value: String) : RegexElement {
    override fun appendTo(stringBuilder: StringBuilder) {
        stringBuilder.append(value)
    }
}

public val RegexScope.anyChar: MetaCharacter get() = MetaCharacter(".")
public val RegexScope.spaceChar: MetaCharacter get() = MetaCharacter("\\s")
public val RegexScope.nonSpaceChar: MetaCharacter get() = MetaCharacter("\\S")
public val RegexScope.wordChar: MetaCharacter get() = MetaCharacter("\\w")
public val RegexScope.nonWordChar: MetaCharacter get() = MetaCharacter("\\W")
public val RegexScope.digitChar: MetaCharacter get() = MetaCharacter("\\d")
public val RegexScope.nonDigitChar: MetaCharacter get() = MetaCharacter("\\D")