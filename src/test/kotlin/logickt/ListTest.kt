package logickt

import logickt.List.*
import logickt.List.Companion.atom
import logickt.List.Companion.cons
import logickt.List.Companion.list
import logickt.List.Companion.nil
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
}
