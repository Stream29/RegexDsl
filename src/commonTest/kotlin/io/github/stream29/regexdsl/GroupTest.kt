package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class GroupTest {
    
    @Test
    fun testIndexedGroupRefAppendTo() {
        val groupRef = IndexedGroupRef(1)
        val sb = StringBuilder()
        groupRef.appendTo(sb)
        assertEquals("\\1", sb.toString())
    }
    
    @Test
    fun testNamedGroupRefAppendTo() {
        val groupRef = NamedGroupRef("test")
        val sb = StringBuilder()
        groupRef.appendTo(sb)
        assertEquals("\\k<test>", sb.toString())
    }
    
    @Test
    fun testIndexedGroupInRegex() {
        val regex = buildRegex {
            val indexedGroup = matchIndexedGroup {
                match("abc")
            }
            match(" ")
            match(indexedGroup)
        }
        
        assertEquals("(abc) \\1", regex.pattern)
        assertEquals(true, regex.matches("abc abc"))
        assertEquals(false, regex.matches("abc def"))
    }
    
    @Test
    fun testNamedGroupInRegex() {
        val regex = buildRegex {
            val group = matchNamedGroup("test") {
                match("abc")
            }
            match(" ")
            match(group)
        }
        
        assertEquals("(?<test>abc) \\k<test>", regex.pattern)
        assertEquals(true, regex.matches("abc abc"))
        assertEquals(false, regex.matches("abc def"))
    }
    
    @Test
    fun testMultipleIndexedGroups() {
        val regex = buildRegex {
            matchIndexedGroup {
                match("abc")
            }
            match(" ")
            matchIndexedGroup {
                match("def")
            }
            match(" ")
            match(IndexedGroupRef(1))
            match(" ")
            match(IndexedGroupRef(2))
        }
        
        assertEquals("(abc) (def) \\1 \\2", regex.pattern)
        assertEquals(true, regex.matches("abc def abc def"))
        assertEquals(false, regex.matches("abc def def abc"))
    }
    
    @Test
    fun testMultipleNamedGroups() {
        val regex = buildRegex {
            val group1 = matchNamedGroup("first") {
                match("abc")
            }
            match(" ")
            val group2 = matchNamedGroup("second") {
                match("def")
            }
            match(" ")
            match(group1)
            match(" ")
            match(group2)
        }
        
        assertEquals("(?<first>abc) (?<second>def) \\k<first> \\k<second>", regex.pattern)
        assertEquals(true, regex.matches("abc def abc def"))
        assertEquals(false, regex.matches("abc def def abc"))
    }
    
    @Test
    fun testNestedGroups() {
        val regex = buildRegex {
            matchIndexedGroup {
                match("a")
                matchIndexedGroup {
                    match("b")
                }
                match("c")
            }
            match(" ")
            match(IndexedGroupRef(1))
            match(" ")
            match(IndexedGroupRef(2))
        }
        
        assertEquals("(a(b)c) \\1 \\2", regex.pattern)
        assertEquals(true, regex.matches("abc abc b"))
        assertEquals(false, regex.matches("abc abc c"))
    }
}