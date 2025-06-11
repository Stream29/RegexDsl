package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class MetaCharacterTest {
    
    @Test
    fun testMetaCharacterAppendTo() {
        val metaChar = MetaCharacter("\\d")
        val sb = StringBuilder()
        metaChar.appendTo(sb)
        assertEquals("\\d", sb.toString())
    }
    
    @Test
    fun testAnyChar() {
        val scope = RegexScope()
        val anyChar = scope.anyChar
        assertEquals(".", anyChar.value)
        
        val sb = StringBuilder()
        anyChar.appendTo(sb)
        assertEquals(".", sb.toString())
    }
    
    @Test
    fun testSpaceChar() {
        val scope = RegexScope()
        val spaceChar = scope.spaceChar
        assertEquals("\\s", spaceChar.value)
        
        val sb = StringBuilder()
        spaceChar.appendTo(sb)
        assertEquals("\\s", sb.toString())
    }
    
    @Test
    fun testNonSpaceChar() {
        val scope = RegexScope()
        val nonSpaceChar = scope.nonSpaceChar
        assertEquals("\\S", nonSpaceChar.value)
        
        val sb = StringBuilder()
        nonSpaceChar.appendTo(sb)
        assertEquals("\\S", sb.toString())
    }
    
    @Test
    fun testWordChar() {
        val scope = RegexScope()
        val wordChar = scope.wordChar
        assertEquals("\\w", wordChar.value)
        
        val sb = StringBuilder()
        wordChar.appendTo(sb)
        assertEquals("\\w", sb.toString())
    }
    
    @Test
    fun testNonWordChar() {
        val scope = RegexScope()
        val nonWordChar = scope.nonWordChar
        assertEquals("\\W", nonWordChar.value)
        
        val sb = StringBuilder()
        nonWordChar.appendTo(sb)
        assertEquals("\\W", sb.toString())
    }
    
    @Test
    fun testDigitChar() {
        val scope = RegexScope()
        val digitChar = scope.digitChar
        assertEquals("\\d", digitChar.value)
        
        val sb = StringBuilder()
        digitChar.appendTo(sb)
        assertEquals("\\d", sb.toString())
    }
    
    @Test
    fun testNonDigitChar() {
        val scope = RegexScope()
        val nonDigitChar = scope.nonDigitChar
        assertEquals("\\D", nonDigitChar.value)
        
        val sb = StringBuilder()
        nonDigitChar.appendTo(sb)
        assertEquals("\\D", sb.toString())
    }
    
    @Test
    fun testMetaCharactersInRegex() {
        val regex = buildRegex {
            match(anyChar)
            match(spaceChar)
            match(nonSpaceChar)
            match(wordChar)
            match(nonWordChar)
            match(digitChar)
            match(nonDigitChar)
        }
        
        assertEquals(".\\s\\S\\w\\W\\d\\D", regex.pattern)
    }
}