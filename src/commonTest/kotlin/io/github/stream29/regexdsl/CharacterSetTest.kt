package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class CharacterSetTest {
    
    @Test
    fun testCharacterSetAppendTo() {
        val characterSet = CharacterSet()
        characterSet.add('a')
        characterSet.add('b')
        characterSet.add('c')
        
        val sb = StringBuilder()
        characterSet.appendTo(sb)
        assertEquals("[abc]", sb.toString())
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
        val characterSet = CharacterSet()
        characterSet.range('a', 'z')
        characterSet.range('A', 'Z')
        characterSet.range('0', '9')
        
        val sb = StringBuilder()
        characterSet.appendTo(sb)
        assertEquals("[a-zA-Z0-9]", sb.toString())
    }
    
    @Test
    fun testCharacterSetAddMixed() {
        val characterSet = CharacterSet()
        characterSet.add('a')
        characterSet.range('0', '9')
        characterSet.add('_')
        
        val sb = StringBuilder()
        characterSet.appendTo(sb)
        assertEquals("[a0-9_]", sb.toString())
    }
    
    @Test
    fun testNegatedCharacterSet() {
        val characterSet = NegatedCharacterSet()
        characterSet.add('a')
        characterSet.add('b')
        characterSet.add('c')
        
        val sb = StringBuilder()
        characterSet.appendTo(sb)
        assertEquals("[^abc]", sb.toString())
    }
    
    @Test
    fun testNegatedCharacterSetAddRange() {
        val characterSet = NegatedCharacterSet()
        characterSet.range('a', 'z')
        
        val sb = StringBuilder()
        characterSet.appendTo(sb)
        assertEquals("[^a-z]", sb.toString())
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
        assertEquals(true, regex.matches("a"))
        assertEquals(true, regex.matches("b"))
        assertEquals(true, regex.matches("c"))
        assertEquals(false, regex.matches("d"))
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
        assertEquals(false, regex.matches("a"))
        assertEquals(false, regex.matches("b"))
        assertEquals(false, regex.matches("c"))
        assertEquals(true, regex.matches("d"))
    }
    
    @Test
    fun testCharacterSetWithQuantifier() {
        val regex = buildRegex {
            matchCharacterSet(Quantifier.atLeastOne) {
                range('0', '9')
            }
        }
        
        assertEquals("[0-9]+", regex.pattern)
        assertEquals(false, regex.matches(""))
        assertEquals(true, regex.matches("0"))
        assertEquals(true, regex.matches("123"))
        assertEquals(false, regex.matches("a"))
    }
}