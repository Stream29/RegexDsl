package io.github.stream29.regexdsl

/**
 * Provides a mechanism to generate sequential indices for indexed groups of regex.
 */
public interface IndexProvider {
    /**
     * Returns the next index to be used for capturing a group.
     *
     * **Warning: If you call this function manually, there may be a mistake in the generated [IndexedGroupRef]**
     */
    @InternalRegexDslApi
    public fun nextIndex(): Int
    public class Impl(
        startFrom: Int = 1,
        private val step: Int = 1,
    ) : IndexProvider {
        private var index = startFrom - step
        @InternalRegexDslApi
        override fun nextIndex(): Int {
            index += step
            return index
        }
    }
}