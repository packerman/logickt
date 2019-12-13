package logickt

import funkt.Assoc
import funkt.Option
import funkt.Option.Companion.some
import logickt.List.Companion.atom
import logickt.List.Companion.cons
import logickt.List.Companion.list
import logickt.List.Companion.variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class MicroKanrenKtTest {

    private val x = variable("x")
    private val y = variable("y")
    private val z = variable("z")
    private val w = variable("w")
    private val v = variable("v")

    private val a = atom('a')
    private val e = atom('e')

    @Test
    internal fun testWalk() {
        assertEquals(a, walk(z, Assoc(z to a, x to w, y to z)))
        assertEquals(a, walk(y, Assoc(z to a, x to w, y to z)))
        assertEquals(w, walk(x, Assoc(z to a, x to w, y to z)))
        assertEquals(y, walk(x, Assoc(x to y, v to x, w to x)))
        assertEquals(y, walk(v, Assoc(x to y, v to x, w to x)))
        assertEquals(y, walk(w, Assoc(x to y, v to x, w to x)))
        assertEquals(list(x, e, z), walk(w, Assoc(x to atom('b'), z to y, w to list(x, e, z))))
    }

    @Test
    internal fun testExtendSubstitution() {
        assertEquals(Option<Any>(), extendSubstitution(x, list(x), Assoc()))
        assertEquals(Option<Any>(), extendSubstitution(x, list(y), Assoc(y to x)))
        val s = Assoc(z to x, y to z)
        assertEquals(some(e), extendSubstitution(x, e, s).map { walk(y, it) })
    }

    @Test
    internal fun testOccurs() {
        assertTrue(occurs(x, x, Assoc()))
        assertTrue(occurs(x, list(y), Assoc(y to x)))
    }

    @Test
    internal fun testUnify() {
        assertEquals(some(Substitution<Char>()), unify(a, a, Assoc()))
        assertEquals(some(Assoc(x to a)), unify(x, a, Assoc()))
        assertEquals(some(Assoc(y to a)), unify(a, y, Assoc()))
        assertEquals(some(Assoc(y to a, x to e)), unify(cons(x, a), cons(e, y), Assoc()))
        assertEquals(Option<Substitution<Char>>(), unify(cons(a, x), cons(e, y), Assoc()))
        assertEquals(Option<Substitution<Char>>(), unify(cons(x, a), cons(e, y), Assoc(x to a)))
    }
}
