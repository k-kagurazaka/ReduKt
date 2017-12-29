package com.kkagurazaka.redukt.sample.presentation.todolist

import com.kkagurazaka.redukt.Action
import com.kkagurazaka.redukt.sample.query.Todo
import com.kkagurazaka.redukt.sample.query.VisibilityFilter

sealed class TodoListAction : Action {

    sealed class Todos : TodoListAction() {

        data class Change(val todoList: List<Todo>) : Todos()
    }

    sealed class Visibility : TodoListAction() {

        data class Change(val filter: VisibilityFilter) : Visibility()
    }
}
