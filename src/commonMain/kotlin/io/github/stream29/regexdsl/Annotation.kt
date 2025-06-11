package io.github.stream29.regexdsl

@DslMarker
public annotation class RegexDsl

@RequiresOptIn(
    message = "This API is intended to be only used by the library itself.",
    level = RequiresOptIn.Level.WARNING
)
public annotation class InternalRegexDslApi