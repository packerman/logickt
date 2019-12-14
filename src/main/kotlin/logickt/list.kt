package logickt

import funkt.Option

sealed class List<out A> {

    fun getVariable(): Option<Variable> = Option(this as? Variable)

    fun getPair(): Option<Pair<List<A>, List<A>>> = when (this) {
        is Cons -> Option(car to cdr)
        else -> Option()
    }

    internal object Nil : List<Nothing>()

    internal data class Atom<A>(val value: A) : List<A>() {

        override fun toString(): String = value.toString()
    }

    internal data class Cons<A>(val car: List<A>, val cdr: List<A>) : List<A>()

    class Variable internal constructor(val name: String) : List<Nothing>() {

        override fun toString(): String = name
    }

    internal data class Reified(val n: Int) : List<Nothing>() {

        override fun toString(): String = "_$n"
    }

    companion object {

        fun <A> nil(): List<A> = Nil

        fun <A> atom(value: A): List<A> = Atom(value)

        fun <A> cons(car: A, cdr: A): List<A> = Cons(Atom(car), Atom(cdr))

        fun <A> cons(car: A, cdr: List<A>): List<A> = Cons(Atom(car), cdr)

        fun <A> cons(car: List<A>, cdr: List<A>): List<A> = Cons(car, cdr)

        fun <A> list(vararg es: List<A>): List<A> = es.foldRight(Nil) { e: List<A>, l: List<A> ->
            Cons(e, l)
        }

        fun variable(name: String): Variable = Variable(name)

        fun reifyName(n: Int): List<Nothing> = Reified(n)

        fun <A> list(f: A, vararg es: A): List<A> = Cons(Atom(f), es.foldRight(Nil) { e: A, l: List<A> ->
            Cons(Atom(e), l)
        })
    }
}
