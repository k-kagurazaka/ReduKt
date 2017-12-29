package com.kkagurazaka.redukt

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.UiThread
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.run

/**
 * Class representing a holder of the app state.
 */
abstract class Store<S : State> : ViewModel() {

    /**
     * The current state held in this [Store].
     */
    val state: S
        get() = mutableState.value ?: throw IllegalStateException()

    /**
     * The current state held in this [Store], which can be observed.
     */
    val observableState: LiveData<S>
        get() = mutableState

    /**
     * Called when the state is changed by [Action].
     */
    var onStateChanged: ((old: S, action: Action, new: S) -> Unit)? = null

    private val mutableState = MutableLiveData<S>()

    private val reducers = mutableListOf<ReducerWrapper<S, *>>()

    private val dispatchActor = actor<Action>(CommonPool, capacity = Channel.UNLIMITED) {
        for (action in channel) {
            onActionDispatch(action)
        }
    }

    /**
     * Registers a reducer to calculate a next state.
     *
     * @param reducer a reducer to be registered
     */
    @UiThread
    protected inline fun <reified A : Action> register(reducer: Reducer<S, A>) {
        register(reducer, A::class.java)
    }

    @UiThread
    protected fun <A : Action> register(reducer: Reducer<S, A>, actionClass: Class<out Action>) {
        if (mutableState.value == null) {
            mutableState.value = reducer.initialState()
        }
        ReducerWrapper(reducer, actionClass).let(reducers::add)
    }

    /**
     * Dispatches [action] to change the state held in this [Store].
     *
     * @param action an action to be dispatched
     */
    fun <A : Action> dispatch(action: A) {
        dispatchActor.offer(action)
    }

    private suspend fun onActionDispatch(action: Action) {
        val currentState = mutableState.value ?: throw IllegalStateException()
        val nextState = reducers.fold(currentState) { state, reducer ->
            reducer.reduce(state, action)
        }
        if (currentState !== nextState) {
            onStateChanged?.invoke(currentState, action, nextState)
            run(UI) {
                mutableState.value = nextState
            }
        }
    }

    private class ReducerWrapper<S : State, A : Action>(
            private val reducer: Reducer<S, A>,
            private val actionClass: Class<out Action>
    ) {

        @Suppress("UNCHECKED_CAST")
        fun reduce(currentState: S, action: Action): S =
                if (actionClass.isAssignableFrom(action.javaClass)) {
                    reducer.reduce(currentState, action as A)
                } else {
                    currentState
                }
    }
}
