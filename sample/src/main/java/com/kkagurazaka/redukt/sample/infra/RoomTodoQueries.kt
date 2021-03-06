package com.kkagurazaka.redukt.sample.infra

import com.kkagurazaka.redukt.sample.query.Todo
import com.kkagurazaka.redukt.sample.query.TodoQueries
import dagger.Reusable
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

@Reusable
class RoomTodoQueries @Inject constructor(
        private val dao: TodoQueryDao
) : TodoQueries {

    override suspend fun findAll(): List<Todo> = withContext(CommonPool) {
        dao.findAll()
    }

    override suspend fun findActive(): List<Todo> = withContext(CommonPool) {
        dao.findBy(completed = false)
    }

    override suspend fun findCompleted(): List<Todo> = withContext(CommonPool) {
        dao.findBy(completed = true)
    }
}
