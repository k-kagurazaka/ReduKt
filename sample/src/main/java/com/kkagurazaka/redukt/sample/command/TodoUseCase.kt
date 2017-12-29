package com.kkagurazaka.redukt.sample.command

interface TodoUseCase {

    suspend fun add(text: String)

    suspend fun toggleCompleted(id: Int)
}
