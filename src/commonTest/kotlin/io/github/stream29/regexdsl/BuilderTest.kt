package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class BuilderTest {
    
    @Test
    fun testBuildSimpleRegex() {
        val regex = buildRegex {
            match("abc")
        }
        
        assertEquals("abc", regex.pattern)
        assertTrue(regex.matches("abc"))
        assertFalse(regex.matches("def"))
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
        
        assertEquals("\\d\\d\\d-\\d\\d\\d-\\d\\d\\d\\d", regex.pattern)
        assertTrue(regex.matches("123-456-7890"))
        assertFalse(regex.matches("12-345-6789"))
        assertFalse(regex.matches("abc-def-ghij"))
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
        
        assertEquals("\\d{3}-\\d{3}-\\d{4}", regex.pattern)
        assertTrue(regex.matches("123-456-7890"))
        assertFalse(regex.matches("12-345-6789"))
        assertFalse(regex.matches("abc-def-ghij"))
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
        
        assertEquals("(\\w+)@(\\w+)\\.(\\w+)", regex.pattern)
        assertTrue(regex.matches("user@example.com"))
        assertFalse(regex.matches("user@.com"))
        assertFalse(regex.matches("@example.com"))
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
        assertTrue(regex.matches("a1"))
        assertTrue(regex.matches("b2"))
        assertTrue(regex.matches("c9"))
        assertFalse(regex.matches("d1"))
        assertFalse(regex.matches("aa"))
    }
    
    @Test
    fun testBuildComplexRegex() {
        // Build a regex for validating a simple URL pattern
        val regex = buildRegex {
            matchStringBegin()
            match("http")
            matchUncapturedGroup(Quantifier.atMostOne) {
                match("s")
            }
            match("://")
            matchIndexedGroup {
                match(wordChar, Quantifier.atLeastOne)
                matchUncapturedGroup(Quantifier.anyTimes) {
                    match(".")
                    match(wordChar, Quantifier.atLeastOne)
                }
            }
            matchUncapturedGroup(Quantifier.atMostOne) {
                match("/")
                matchUncapturedGroup(Quantifier.atMostOne) {
                    match(anyChar, Quantifier.atLeastOne)
                }
            }
            matchStringEnd()
        }
        
        assertTrue(regex.matches("http://example.com"))
        assertTrue(regex.matches("https://example.com"))
        assertTrue(regex.matches("http://sub.example.com"))
        assertTrue(regex.matches("http://example.com/path"))
        assertFalse(regex.matches("ftp://example.com"))
        assertFalse(regex.matches("http:/example.com"))
    }
}