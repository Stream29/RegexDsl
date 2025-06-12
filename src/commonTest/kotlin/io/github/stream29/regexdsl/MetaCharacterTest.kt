package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals


class MetaCharacterTest {
    @Test
    fun testAnyChar() {
        val regex = buildRegex {
            match(anyChar)
        }
        assertEquals(".", regex.pattern)
    }

    @Test
    fun testSpaceChar() {
        val regex = buildRegex {
            match(spaceChar)
        }
        assertEquals("""\s""", regex.pattern)
    }

    @Test
    fun testNonSpaceChar() {
        val regex = buildRegex {
            match(nonSpaceChar)
        }
        assertEquals("""\S""", regex.pattern)
    }


    @Test
    fun testWordChar() {
        val regex = buildRegex {
            match(wordChar)
        }
        assertEquals("""\w""", regex.pattern)
    }

    @Test
    fun testNonWordChar() {
        val regex = buildRegex {
            match(nonWordChar)
        }
        assertEquals("""\W""", regex.pattern)
    }

    @Test
    fun testDigitChar() {
        val regex = buildRegex {
            match(digitChar)
        }
        assertEquals("""\d""", regex.pattern)
    }

    @Test
    fun testNonDigitChar() {
        val regex = buildRegex {
            match(nonDigitChar)
        }
        assertEquals("""\D""", regex.pattern)
    }

    @Test
    fun testMetaCharactersInRegex() {
        val regex = buildRegex {
            match(anyChar, Quantifier.anyTimes)
            match(spaceChar)
            match(nonSpaceChar)
            match(wordChar)
            match(nonWordChar)
            match(digitChar)
            match(nonDigitChar)
        }
        assertEquals(""".*\s\S\w\W\d\D""", regex.pattern)
    }
}
