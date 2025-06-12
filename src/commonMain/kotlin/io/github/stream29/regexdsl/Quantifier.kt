package io.github.stream29.regexdsl

import kotlin.jvm.JvmInline

/**
 * Represents a quantifier in a regular expression.
 * 
 * Quantifiers specify how many times a character, group, or character class can be repeated.
 * 
 * @property value The string representation of the quantifier in regex syntax.
 */
@JvmInline
public value class Quantifier internal constructor(public val value: String) {
    public companion object {
        /**
         * Matches zero or more occurrences of the preceding element.
         * 
         * Equivalent to the `*` quantifier in regex.
         */
        public val anyTimes: Quantifier = Quantifier("*")

        /**
         * Matches one or more occurrences of the preceding element.
         * 
         * Equivalent to the `+` quantifier in regex.
         */
        public val atLeastOne: Quantifier = Quantifier("+")

        /**
         * Matches one or more occurrences of the preceding element, as few as possible (lazy).
         * 
         * Equivalent to the `+?` quantifier in regex.
         */
        public val atLeastOneLazy: Quantifier = Quantifier("+?")

        /**
         * Matches zero or one occurrence of the preceding element.
         * 
         * Equivalent to the `?` quantifier in regex.
         */
        public val atMostOne: Quantifier = Quantifier("?")

        /**
         * Matches exactly the specified number of occurrences of the preceding element.
         * 
         * @param times The exact number of times the element should occur.
         * @return A quantifier that matches exactly [times] occurrences.
         */
        public fun exactly(times: Int): Quantifier = Quantifier("{$times}")

        /**
         * Matches at least the specified number of occurrences of the preceding element.
         * 
         * @param times The minimum number of times the element should occur.
         * @return A quantifier that matches at least [times] occurrences.
         */
        public fun atLeast(times: Int): Quantifier = Quantifier("{$times,}")

        /**
         * Matches at least the specified number of occurrences of the preceding element, as few as possible (lazy).
         * 
         * @param times The minimum number of times the element should occur.
         * @return A lazy quantifier that matches at least [times] occurrences.
         */
        public fun atLeastLazy(times: Int): Quantifier = Quantifier("{$times,}?")

        /**
         * Matches a number of occurrences of the preceding element within the specified range.
         * 
         * @param range The range of allowed occurrences.
         * @return A quantifier that matches between [range.first] and [range.last] occurrences.
         */
        public fun inRange(range: IntRange): Quantifier = Quantifier("{${range.first},${range.last}}")

        /**
         * Matches a number of occurrences of the preceding element within the specified range, as few as possible (lazy).
         * 
         * @param range The range of allowed occurrences.
         * @return A lazy quantifier that matches between [range.first] and [range.last] occurrences.
         */
        public fun inRangeLazy(range: IntRange): Quantifier = Quantifier("{${range.first},${range.last}}?")
    }
}
