# RegexDsl

A Kotlin Multiplatform library that provides a DSL for writing regular expressions in a more readable and maintainable way.

## Features

- Type-safe and expressive API for building regex patterns
- Automatic escaping of special characters
- Support for all common regex operations:
  - String matching
  - Character sets and ranges
  - Groups (captured, uncaptured, named)
  - Quantifiers (exactly, at least, at most, etc.)
  - Lookahead and lookbehind assertions
  - Boundaries (string begin/end, word boundaries)
  - Meta characters (digit, word, space, etc.)
- Multiplatform support (JVM, JS, Native, Wasm)

## Installation

### Gradle 

```kotlin
implementation("io.github.stream29:regexdsl:0.0.1")
```

## Usage

### Basic Example

```kotlin
// Create a simple regex pattern
val regex = buildRegex {
    match("hello")
    match(spaceChar)
    match("world")
}

// Use the regex
val text = "hello world"
val matches = regex.matches(text) // true
```

### Phone Number Pattern

```kotlin
val phoneRegex = buildRegex {
    match(digitChar, Quantifier.exactly(3))
    match("-")
    match(digitChar, Quantifier.exactly(3))
    match("-")
    match(digitChar, Quantifier.exactly(4))
}

// Equivalent to: \d{3}-\d{3}-\d{4}
```

### Email Pattern

```kotlin
val emailRegex = buildRegex {
  matchStringBegin()
  matchNamedGroup("username") {
    matchCharacterSet(Quantifier.atLeastOne) {
      add(digitChar)
      add(wordChar)
      add('_')
    }
  }
  match("@")
  matchNamedGroup("domain") {
    match(wordChar, Quantifier.atLeastOne)
    matchUncapturedGroup(Quantifier.atLeastOne) {
      match('.')
      match(wordChar, Quantifier.atLeastOne)
    }
  }
  matchStringEnd()
}

// Equivalent to: ^(?<username>[\d\w_]+)@(?<domain>\w+(?:\.\w+)+)$
```

### URL Pattern

```kotlin
val urlRegex = buildRegex {
    matchStringBegin()
    match("http")
    match('s', Quantifier.atMostOne)
    match("://")
    matchIndexedGroup {
        matchNamedGroup("ip") {
            match(digitChar, Quantifier.atLeastOne)
            match('.')
            match(digitChar, Quantifier.atLeastOne)
            match('.')
            match(digitChar, Quantifier.atLeastOne)
            match('.')
            match(digitChar, Quantifier.atLeastOne)
            matchIndexedGroup(Quantifier.atMostOne) {
                match(':')
                match(digitChar, Quantifier.anyTimes)
            }
        }
        insertOr()
        matchNamedGroup("domain") {
            matchIndexedGroup(Quantifier.anyTimes) {
                match(wordChar, Quantifier.atLeastOne)
                match('.')
            }
            match(wordChar, Quantifier.atLeastOne)
        }
    }
    matchNamedGroup("path") {
        matchIndexedGroup(Quantifier.anyTimes) {
            match('/')
            matchNegatedCharacterSet(Quantifier.atLeastOne) {
                add('/')
            }
        }
    }
    matchStringEnd()
}

// Equivalent to: ^https?://((?<ip>\d+\.\d+\.\d+\.\d+(:\d*)?)|(?<domain>(\w+\.)*\w+))(?<path>(/[^/]+)*)$
```

### Using Groups and Backreferences

```kotlin
val regex = buildRegex {
    val group = matchNamedGroup("test") {
        match("abc")
    }
    match(group) // Backreference to the named group
}

// Equivalent to: (?<test>abc)\k<test>
```

## API Reference

### Main Functions

- `buildRegex { ... }`: Creates a `Regex` object using the DSL
- `buildRegexString { ... }`: Returns the string representation of the regex pattern

### Matching Functions

- `match(string)`: Matches a literal string (automatically escaped)
- `match(char)`: Matches a literal character (automatically escaped)
- `match(regexElement)`: Matches a regex element (e.g., `digitChar`, `wordChar`)
- `matchCharacterSet { ... }`: Matches a character set (e.g., `[abc]`)
- `matchNegatedCharacterSet { ... }`: Matches a negated character set (e.g., `[^abc]`)

### Boundary Functions

- `matchStringBegin()`: Matches the beginning of the string (`^`)
- `matchStringEnd()`: Matches the end of the string (`$`)
- `matchWordBoundary()`: Matches a word boundary (`\b`)
- `matchNonWordBoundary()`: Matches a non-word boundary (`\B`)

### Group Functions

- `matchIndexedGroup { ... }`: Creates a capturing group (`(...)`)
- `matchNamedGroup(name) { ... }`: Creates a named capturing group (`(?<name>...)`)
- `matchUncapturedGroup { ... }`: Creates a non-capturing group (`(?:...)`)

### Lookaround Functions

- `lookahead { ... }`: Creates a positive lookahead assertion (`(?=...)`)
- `lookaheadNegative { ... }`: Creates a negative lookahead assertion (`(?!...)`)
- `lookbehind { ... }`: Creates a positive lookbehind assertion (`(?<=...)`)
- `lookbehindNegative { ... }`: Creates a negative lookbehind assertion (`(?<!...)`)

### Other Functions

- `insertOr()`: Inserts an alternation operator (`|`)

### Meta Characters

- `anyChar`: Matches any character (`.`)
- `digitChar`: Matches a digit character (`\d`)
- `nonDigitChar`: Matches a non-digit character (`\D`)
- `wordChar`: Matches a word character (`\w`)
- `nonWordChar`: Matches a non-word character (`\W`)
- `spaceChar`: Matches a whitespace character (`\s`)
- `nonSpaceChar`: Matches a non-whitespace character (`\S`)

### Quantifiers

- `Quantifier.anyTimes`: Matches 0 or more times (`*`)
- `Quantifier.atLeastOne`: Matches 1 or more times (`+`)
- `Quantifier.atMostOne`: Matches 0 or 1 time (`?`)
- `Quantifier.exactly(n)`: Matches exactly n times (`{n}`)
- `Quantifier.atLeast(n)`: Matches at least n times (`{n,}`)
- `Quantifier.inRange(n..m)`: Matches between n and m times (`{n,m}`)
- Lazy versions: `atLeastOneLazy`, `atLeastLazy(n)`, `inRangeLazy(n..m)`

## Supported Platforms

- JVM
- JavaScript
- Native (iOS, macOS, Linux, Windows, watchOS, tvOS, Android)
- WebAssembly (Wasm)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) file for details.
