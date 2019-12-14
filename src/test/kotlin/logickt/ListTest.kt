package logickt

import funkt.Option
import logickt.List.*
import logickt.List.Companion.atom
import logickt.List.Companion.cons
import logickt.List.Companion.list
import logickt.List.Companion.nil
import logickt.List.Companion.variable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ListTest {

    @Test
    internal fun createList() {
        assertEquals(Nil, nil<Any>())
        assertEquals(nil<Any>(), list<Any>())
        assertEquals(Atom(3), atom(3))
        assertEquals(Cons(Atom(2), Atom(3)), cons(2, atom(3)))
        assertEquals(Cons(Atom(2), Atom(3)), cons(2, 3))
        assertEquals(Cons(Atom(2), Atom(3)), cons(2, 3))
        assertEquals(Cons(Atom(2), Atom(3)), cons(atom(2), atom(3)))
        assertEquals(cons(1, cons(2, cons(3, nil()))), list(atom(1), atom(2), atom(3)))
        assertEquals(cons(1, cons(2, cons(3, nil()))), list(1, 2, 3))
    }

    @Test
    internal fun shouldGetVariable() {
        val v = variable("x")

        assertEquals(Option<Variable>(), cons(1, 2).getVariable())
        assertEquals(Option(v), v.getVariable())
    }

    @Test
    internal fun shouldGetPair() {
        assertEquals(Option(atom(1) to atom(2)), cons(1, 2).getPair())
        assertEquals(Option<Nothing>(), atom(1).getPair())
        assertEquals(Option<Nothing>(), variable("x").getPair())
    }
}
