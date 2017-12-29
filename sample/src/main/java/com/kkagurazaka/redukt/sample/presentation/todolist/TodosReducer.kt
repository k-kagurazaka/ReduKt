package com.kkagurazaka.redukt.sample.presentation.todolist

import com.kkagurazaka.redukt.Reducer

object TodosReducer : Reducer<TodosState, TodoListAction.Todos> {

    override fun initialState(): TodosState = TodosState(emptyList())

    override fun reduce(currentState: TodosState, action: TodoListAction.Todos): TodosState =
            when (action) {
                is TodoListAction.Todos.Change -> reduce(currentState, action)
            }

    private fun reduce(currentState: TodosState, action: TodoListAction.Todos.Change): TodosState =
            currentState.copy(todoList = action.todoList)
}
