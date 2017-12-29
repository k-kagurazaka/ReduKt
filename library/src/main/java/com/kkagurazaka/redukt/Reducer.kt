package com.kkagurazaka.redukt

/**
 * Interface representing a calculator of the app state.
 */
interface Reducer<S : State, in A : Action> {

    /**
     * @return the initial instance of a state which is a target of this [Reducer]
     */
    fun initialState(): S

    /**
     * Calculates a next state by given [currentState] and [action].
     *
     * This operation must be pure, so makes no side effects and no mutations.
     *
     * @param currentState the current state
     * @param action the payload to change the [currentState]
     * @return a calculated next state as *a new instance*
     */
    fun reduce(currentState: S, action: A): S
}

/**
 * [Reducer] that delegates sub-tree state reduction to other reducers.
 */
abstract class CombinedReducer<S : State, A : Action> : Reducer<S, A> {

    private val childReducers = mutableListOf<ChildReducer<S, A, *, *>>()

    /**
     * Registers a [reducer] that calculates a sub-tree of the state.
     *
     * @param reducer a reducer to be registered, which calculates a sub-tree of the state
     * @param extract a lambda to extract a sub-tree from the state
     * @param combine a lambda to create a news state that partially replaced by the new sub-tree state
     */
    protected inline fun <SS : State, reified SA : A> register(
            reducer: Reducer<SS, SA>,
            noinline extract: (S) -> (SS),
            noinline combine: (S, SS) -> S
    ) {
        register(reducer, extract, combine) { it as? SA }
    }

    protected fun <SS : State, SA : A> register(
            reducer: Reducer<SS, SA>,
            extract: (S) -> (SS),
            combine: (S, SS) -> S,
            castAction: (A) -> SA?
    ) {
        ChildReducer<S, A, SS, SA>(reducer, castAction, extract, combine).let(childReducers::add)
    }

    override final fun reduce(currentState: S, action: A): S =
            childReducers.fold(currentState) { state, childReducer ->
                childReducer.reduce(state, action)
            }

    private class ChildReducer<S : State, A : Action, SS : State, SA : A>(
            private val reducer: Reducer<SS, SA>,
            private val castAction: (A) -> SA?,
            private val extract: (S) -> SS,
            private val combine: (S, SS) -> S
    ) {

        fun reduce(currentState: S, action: A): S {
            val subAction = castAction(action) ?: return currentState
            val subState = extract(currentState)
            val nextSubState = reducer.reduce(subState, subAction)
            return combine(currentState, nextSubState)
        }
    }
}
