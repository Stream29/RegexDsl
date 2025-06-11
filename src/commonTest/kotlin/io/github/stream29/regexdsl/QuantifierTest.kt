package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class QuantifierTest {
    
    @Test
    fun testAnyTimes() {
        val quantifier = Quantifier.anyTimes
        assertEquals("*", quantifier.value)
        
        val regex = buildRegex {
            match("a", quantifier)
        }
        
        assertEquals("a*", regex.pattern)
        assertTrue(regex.matches(""))
        assertTrue(regex.matches("a"))
        assertTrue(regex.matches("aa"))
        assertTrue(regex.matches("aaa"))
        assertFalse(regex.matches("b"))
    }
    
    @Test
    fun testAtLeastOne() {
        val quantifier = Quantifier.atLeastOne
        assertEquals("+", quantifier.value)
        
        val regex = buildRegex {
            match("a", quantifier)
        }
        
        assertEquals("a+", regex.pattern)
        assertFalse(regex.matches(""))
        assertTrue(regex.matches("a"))
        assertTrue(regex.matches("aa"))
        assertTrue(regex.matches("aaa"))
        assertFalse(regex.matches("b"))
    }
    
    @Test
    fun testAtLeastOneLazy() {
        val quantifier = Quantifier.atLeastOneLazy
        assertEquals("+?", quantifier.value)
        
        val regex = buildRegex {
            match("a", quantifier)
        }
        
        assertEquals("a+?", regex.pattern)
        assertFalse(regex.matches(""))
        assertTrue(regex.matches("a"))
        assertTrue(regex.matches("aa"))
        assertTrue(regex.matches("aaa"))
        assertFalse(regex.matches("b"))
    }
    
    @Test
    fun testAtMostOne() {
        val quantifier = Quantifier.atMostOne
        assertEquals("?", quantifier.value)
        
        val regex = buildRegex {
            match("a", quantifier)
        }
        
        assertEquals("a?", regex.pattern)
        assertTrue(regex.matches(""))
        assertTrue(regex.matches("a"))
        assertFalse(regex.matches("aa"))
        assertFalse(regex.matches("b"))
    }
    
    @Test
    fun testExactly() {
        val quantifier = Quantifier.exactly(3)
        assertEquals("{3}", quantifier.value)
        
        val regex = buildRegex {
            match("a", quantifier)
        }
        
        assertEquals("a{3}", regex.pattern)
        assertFalse(regex.matches(""))
        assertFalse(regex.matches("a"))
        assertFalse(regex.matches("aa"))
        assertTrue(regex.matches("aaa"))
        assertFalse(regex.matches("aaaa"))
        assertFalse(regex.matches("b"))
    }
    
    @Test
    fun testAtLeast() {
        val quantifier = Quantifier.atLeast(2)
        assertEquals("{2,}", quantifier.value)
        
        val regex = buildRegex {
            match("a", quantifier)
        }
        
        assertEquals("a{2,}", regex.pattern)
        assertFalse(regex.matches(""))
        assertFalse(regex.matches("a"))
        assertTrue(regex.matches("aa"))
        assertTrue(regex.matches("aaa"))
        assertTrue(regex.matches("aaaa"))
        assertFalse(regex.matches("b"))
    }
    
    @Test
    fun testInRange() {
        val quantifier = Quantifier.inRange(2..4)
        assertEquals("{2,4}", quantifier.value)
        
        val regex = buildRegex {
            match("a", quantifier)
        }
        
        assertEquals("a{2,4}", regex.pattern)
        assertFalse(regex.matches(""))
        assertFalse(regex.matches("a"))
        assertTrue(regex.matches("aa"))
        assertTrue(regex.matches("aaa"))
        assertTrue(regex.matches("aaaa"))
        assertFalse(regex.matches("aaaaa"))
        assertFalse(regex.matches("b"))
    }
    
    @Test
    fun testQuantifierWithMetaCharacter() {
        val regex = buildRegex {
            match(digitChar, Quantifier.exactly(3))
        }
        
        assertEquals("\\d{3}", regex.pattern)
        assertTrue(regex.matches("123"))
        assertFalse(regex.matches("12"))
        assertFalse(regex.matches("1234"))
        assertFalse(regex.matches("abc"))
    }
    
    @Test
    fun testQuantifierWithCharacterSet() {
        val regex = buildRegex {
            matchCharacterSet(Quantifier.atLeastOne) {
                range('a', 'z')
            }
        }
        
        assertEquals("[a-z]+", regex.pattern)
        assertFalse(regex.matches(""))
        assertTrue(regex.matches("a"))
        assertTrue(regex.matches("abc"))
        assertFalse(regex.matches("123"))
    }
    
    @Test
    fun testQuantifierWithGroup() {
        val regex = buildRegex {
            matchIndexedGroup(Quantifier.atLeastOne) {
                match("abc")
            }
        }
        
        assertEquals("(abc)+", regex.pattern)
        assertFalse(regex.matches(""))
        assertTrue(regex.matches("abc"))
        assertTrue(regex.matches("abcabc"))
        assertFalse(regex.matches("ab"))
    }
}