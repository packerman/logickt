package logickt

import funkt.*
import funkt.Option.Companion.some
import logickt.List.Variable

typealias Substitution<A> = Assoc<Variable, List<A>>

fun <A> walk(v: List<A>, s: Substitution<A>): List<A> =
    v.getVariable().flatMap { s.lookup(it).map { a -> walk(a, s) } }.getOrElse(v)

fun <A> extendSubstitution(x: Variable, v: List<A>, s: Substitution<A>): Option<Substitution<A>> =
    if (occurs(x, v, s)) Option() else some(s.assoc(x, v))

fun <A> occurs(x: Variable, v: List<A>, s: Substitution<A>): Boolean =
    walk(v, s).let { w ->
        w.getVariable().map { w == x }.orElse {
            w.getPair().map { (car, cdr) ->
                occurs(x, car, s) || occurs(x, cdr, s)
            }
        }.getOrElse(false)
    }

fun <A> unify(u: List<A>, v: List<A>, s: Substitution<A>): Option<Substitution<A>> =
    walk(u, s).let { wu ->
        walk(v, s).let { wv ->
            if (wu == wv) some(s)
            else wu.getVariable().flatMap { extendSubstitution(it, wv, s) }.orElse {
                wv.getVariable().flatMap { extendSubstitution(it, wu, s) }.orElse {
                    wu.getPair().flatMap { (carU, cdrU) ->
                        wv.getPair().flatMap { (carV, cdrV) ->
                            unify(carU, carV, s).flatMap {
                                unify(cdrU, cdrV, it)
                            }
                        }
                    }
                }
            }
        }
    }

typealias Goal<A> = (Substitution<A>) -> Stream<Substitution<A>>

fun <A> equiv(u: List<A>, v: List<A>): Goal<A> = {
    unify(u, v, it).toStream()
}

fun <A> success(): Goal<A> = {
    Stream(it)
}

fun <A> failure(): Goal<A> = {
    Stream()
}

fun <A> disj2(goal1: Goal<A>, goal2: Goal<A>): Goal<A> = { s ->
    goal1(s).interleave(goal2(s))
}
