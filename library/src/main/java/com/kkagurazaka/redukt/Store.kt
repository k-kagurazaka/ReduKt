package com.kkagurazaka.redukt

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.UiThread
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Class representing a holder of the app state.
 */
abstract class Store(private val uiContext: CoroutineContext = UI) : ViewModel() {

    /**
     * Called when the state is changed by [Action].
     */
    var onStateChanged: ((old: State, action: Action, new: State) -> Unit)? = null

    private val states = mutableMapOf<Class<out State>, MutableLiveData<out State>>()

    private val reducers = mutableListOf<ReducerWrapper<*, *>>()

    private val dispatchActor = actor<DispatchRequest>(CommonPool, capacity = Channel.UNLIMITED) {
        for ((action, channel) in channel) {
            onActionDispatch(action)
            channel.send(Unit)
        }
    }

    /**
     * The current state held in this [Store].
     */
    inline fun <reified S : State> getState(): S =
            getLiveState<S>(S::class.java).value ?: throw IllegalStateException()

    /**
     * The current state held in this [Store], which can be observed.
     */
    inline fun <reified S : State> getLiveState(): LiveData<S> =
            getLiveState(S::class.java)

    @Suppress("UNCHECKED_CAST")
    fun <S : State> getLiveState(stateClass: Class<out State>): LiveData<S> =
            states[stateClass] as? LiveData<S> ?: throw IllegalArgumentException()

    /**
     * Registers a reducer to calculate a next state.
     *
     * @param reducer a reducer to be registered
     */
    @UiThread
    protected inline fun <reified S : State, reified A : Action> register(
            reducer: Reducer<S, A>,
            noinline stateCreator: () -> MutableLiveData<S> = ::MutableLiveData
    ) {
        register(reducer, stateCreator, S::class.java, { it as? S }, { it as? A })
    }

    @UiThread
    protected fun <S : State, A : Action> register(
            reducer: Reducer<S, A>,
            stateCreator: () -> MutableLiveData<S>,
            stateClass: Class<out State>,
            stateCast: (State) -> S?,
            actionCast: (Action) -> A?
    ) {
        val state = states[stateClass] ?: stateCreator().also { states[stateClass] = it }
        if (state.value == null) {
            state.value = reducer.initialState()
        }
        ReducerWrapper(reducer, stateCast, actionCast).let(reducers::add)
    }

    /**
     * Dispatches [action] to change the state held in this [Store].
     *
     * @param action an action to be dispatched
     */
    fun <A : Action> dispatch(action: A): Job {
        val channel = Channel<Unit>()
        DispatchRequest(action, channel).let(dispatchActor::offer)
        return launch(CommonPool) { channel.receiveOrNull() }
    }

    private suspend fun onActionDispatch(action: Action) {
        for (liveState in states.values) {
            val currentState = liveState.value ?: throw IllegalStateException()
            val nextState = reducers.fold(currentState) { state, reducer ->
                reducer.reduce(state, action)
            }
            if (currentState !== nextState) {
                onStateChanged?.invoke(currentState, action, nextState)
                withContext(uiContext) {
                    liveState.value = nextState
                }
            }
        }
    }

    private class ReducerWrapper<S : State, A : Action>(
            private val reducer: Reducer<S, A>,
            private val stateCast: (State) -> S?,
            private val actionCast: (Action) -> A?
    ) {

        fun reduce(currentState: State, action: Action): State {
            val concreteState = stateCast(currentState) ?: return currentState
            val concreteAction = actionCast(action) ?: return currentState
            return reducer.reduce(concreteState, concreteAction)
        }
    }

    private data class DispatchRequest(
            val action: Action,
            val channel: Channel<Unit>
    )
}
