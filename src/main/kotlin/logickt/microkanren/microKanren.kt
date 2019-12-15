package logickt.microkanren

fun <A> disj(vararg goals: Goal<A>): Goal<A> = goals.fold(failure()) { acc, goal ->
    disj2(acc, goal)
}

fun <A> conj(vararg goals: Goal<A>): Goal<A> = goals.fold(success()) { acc, goal ->
    conj2(acc, goal)
}

