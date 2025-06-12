package io.github.stream29.regexdsl

import kotlin.test.Test
import kotlin.test.assertEquals

class GroupTest {
    @Test
    fun testNamedGroupRefAppendTo() {
        val regex = buildRegex {
            match("abc")
            matchNamedGroup("test") {
                match("def")
            }
            match(NamedGroupRef("test"))
        }
        assertEquals("""abc(?<test>def)\k<test>""", regex.pattern)
    }

    @Test
    fun testIndexedGroupInRegex() {
        val regex = buildRegex {
            val indexedGroup = matchIndexedGroup {
                match("abc")
            }
            match(indexedGroup)
        }
        assertEquals("""(abc)\1""", regex.pattern)
    }

    @Test
    fun testNamedGroupInRegex() {
        val regex = buildRegex {
            val group = matchNamedGroup("test") {
                match("abc")
            }
            match(group)
        }
        assertEquals("""(?<test>abc)\k<test>""", regex.pattern)
    }

    @Test
    fun testMultipleIndexedGroups() {
        val regex = buildRegex {
            matchIndexedGroup {
                match("abc")
            }
            matchIndexedGroup {
                match("def")
            }
            match(IndexedGroupRef(1))
            match(IndexedGroupRef(2))
        }
        assertEquals("""(abc)(def)\1\2""", regex.pattern)
    }

    @Test
    fun testMultipleNamedGroups() {
        val regex = buildRegex {
            val group1 = matchNamedGroup("first") {
                match("abc")
            }
            val group2 = matchNamedGroup("second") {
                match("def")
            }
            match(group1)
            match(group2)
        }
        assertEquals("""(?<first>abc)(?<second>def)\k<first>\k<second>""", regex.pattern)
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
            match(IndexedGroupRef(1))
            match(IndexedGroupRef(2))
        }
        assertEquals("""(a(b)c)\1\2""", regex.pattern)
    }
}
