package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class RegexScopeTest {

    @Test
    fun testAppendTo() {
        val scope = RegexScope()
        scope.match("test")

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("test", sb.toString())
    }

    @Test
    fun testMatchStringBegin() {
        val scope = RegexScope()
        scope.matchStringBegin()

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("^", sb.toString())
    }

    @Test
    fun testInsertOr() {
        val scope = RegexScope()
        scope.match("a")
        scope.insertOr()
        scope.match("b")

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("a|b", sb.toString())
    }

    @Test
    fun testMatchStringEnd() {
        val scope = RegexScope()
        scope.matchStringEnd()

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("$", sb.toString())
    }

    @Test
    fun testMatchWordBoundary() {
        val scope = RegexScope()
        scope.matchWordBoundary()

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("\\b", sb.toString())
    }

    @Test
    fun testMatchNonWordBoundary() {
        val scope = RegexScope()
        scope.matchNonWordBoundary()

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("\\B", sb.toString())
    }

    @Test
    fun testMatchString() {
        val scope = RegexScope()
        scope.match("test")

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("test", sb.toString())
    }

    @Test
    fun testMatchStringWithSpecialChars() {
        val scope = RegexScope()
        scope.match("a.b*c+d?")

        val sb = StringBuilder()
        scope.appendTo(sb)
        // Special characters should be escaped
        assertEquals("a\\.b\\*c\\+d\\?", sb.toString())
    }

    @Test
    fun testMatchRegexElement() {
        val scope = RegexScope()
        scope.match(MetaCharacter("\\d"))

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("\\d", sb.toString())
    }

    @Test
    fun testMatchRegexElementWithQuantifier() {
        val scope = RegexScope()
        scope.match(MetaCharacter("\\d"), Quantifier.atLeastOne)

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("\\d+", sb.toString())
    }

    @Test
    fun testMatchCharacterSet() {
        val scope = RegexScope()
        scope.matchCharacterSet {
            add('a')
            add('b')
            add('c')
        }

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("[abc]", sb.toString())
    }

    @Test
    fun testMatchNegatedCharacterSet() {
        val scope = RegexScope()
        scope.matchNegatedCharacterSet {
            add('a')
            add('b')
            add('c')
        }

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("[^abc]", sb.toString())
    }

    @Test
    fun testMatchUncapturedGroup() {
        val scope = RegexScope()
        scope.matchUncapturedGroup {
            match("abc")
        }

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("(?:abc)", sb.toString())
    }

    @Test
    fun testMatchNamedGroup() {
        val scope = RegexScope()
        val groupRef = scope.matchNamedGroup("test") {
            match("abc")
        }

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("(?<test>abc)", sb.toString())
        assertEquals("test", groupRef.name)
    }

    @Test
    fun testMatchIndexedGroup() {
        val scope = RegexScope()
        val groupRef = scope.matchIndexedGroup {
            match("abc")
        }

        val sb = StringBuilder()
        scope.appendTo(sb)
        assertEquals("(abc)", sb.toString())
        assertEquals(1, groupRef.index)
    }

    @Test
    fun testComplexRegex() {
        val regex = buildRegex {
            matchStringBegin()
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
            matchStringEnd()
        }

        // This should create a regex for a simple email pattern
        assertEquals("^(\\w+)@(\\w+)\\.(\\w+)$", regex.pattern)

        // Test that the regex works as expected
        val testEmail = "test@example.com"
        val match = regex.matchEntire(testEmail)
        assertEquals(true, match != null)
        assertEquals("test", match?.groupValues?.get(1))
        assertEquals("example", match?.groupValues?.get(2))
        assertEquals("com", match?.groupValues?.get(3))
    }
}
