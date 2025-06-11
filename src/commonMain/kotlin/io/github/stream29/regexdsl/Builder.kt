package io.github.stream29.regexdsl

public inline fun buildRegex(block: RegexScope.() -> Unit): Regex {
    val sb = StringBuilder()
    RegexScope().apply(block).appendTo(sb)
    return sb.toString().toRegex()
}