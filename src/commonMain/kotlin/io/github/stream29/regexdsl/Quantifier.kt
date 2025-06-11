package io.github.stream29.regexdsl

import kotlin.jvm.JvmInline

@JvmInline
public value class Quantifier internal constructor(public val value: String) {
    public companion object {
        public val anyTimes: Quantifier = Quantifier("*")
        public val atLeastOne: Quantifier = Quantifier("+")
        public val atLeastOneLazy: Quantifier = Quantifier("+?")
        public val atMostOne: Quantifier = Quantifier("?")
        public fun exactly(times: Int): Quantifier = Quantifier("{$times}")
        public fun atLeast(times: Int): Quantifier = Quantifier("{$times,}")
        public fun atLeastLazy(times: Int): Quantifier = Quantifier("{$times,}?")
        public fun inRange(range: IntRange): Quantifier = Quantifier("{${range.first},${range.last}}")
        public fun inRangeLazy(range: IntRange): Quantifier = Quantifier("{${range.first},${range.last}}?")
    }
}