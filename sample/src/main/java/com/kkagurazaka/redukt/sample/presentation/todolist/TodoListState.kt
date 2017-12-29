package com.kkagurazaka.redukt.sample.presentation.todolist

import com.kkagurazaka.redukt.State
import com.kkagurazaka.redukt.sample.query.Todo
import com.kkagurazaka.redukt.sample.query.VisibilityFilter

data class TodoListState(
        val todos: TodosState,
        val visibilityFilter: VisibilityFilterState
) : State

data class TodosState(
        val todoList: List<Todo>
) : State

data class VisibilityFilterState(
        val filter: VisibilityFilter
) : State
