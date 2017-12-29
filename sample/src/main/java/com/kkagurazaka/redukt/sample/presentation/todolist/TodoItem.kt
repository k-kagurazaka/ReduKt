package com.kkagurazaka.redukt.sample.presentation.todolist

import com.kkagurazaka.redukt.sample.query.Todo

class TodoItem(val todo: Todo, private val onClick: (Todo) -> Unit) {

    fun onClick() {
        onClick.invoke(todo)
    }
}
