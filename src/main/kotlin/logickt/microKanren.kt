package logickt

import funkt.*
import funkt.Option.Companion.some
import logickt.List.Companion.cons
import logickt.List.Companion.reifyName
import logickt.List.Companion.variable
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

fun <A> conj2(goal1: Goal<A>, goal2: Goal<A>): Goal<A> = { s ->
    goal1(s).flatMap(goal2)
}

fun <R> fresh(name: String, f: (Variable) -> R): R =
    variable(name).let(f)

fun <A> walkRec(v: List<A>, s: Substitution<A>): List<A> =
    walk(v, s).let { w ->
        w.getVariable().map { it as List<A> }.orElse {
            w.getPair().map { (car, cdr) ->
                cons(walkRec(car, s), walkRec(cdr, s))
            }
        }.getOrElse(w)
    }

fun <A> reifySubstitution(v: List<A>, r: Substitution<A>): Substitution<A> =
    walk(v, r).let { w ->
        w.getVariable().map {
            r.assoc(it, reifyName(r.length))
        }.orElse {
            w.getPair().map { (car, cdr) ->
                reifySubstitution(cdr, reifySubstitution(car, r))
            }
        }.getOrElse(r)
    }

fun <A> reify(v: List<A>): ((Substitution<A>) -> List<A>) = { s ->
    walkRec(v, s).let { w ->
        walkRec(w, reifySubstitution(w, Substitution()))
    }
}
