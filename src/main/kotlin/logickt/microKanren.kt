package logickt

import funkt.Assoc
import funkt.lookup
import logickt.List.Variable

typealias Substitution<A> = Assoc<Variable, List<A>>

fun <A> walk(v: List<A>, s: Substitution<A>): List<A> =
    v.getVariable().flatMap { s.lookup(it).map { a -> walk(a, s) } }.getOrElse(v)
