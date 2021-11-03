package net.notjustanna.tartar.api.lexer

/**
 * Indicates that a specified object has a [section][Section] assigned to it.
 *
 * @author An Tran
 */
public interface Sectional {
    /**
     * The assigned section.
     */
    public val section: Section

    /**
     * Creates a section which spans across this and another section.
     */
    public fun span(other: Sectional): Section {
        return section.span(other.section)
    }
}
