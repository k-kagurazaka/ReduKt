package com.kkagurazaka.redukt.sample.query

interface TodoQueries {

    suspend fun findAll(): List<Todo>

    suspend fun findActive(): List<Todo>

    suspend fun findCompleted(): List<Todo>
}
