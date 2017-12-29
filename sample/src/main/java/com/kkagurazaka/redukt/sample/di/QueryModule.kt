package com.kkagurazaka.redukt.sample.di

import com.kkagurazaka.redukt.sample.infra.RoomTodoQueries
import com.kkagurazaka.redukt.sample.query.TodoQueries
import dagger.Binds
import dagger.Module

@Module
interface QueryModule {

    @Binds
    fun bindsTodoQueries(queries: RoomTodoQueries): TodoQueries
}
