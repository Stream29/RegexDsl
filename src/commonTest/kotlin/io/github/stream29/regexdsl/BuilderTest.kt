package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class BuilderTest {

    @Test
    fun testBuildSimpleRegex() {
        val regex = buildRegex {
            match("abc")
        }
        assertEquals("abc", regex.pattern)
    }

    @Test
    fun testBuildRegexWithMetaCharacters() {
        val regex = buildRegex {
            match(digitChar)
            match(digitChar)
            match(digitChar)
            match("-")
            match(digitChar)
            match(digitChar)
            match(digitChar)
            match("-")
            match(digitChar)
            match(digitChar)
            match(digitChar)
            match(digitChar)
        }
        assertEquals("""\d\d\d-\d\d\d-\d\d\d\d""", regex.pattern)
    }

    @Test
    fun testBuildRegexWithQuantifiers() {
        val regex = buildRegex {
            match(digitChar, Quantifier.exactly(3))
            match("-")
            match(digitChar, Quantifier.exactly(3))
            match("-")
            match(digitChar, Quantifier.exactly(4))
        }
        assertEquals("""\d{3}-\d{3}-\d{4}""", regex.pattern)
    }

    @Test
    fun testBuildRegexWithGroups() {
        val regex = buildRegex {
            matchIndexedGroup {
                match(wordChar, Quantifier.atLeastOne)
            }
            match("@")
            matchIndexedGroup {
                match(wordChar, Quantifier.atLeastOne)
            }
            match(".")
            matchIndexedGroup {
                match(wordChar, Quantifier.atLeastOne)
            }
        }
        assertEquals("""(\w+)@(\w+)\.(\w+)""", regex.pattern)
    }

    @Test
    fun testBuildRegexWithCharacterSets() {
        val regex = buildRegex {
            matchCharacterSet {
                add('a')
                add('b')
                add('c')
            }
            matchCharacterSet {
                range('0', '9')
            }
        }
        assertEquals("[abc][0-9]", regex.pattern)
    }

    @Test
    fun testBuildComplexRegex() {
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
        assertEquals("""^https?://((?<ip>\d+\.\d+\.\d+\.\d+(:\d*)?)|(?<domain>(\w+\.)*\w+))(?<path>(/[^/]+)*)$""", regex.pattern)
    }
}