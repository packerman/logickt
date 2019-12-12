package logickt

sealed class List<out A> {

    internal object Nil : List<Nothing>()

    internal data class Atom<A>(val value: A) : List<A>()

    internal data class Cons<A>(val car: List<A>, val cdr: List<A>) : List<A>()

    class Variable<A>(private val name: String) : List<A>()

    companion object {

        fun <A> nil(): List<A> = Nil

        fun <A> atom(value: A): List<A> = Atom(value)

        fun <A> cons(car: A, cdr: A): List<A> = Cons(Atom(car), Atom(cdr))

        fun <A> cons(car: A, cdr: List<A>): List<A> = Cons(Atom(car), cdr)

        fun <A> cons(car: List<A>, cdr: List<A>): List<A> = Cons(car, cdr)

        fun <A> list(vararg es: List<A>): List<A> = es.foldRight(Nil) { e: List<A>, l: List<A> ->
            Cons(e, l)
        }

        fun <A> list(f: A, vararg es: A): List<A> = Cons(Atom(f), es.foldRight(Nil) { e: A, l: List<A> ->
            Cons(Atom(e), l)
        })
    }
}
