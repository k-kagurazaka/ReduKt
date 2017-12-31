package com.kkagurazaka.redukt.sample.presentation.todolist

import android.arch.lifecycle.LiveData
import android.util.Log
import com.kkagurazaka.redukt.Store
import com.kkagurazaka.redukt.sample.presentation.common.StoreLogger
import com.kkagurazaka.redukt.sample.presentation.common.map
import com.kkagurazaka.redukt.sample.query.Todo
import com.kkagurazaka.redukt.sample.query.VisibilityFilter

class TodoListStore : Store<TodoListState>() {

    val todoList: LiveData<List<Todo>> =
            observableState.map { it?.todos?.todoList }

    val visibilityFilter: LiveData<VisibilityFilter> =
            observableState.map { it?.visibilityFilter?.filter }

    init {
        register(TodoListReducer)
        onStateChanged = StoreLogger
    }
}
