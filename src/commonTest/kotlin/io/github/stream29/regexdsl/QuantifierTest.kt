package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class QuantifierTest {
    @Test
    fun testAnyTimes() {
        val regex = buildRegex {
            match("a", Quantifier.anyTimes)
        }
        assertEquals("a*", regex.pattern)
    }

    @Test
    fun testAtLeastOne() {
        val regex = buildRegex {
            match("a", Quantifier.atLeastOne)
        }
        assertEquals("a+", regex.pattern)
    }

    @Test
    fun testAtLeastOneLazy() {
        val regex = buildRegex {
            match("a", Quantifier.atLeastOneLazy)
        }
        assertEquals("a+?", regex.pattern)
    }

    @Test
    fun testAtMostOne() {
        val regex = buildRegex {
            match("a", Quantifier.atMostOne)
        }
        assertEquals("a?", regex.pattern)
    }

    @Test
    fun testExactly() {
        val regex = buildRegex {
            match("a", Quantifier.exactly(2))
        }
        assertEquals("a{2}", regex.pattern)
    }

    @Test
    fun testAtLeast() {
        val regex = buildRegex {
            match("a", Quantifier.atLeast(2))
        }
        assertEquals("a{2,}", regex.pattern)
    }

    @Test
    fun testAtLeastLazy() {
        val regex = buildRegex {
            match("a", Quantifier.atLeastLazy(2))
        }
        assertEquals("a{2,}?", regex.pattern)
    }

    @Test
    fun testInRange() {
        val regex = buildRegex {
            match("a", Quantifier.inRange(2..4))
        }
        assertEquals("a{2,4}", regex.pattern)
    }

    @Test
    fun testInRangeLazy() {
        val regex = buildRegex {
            match("a", Quantifier.inRangeLazy(2..4))
        }
        assertEquals("a{2,4}?", regex.pattern)
    }

    @Test
    fun testQuantifierWithMetaCharacter() {
        val regex = buildRegex {
            match(anyChar, Quantifier.atLeastOne)
            match(digitChar)
        }
        assertEquals(""".+\d""", regex.pattern)
    }

    @Test
    fun testQuantifierWithCharacterSet() {
        val regex = buildRegex {
            matchCharacterSet(Quantifier.atLeastOne) {
                add('a')
                add(digitChar)
            }
        }
        assertEquals("""[a\d]+""", regex.pattern)
    }

    @Test
    fun testQuantifierWithGroup() {
        val regex = buildRegex {
            matchIndexedGroup(Quantifier.atLeastOne) {
                match(wordChar)
            }
        }
        assertEquals("""(\w)+""", regex.pattern)
    }
}