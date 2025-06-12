package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterSetTest {
    @Test
    fun testCharacterSetAppendTo() {
        val regex = buildRegex {
            matchCharacterSet {
                add('a')
                add('b')
                add('c')
            }
        }
        assertEquals("[abc]", regex.pattern)
    }

    @Test
    fun testCharacterSetAddRange() {
        val characterSet = CharacterSet()
        characterSet.range('a', 'z')

        val sb = StringBuilder()
        characterSet.appendTo(sb)
        assertEquals("[a-z]", sb.toString())
    }

    @Test
    fun testCharacterSetAddMultipleRanges() {
        val regex = buildRegex {
            matchCharacterSet {
                range('a', 'z')
                range('A', 'Z')
                range('0', '9')
            }
        }
        assertEquals("[a-zA-Z0-9]", regex.pattern)
    }

    @Test
    fun testCharacterSetAddMixed() {
        val regex = buildRegex {
            matchCharacterSet {
                add('a')
                range('0', '9')
                add('_')
            }
        }
        assertEquals("[a0-9_]", regex.pattern)
    }

    @Test
    fun testNegatedCharacterSet() {
        val regex = buildRegex {
            matchNegatedCharacterSet {
                add('a')
                add('b')
                add('c')
            }
        }
        assertEquals("[^abc]", regex.pattern)
    }

    @Test
    fun testNegatedCharacterSetAddRange() {
        val regex = buildRegex {
            matchNegatedCharacterSet {
                range('a', 'z')
            }
        }
        assertEquals("[^a-z]", regex.pattern)
    }

    @Test
    fun testCharacterSetInRegex() {
        val regex = buildRegex {
            matchCharacterSet {
                add('a')
                add('b')
                add('c')
            }
        }
        assertEquals("[abc]", regex.pattern)
    }

    @Test
    fun testNegatedCharacterSetInRegex() {
        val regex = buildRegex {
            matchNegatedCharacterSet {
                add('a')
                add('b')
                add('c')
            }
        }
        assertEquals("[^abc]", regex.pattern)
    }

    @Test
    fun testCharacterSetWithQuantifier() {
        val regex = buildRegex {
            matchCharacterSet(Quantifier.atLeastOne) {
                range('0', '9')
            }
        }
        assertEquals("[0-9]+", regex.pattern)
    }
}