package logickt.microkanren

import logickt.Eat
import logickt.List
import logickt.assertStreamEquals
import org.junit.jupiter.api.Test

internal class MicroKanrenKtTest {

    private val x = List.variable("x")
    private val y = List.variable("y")
    private val z = List.variable("z")
    private val u = List.variable("u")
    private val w = List.variable("w")
    private val v = List.variable("v")

    private val a = List.atom('a')
    private val b = List.atom('b')
    private val e = List.atom('e')

    private val t = List.atom(true)
    private val f = List.atom(false)

    private val olive = List.atom(Eat.Olive)
    private val oil = List.atom(Eat.Oil)
    private val plum = List.atom(Eat.Plum)
    private val ice = List.atom(Eat.Ice)
    private val corn = List.atom(Eat.Corn)

    private val _0 = List.reifyName(0)
    private val _1 = List.reifyName(1)
    private val _2 = List.reifyName(2)

    @Test
    internal fun testDisj() {
        assertStreamEquals(listOf(), disj<Any>()(Substitution()))
        assertStreamEquals(listOf(Substitution(x to a)), disj(equiv(x, a))(Substitution()))
        assertStreamEquals(
            listOf(Substitution(x to a), Substitution(x to b)),
            disj(equiv(x, a), equiv(x, b))(Substitution())
        )
        assertStreamEquals(
            listOf(Substitution(x to a), Substitution(x to e), Substitution(x to b)),
            disj(equiv(x, a), equiv(x, b), equiv(x, e))(Substitution())
        )
    }

    @Test
    internal fun testConj() {
        assertStreamEquals(listOf(Substitution()), conj<Any>()(Substitution()))
        assertStreamEquals(listOf(Substitution(x to a)), conj(equiv(x, a))(Substitution()))
        assertStreamEquals(
            listOf(),
            conj(equiv(x, a), equiv(x, b))(Substitution())
        )
        assertStreamEquals(
            listOf(Substitution(y to b, x to a)),
            conj(equiv(x, a), equiv(y, b))(Substitution())
        )
        assertStreamEquals(
            listOf(Substitution(z to e, y to b, x to a)),
            conj(equiv(x, a), equiv(y, b), equiv(z, e))(Substitution())
        )
        assertStreamEquals(
            listOf(),
            conj(equiv(x, a), equiv(y, b), equiv(x, e))(Substitution())
        )
    }

    @Test
    internal fun testDisjAndConj() {
        assertStreamEquals(
            listOf(Substitution(y to e, x to a), Substitution(z to e, x to b)),
            disj(conj(equiv(x, a), equiv(y, e)), conj(equiv(x, b), equiv(z, e)))(Substitution())
        )
        assertStreamEquals(
            listOf(Substitution(z to e, x to a), Substitution(x to b, y to e), Substitution(z to e, y to e)),
            conj(disj(equiv(x, a), equiv(y, e)), disj(equiv(x, b), equiv(z, e)))(Substitution())
        )
    }
}
