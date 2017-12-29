package com.kkagurazaka.redukt.sample.presentation.todolist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.util.Log
import com.kkagurazaka.redukt.Store
import com.kkagurazaka.redukt.sample.query.Todo
import com.kkagurazaka.redukt.sample.query.VisibilityFilter

class TodoListStore : Store<TodoListState>() {

    val todoList: LiveData<List<Todo>>
        get() = Transformations.map(observableState) { it.todos.todoList }

    val visibilityFilter: LiveData<VisibilityFilter>
        get() = Transformations.map(observableState) { it.visibilityFilter.filter }

    init {
        register(TodoListReducer)

        onStateChanged = { old, action, new ->
            Log.d("TodoStoreLog", "=============== Log ===============")
            Log.d("TodoStoreLog", "before state: $old")
            Log.d("TodoStoreLog", "action: $action")
            Log.d("TodoStoreLog", "after changed: $new")
            Log.d("TodoStoreLog", "===================================")
        }
    }
}
