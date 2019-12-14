package logickt

import funkt.Stream
import org.junit.jupiter.api.Assertions.assertEquals

enum class Eat {
    Olive,
    Oil,
    Plum,
    Kiwi,
    Ice,
    Corn
}

fun <A> assertStreamEquals(expected: kotlin.collections.List<A>, actual: Stream<A>) =
    assertEquals(expected, actual.asIterable().toList())
