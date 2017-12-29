package com.kkagurazaka.redukt.sample.presentation.todolist

import com.kkagurazaka.redukt.Reducer
import com.kkagurazaka.redukt.sample.query.VisibilityFilter

object VisibilityFilterReducer : Reducer<VisibilityFilterState, TodoListAction.Visibility> {

    override fun initialState(): VisibilityFilterState =
            VisibilityFilterState(filter = VisibilityFilter.ALL)

    override fun reduce(currentState: VisibilityFilterState, action: TodoListAction.Visibility): VisibilityFilterState =
            when (action) {
                is TodoListAction.Visibility.Change -> reduce(currentState, action)
            }

    private fun reduce(currentState: VisibilityFilterState, action: TodoListAction.Visibility.Change): VisibilityFilterState =
            currentState.copy(
                    filter = action.filter
            )
}
