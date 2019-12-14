package logickt

import funkt.Option
import funkt.Option.Companion.some
import funkt.toStream
import logickt.Eat.*
import logickt.List.Companion.atom
import logickt.List.Companion.cons
import logickt.List.Companion.list
import logickt.List.Companion.reifyName
import logickt.List.Companion.variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class MicroKanrenKtTest {

    private val x = variable("x")
    private val y = variable("y")
    private val z = variable("z")
    private val u = variable("u")
    private val w = variable("w")
    private val v = variable("v")

    private val a = atom('a')
    private val b = atom('b')
    private val e = atom('e')

    private val t = atom(true)
    private val f = atom(false)

    private val olive = atom(Olive)
    private val oil = atom(Oil)
    private val plum = atom(Plum)
    private val ice = atom(Ice)
    private val corn = atom(Corn)

    private val _0 = reifyName(0)
    private val _1 = reifyName(1)
    private val _2 = reifyName(2)

    @Test
    internal fun testWalk() {
        assertEquals(a, walk(z, Substitution(z to a, x to w, y to z)))
        assertEquals(a, walk(y, Substitution(z to a, x to w, y to z)))
        assertEquals(w, walk(x, Substitution(z to a, x to w, y to z)))
        assertEquals(y, walk(x, Substitution(x to y, v to x, w to x)))
        assertEquals(y, walk(v, Substitution(x to y, v to x, w to x)))
        assertEquals(y, walk(w, Substitution(x to y, v to x, w to x)))
        assertEquals(list(x, e, z), walk(w, Substitution(x to b, z to y, w to list(x, e, z))))
    }

    @Test
    internal fun testExtendSubstitution() {
        assertEquals(Option<Any>(), extendSubstitution(x, list(x), Substitution()))
        assertEquals(Option<Any>(), extendSubstitution(x, list(y), Substitution(y to x)))
        val s = Substitution(z to x, y to z)
        assertEquals(some(e), extendSubstitution(x, e, s).map { walk(y, it) })
    }

    @Test
    internal fun testOccurs() {
        assertTrue(occurs(x, x, Substitution()))
        assertTrue(occurs(x, list(y), Substitution(y to x)))
    }

    @Test
    internal fun testUnify() {
        assertEquals(some(Substitution<Char>()), unify(a, a, Substitution()))
        assertEquals(some(Substitution(x to a)), unify(x, a, Substitution()))
        assertEquals(some(Substitution(y to a)), unify(a, y, Substitution()))
        assertEquals(some(Substitution(y to a, x to e)), unify(cons(x, a), cons(e, y), Substitution()))
        assertEquals(Option<Substitution<Char>>(), unify(cons(a, x), cons(e, y), Substitution()))
        assertEquals(Option<Substitution<Char>>(), unify(cons(x, a), cons(e, y), Substitution(x to a)))
    }

    @Test
    internal fun testEquiv() {
        assertStreamEquals(listOf(), equiv(t, f)(Substitution()))
        assertStreamEquals(listOf(), failure<Nothing>()(Substitution()))
        assertStreamEquals(listOf(Substitution<Nothing>()), equiv(f, f)(Substitution()))
        assertStreamEquals(listOf(Substitution<Nothing>()), success<Nothing>()(Substitution()))
        assertStreamEquals(listOf(Substitution(x to y)), equiv(x, y)(Substitution()))
    }

    @Test
    internal fun testDisj2() {
        assertStreamEquals(
            listOf(Substitution(x to olive), Substitution(x to oil)),
            disj2(equiv(olive, x), equiv(oil, x))(Substitution())
        )
    }

    @Test
    internal fun testConj2() {
        assertStreamEquals(
            listOf(Substitution(y to oil, x to olive)),
            conj2(equiv(olive, x), equiv(oil, y))(Substitution())
        )
        assertStreamEquals(
            listOf(),
            conj2(equiv(olive, x), equiv(oil, x))(Substitution())
        )
    }

    @Test
    internal fun testFresh() {
        assertStreamEquals(
            listOf(plum),
            fresh("kiwi") { fruit ->
                equiv(plum, fruit)
            }(Substitution()).take(1)
                .flatMap { it.toStream() }.map { it.second }
        )
    }

    @Test
    internal fun walkRecursively() {
        assertEquals(list(b, e, y), walkRec(w, Substitution(x to b, z to y, w to list(x, e, z))))
    }

    @Test
    internal fun testReify() {
        val a1 = x to list(u, w, y, z, list(list(ice), z))
        val a2 = y to corn
        val a3 = w to list(v, u)
        val s = Substitution(a1, a2, a3)
        assertEquals(list(_0, list(_1, _0), corn, _2, list(list(ice), _2)), reify<Eat>(x)(s))
    }

    @Test
    internal fun testRunGoal() {
        assertStreamEquals(
            listOf(olive, oil),
            runGoal(
                5,
                disj2(equiv(olive, x), equiv(oil, x))
            )
                .map(reify<Eat>(x))
        )
    }

    @Test
    internal fun testIfte() {
        assertStreamEquals(
            listOf(Substitution(y to f)),
            ifte(success(), equiv(f, y), equiv(t, y))(Substitution())
        )
        assertStreamEquals(
            listOf(Substitution(y to t)),
            ifte(failure(), equiv(f, y), equiv(t, y))(Substitution())
        )
        assertStreamEquals(
            listOf(Substitution(y to f, x to t)),
            ifte(equiv(t, x), equiv(f, y), equiv(t, y))(Substitution())
        )
        assertStreamEquals(
            listOf(Substitution(y to f, x to t), Substitution(y to f, x to f)),
            ifte(disj2(equiv(t, x), equiv(f, x)), equiv(f, y), equiv(t, y))(Substitution())
        )
    }

    @Test
    internal fun testOnce() {
        assertStreamEquals(
            listOf(Substitution(y to f, x to t)),
            ifte(once(disj2(equiv(t, x), equiv(f, x))), equiv(f, y), equiv(t, y))(Substitution())
        )
    }
}
