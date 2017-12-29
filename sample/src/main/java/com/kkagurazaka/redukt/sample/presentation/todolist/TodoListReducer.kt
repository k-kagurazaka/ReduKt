package com.kkagurazaka.redukt.sample.presentation.todolist

import com.kkagurazaka.redukt.CombinedReducer

object TodoListReducer : CombinedReducer<TodoListState, TodoListAction>() {

    init {
        register(TodosReducer, extract = TodoListState::todos, combine = { state, subState -> state.copy(todos = subState) })
        register(VisibilityFilterReducer, extract = TodoListState::visibilityFilter, combine = { state, subState -> state.copy(visibilityFilter = subState) })
    }

    override fun initialState(): TodoListState =
            TodoListState(
                    todos = TodosReducer.initialState(),
                    visibilityFilter = VisibilityFilterReducer.initialState()
            )
}
