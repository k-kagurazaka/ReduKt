package com.kkagurazaka.redukt.sample.presentation.todolist

import android.arch.lifecycle.LiveData
import com.kkagurazaka.redukt.Store
import com.kkagurazaka.redukt.sample.presentation.common.StoreLogger
import com.kkagurazaka.redukt.sample.presentation.common.map
import com.kkagurazaka.redukt.sample.query.Todo
import com.kkagurazaka.redukt.sample.query.VisibilityFilter

class TodoListStore : Store() {

    init {
        register(TodoListReducer)
        onStateChanged = StoreLogger
    }

    val state: TodoListState get() = getState()

    val todoList: LiveData<List<Todo>> =
            getLiveState<TodoListState>().map { it?.todos?.todoList }

    val visibilityFilter: LiveData<VisibilityFilter> =
            getLiveState<TodoListState>().map { it?.visibilityFilter?.filter }
}
