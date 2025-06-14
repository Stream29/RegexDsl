# The idea of building a DSL for regex in Kotlin

Regex has a long history in computer science, and it's a powerful tool to process Strings.
But we also know that using a lot of regex expressions in your code would definitely make it unmaintainable.
Thus, I came up with an idea that we need a more simple and maintainable way to write regex.

How? Let's build an expressive DSL for that.
But we want code completion and type safe with our DSL.

Kotlin may be the best choice for this usage. 

## How the DSL should be?

We need it to be straightforward for simple needs. Like:

```Kotlin
val regex = buildRegex {
    match("hello, ")
    matchNamedGroup("name") {
        match(wordChar, Quantifier.anyTimes)
    }
}
// regex: hello, (?<name>\w*)
println(regex.matchEntire("hello, world")?.groups["name"]?.value)
// output: world
```

We need it to be easy to compose, making a complex regex:

```Kotlin
    val regex = buildRegex {
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
// regex: ^https?://((?<ip>\d+\.\d+\.\d+\.\d+(:\d*)?)|(?<domain>(\w+\.)*\w+))(?<path>(/[^/]+)*)$
val matchGroups = regex.matchEntire("https://192.168.1.1:8080/abc/def/ghi")?.groups
println("ip: ${matchGroups?.get("ip")?.value}")
// output: ip: 192.168.1.1:8000
println("domain: ${matchGroups?.get("domain")?.value}")
// output: domain: null
println("path: ${matchGroups?.get("path")?.value}")
// output: path: /abc/def/ghi
```

We need it to be able to represent any element in regex:

```Kotlin
val regex = buildRegex {
    matchStringBegin()
    val capturedGroup = matchIndexedGroup {
        matchWordBoundary()
        match(wordChar, Quantifier.atLeastOne)
        match(digitChar, Quantifier.exactly(3))
        matchWordBoundary()
    }
    match(capturedGroup, Quantifier.atLeastOne)
    matchUncapturedGroup {
        match('@')
        matchNamedGroup("domain") {
            match(wordChar, Quantifier.atLeastOne)
            matchCharacterSet {
                range('a', 'z')
                add(digitChar)
            }
            matchCharacterSet(Quantifier.exactly(2)) {
                range('a', 'z')
            }
        }
    }
    lookbehindNegative {
        match("test")
    }
    lookaheadNegative {
        match("end")
    }
    matchStringEnd()
}
// regex: ^(\b\w+\d{3}\b)\1+(?:@(?<domain>\w+[a-z\d][a-z]{2}))(?<!test)(?!end)$
```

We need it to be type-safe and provide code completions.

Kotlin is the best choice for this usage.

## How to build a DSL in Kotlin?

### Principles

Similar to any other DSL built in Kotlin,
a DSL in Kotlin works with these two core concepts: receiver and lambda.

A **receiver** in Kotlin means the instance of a member/extension function invocation. Or you can call it `this`.
For example, in `listOf(1, 1, 4, 5, 1, 4).sorted()`, the `List<Int>` instance is the **receiver**.
JavaScript programmers may be familiar that, `this` may change in different execution contexts.
Kotlin provides a simple and strong way to change `this` and its type for specific usages, by enhancing the function types in Kotlin.

Basically, function types in Kotlin can have a receiver type. For example:

```Kotlin
typealias normalFunctionType = (ParameterType) -> ReturnType
typealias functionTypeWithReceiver = ReceiverType.(ParameterType) -> ReturnType
```

Then, a small but important sugar in Kotlin: 
if the last parameter's type of function is a function type, we can write it in a more simple way:

```Kotlin
fun functionWithFunctionalParameter(functionalParam: (ParamType) -> ReturnType) {
    // Do some work
}

// invocation without sugar
functionWithFunctionalParameter( // The parathesis can be omitted
    { param: ParamType -> // The type of the lambda's parameter can be inferred and the parameter declaration can be omitted
        return doSomeWork(param) // The last line of the lambda is automatically used as the return value
    }
)

// invocation with sugar
functionWithFunctionalParameter { doSomeWork(it) }
```

When we mix these two features in Kotlin, it just happens:

```Kotlin
class DslScope {
    val count = 0
    fun increase() {
        count++
    }
}

fun DslScope.double() {
    count *= 2
}

fun createScopeAndShowCount(dslBlock: DslScope.() -> Unit) { // Unit is similar to void in other languages, meaning no return value
    val dslScope = DslScope()
    dslScope.dslBlock()
    println("count: ${dslScope.count}")
}

createScopeAndShowCount {
    // Here, the type of `this` is `DslScope`, so all its member and extension functions are accessible
    increase()
    increase()
    double()
}
```

### General guidelines for building your own DSL

Generally, to make a DSL in Kotlin, we can do these things:

#### Define the scope/receiver of your DSL

The scope type also acts as the receiver type of your DSL. 
If needed, we may define multiple receiver types for managing visibility for different usages.

In a functional programming vision, the receiver of a scope collects almost all the side effects of what happens in the scope.
Thus, it should store the state of the scope.

```kotlin
class RegexBuilderScope {
    val currentRegex = StringBuilder()
}
class CharacterSetBuilderScope {
    val chars = setOf<String>()
}
```

#### Define the accessible features of your DSL

Without a proper receiver, or you can call it `this`, in the context, a function or property is not accessible.
This is the key feature that guarantees the type safety of Kotlin DSL.

```kotlin
fun RegexBuilderScope.match(text: String) { 
    // do something with the receiver, for example, store
}

fun RegexBuilderScope.matchGroup(block: RegexBuilderScope.() -> Unit) {
    // In this way, we can create embedded hierarchy
}

fun RegexBuilderScope.matchCharacterSet(block: CharacterSetBuilderScope.() -> Unit) {
    // In this way, we can set the type of the scope receiver to `CharacterSetBuilderScope`
}
```

#### Define the builder function of your DSL

Builder functions are usually visible globally, used as the entrance of your DSL.

```kotlin
fun buildRegexString(buildAction: RegexBuilderScope.() -> Unit): String {
    val builder = RegexBuilderScope()
    builder.buildAction()
    return builder.currentRegex.toString()
}

fun buildRegex(buildAction: RegexBuilderScope.() -> Unit): Regex = buildRegexString(buildAction).toRegex()
```

## The DSL I built in Kotlin

The code above is just an example. The real version I implemented in Kotlin is more strong and fast, supporting more features in regex.

If you want to see the complete version of this library, you can go up to see the chapter [How the DSL should be?](#how-the-dsl-should-be).
These examples are all runnable codes.

If you want to run the examples, You can run them in [Kotlin Notebook](./examples.ipynb).

## Extended contents

### Run as a tool script

This tool is very convenient when we want to build a regex and use it in our Kotlin project.
But what if we need to use it in another way?

If you want to build a DSL with a lightweight tool, you can download [the basic kotlin script](./ScriptBase.main.kts).
You can enjoy code completion and type safe features without creating a Kotlin project. 
Write a script, run it, and get the regex.

### Multiplatform support

This library supports Kotlin/Multiplatform. So it can be used on JVM, Wasm, JS platforms, and compiled to native code.

### Further than a simple guide

This library uses some techniques not in the text above.

To make the DSL running faster and creating fewer lambda classes on JVM, most of the functions in `RegexDsl` are `inline`.

To specify the receiver of DSL function more strictly, `@DslMarker` meta annotation is used.

For those who want to learn further on Kotlin DSL, you can explore these topics.

### Other DSLs implemented in Kotlin

- Gradle build script
- Jetpack Compose `@Composable` function
- Ktor routing DSL
- Exposed database operation DSL (schema/DAO)