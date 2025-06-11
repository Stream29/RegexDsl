package io.github.stream29.regexdsl

internal val escapeMap = mapOf(
    '.' to "\\.",
    '\\' to "\\\\",
    '^' to "\\^",
    '$' to "\\$",
    '[' to "\\[",
    ']' to "\\]",
    '(' to "\\(",
    ')' to "\\)",
    '*' to "\\*",
    '+' to "\\+",
    '?' to "\\?",
    '{' to "\\{",
    '}' to "\\}",
    '|' to "\\|",
    '\n' to "\\n",
    '\r' to "\\r",
    '\t' to "\\t"
)

internal fun String.escaped(): String {
    if (none { escapeMap.containsKey(it) }) // no escape needed, fast path
        return this
    return buildString {
        for (char in this@escaped) {
            val escaped = escapeMap[char]
            if (escaped != null) {
                append(escaped)
            } else {
                append(char)
            }
        }
    }
}
