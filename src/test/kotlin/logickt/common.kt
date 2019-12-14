package logickt

import funkt.Stream
import org.junit.jupiter.api.Assertions.assertEquals

enum class Luquid {
    Olive,
    Oil
}

enum class Fruit {
    Plum,
    Kiwi
}

fun <A> assertStreamEquals(expected: kotlin.collections.List<A>, actual: Stream<A>) =
    assertEquals(expected, actual.asIterable().toList())
