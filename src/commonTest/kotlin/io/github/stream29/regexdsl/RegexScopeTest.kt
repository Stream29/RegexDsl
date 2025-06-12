package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class RegexScopeTest {
    @Test
    fun testMatchStringBegin() {
        val regex = buildRegex {
            matchStringBegin()
            match("test")
            matchStringEnd()
        }
        assertEquals("^test$", regex.pattern)
    }

    @Test
    fun testInsertOr() {
        val regex = buildRegex {
            match("abc")
            insertOr()
            match("def")
        }
        assertEquals("abc|def", regex.pattern)
    }

    @Test
    fun testMatchStringEnd() {
        val regex = buildRegex {
            match("test")
            matchStringEnd()
        }
        assertEquals("test$", regex.pattern)
    }

    @Test
    fun testMatchWordBoundary() {
        val regex = buildRegex {
            matchWordBoundary()
            match("test")
            matchWordBoundary()
        }
        assertEquals("""\btest\b""", regex.pattern)
    }

    @Test
    fun testMatchNonWordBoundary() {
        val regex = buildRegex {
            matchNonWordBoundary()
            match("test")
            matchNonWordBoundary()
        }
        assertEquals("""\Btest\B""", regex.pattern)
    }

    @Test
    fun testMatchString() {
        val regex = buildRegex {
            match("test")
        }
        assertEquals("test", regex.pattern)
    }

    @Test
    fun testMatchStringWithSpecialChars() {
        val regex = buildRegex {
            match("test.?+*^\$()[]{}|\\")
        }
        assertEquals("""test\.\?\+\*\^\$\(\)\[\]\{\}\|\\""", regex.pattern)
    }

    @Test
    fun testMatchRegexElement() {
        val regex = buildRegex {
            match(digitChar)
            match(nonDigitChar)
        }
        assertEquals("""\d\D""", regex.pattern)
    }

    @Test
    fun testMatchRegexElementWithQuantifier() {
        val regex = buildRegex {
            match(digitChar, Quantifier.exactly(2))
            match(nonDigitChar, Quantifier.atLeastOne)
        }
        assertEquals("""\d{2}\D+""", regex.pattern)
    }

    @Test
    fun testMatchCharacterSet() {
        val regex = buildRegex {
            matchCharacterSet {
                range('a', 'z')
            }
        }
        assertEquals("[a-z]", regex.pattern)
    }

    @Test
    fun testMatchNegatedCharacterSet() {
        val regex = buildRegex {
            matchNegatedCharacterSet {
                range('a', 'z')
            }
        }
        assertEquals("[^a-z]", regex.pattern)
    }

    @Test
    fun testMatchUncapturedGroup() {
        val regex = buildRegex {
            match("abc")
            matchUncapturedGroup {
                match("def")
            }
            match("ghi")
        }
        assertEquals("abc(?:def)ghi", regex.pattern)
    }

    @Test
    fun testMatchNamedGroup() {
        val regex = buildRegex {
            match("abc")
            matchNamedGroup("test") {
                match("def")
            }
            match("ghi")
        }
        assertEquals("abc(?<test>def)ghi", regex.pattern)
    }

    @Test
    fun testMatchIndexedGroup() {
        val regex = buildRegex {
            match("abc")
            matchIndexedGroup {
                match("def")
            }
            match("ghi")
        }
        assertEquals("abc(def)ghi", regex.pattern)
    }
}
