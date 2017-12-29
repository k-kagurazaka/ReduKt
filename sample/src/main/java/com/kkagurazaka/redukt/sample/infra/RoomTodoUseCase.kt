package com.kkagurazaka.redukt.sample.infra

import com.kkagurazaka.redukt.sample.command.TodoUseCase
import dagger.Reusable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.run
import javax.inject.Inject

@Reusable
class RoomTodoUseCase @Inject constructor(
        private val dao: TodoCommandDao
) : TodoUseCase {

    override suspend fun add(text: String): Unit = run(CommonPool) {
        Todo(text = text).let(dao::insert)
    }

    override suspend fun toggleCompleted(id: Int): Unit = run<Unit>(CommonPool) {
        val todo = dao.findById(id)
        todo.completed = !todo.completed
        dao.update(todo)
    }
}
