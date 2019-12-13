package logickt

import funkt.Assoc
import logickt.List.Companion.atom
import logickt.List.Companion.list
import logickt.List.Companion.variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MicroKanrenKtTest {

    private val x = variable("x")
    private val y = variable("y")
    private val z = variable("z")
    private val w = variable("w")
    private val v = variable("v")

    @Test
    internal fun testWalk() {
        assertEquals(atom('a'), walk(z, Assoc(z to atom('a'), x to w, y to z)))
        assertEquals(atom('a'), walk(y, Assoc(z to atom('a'), x to w, y to z)))
        assertEquals(w, walk(x, Assoc(z to atom('a'), x to w, y to z)))
        assertEquals(y, walk(x, Assoc(x to y, v to x, w to x)))
        assertEquals(y, walk(v, Assoc(x to y, v to x, w to x)))
        assertEquals(y, walk(w, Assoc(x to y, v to x, w to x)))
        assertEquals(list(x, atom('e'), z), walk(w, Assoc(x to atom('b'), z to y, w to list(x, atom('e'), z))))
    }
}
