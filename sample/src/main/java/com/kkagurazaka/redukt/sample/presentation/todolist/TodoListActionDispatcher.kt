package com.kkagurazaka.redukt.sample.presentation.todolist

import com.kkagurazaka.redukt.sample.command.TodoUseCase
import com.kkagurazaka.redukt.sample.query.TodoQueries
import com.kkagurazaka.redukt.sample.query.VisibilityFilter
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject
import javax.inject.Singleton

class TodoListActionDispatcher private constructor(
        private val store: TodoListStore,
        private val useCase: TodoUseCase,
        private val queries: TodoQueries
) {

    fun initialize(): Job = launch(CommonPool) {
        dispatchTodosChange()
    }

    fun addTodo(text: String): Job = launch(CommonPool) {
        useCase.add(text)
        dispatchTodosChange()
    }

    fun toggleTodo(id: Int): Job = launch(CommonPool) {
        useCase.toggleCompleted(id)
        dispatchTodosChange()
    }

    fun changeVisibilityFilter(filter: VisibilityFilter): Job = launch(CommonPool) {
        TodoListAction.Visibility.Change(filter).let(store::dispatch)
        dispatchTodosChange(filter)
    }

    private suspend fun dispatchTodosChange(
            filter: VisibilityFilter = store.state.visibilityFilter.filter
    ) {
        val todoList = when (filter) {
            VisibilityFilter.ALL -> queries.findAll()
            VisibilityFilter.ACTIVE -> queries.findActive()
            VisibilityFilter.COMPLETED -> queries.findCompleted()
        }
        TodoListAction.Todos.Change(todoList).let(store::dispatch)
    }

    @Singleton
    class Factory @Inject constructor(
            private val useCase: TodoUseCase,
            private val queries: TodoQueries
    ) {

        operator fun invoke(store: TodoListStore): TodoListActionDispatcher =
                TodoListActionDispatcher(store, useCase, queries)
    }
}
