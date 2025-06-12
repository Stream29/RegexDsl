package io.github.stream29.regexdsl

/**
 * Marks a type as being part of the RegexDSL.
 * 
 * This annotation is used to prevent receivers from outer DSL scopes
 * from being implicitly available in nested DSL scopes, which helps
 * avoid confusion and errors.
 */
@DslMarker
public annotation class RegexDsl

/**
 * Marks an API as internal to the RegexDSL library.
 * 
 * APIs marked with this annotation are not intended to be used directly
 * by library consumers and may change without notice.
 */
@RequiresOptIn(
    message = "This API is intended to be only used by the library itself.",
    level = RequiresOptIn.Level.WARNING
)
public annotation class InternalRegexDslApi
