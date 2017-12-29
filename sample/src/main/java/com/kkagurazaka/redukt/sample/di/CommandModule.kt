package com.kkagurazaka.redukt.sample.di

import com.kkagurazaka.redukt.sample.command.TodoUseCase
import com.kkagurazaka.redukt.sample.infra.RoomTodoUseCase
import dagger.Binds
import dagger.Module

@Module
interface CommandModule {

    @Binds
    fun bindsTodoUseCase(commands: RoomTodoUseCase): TodoUseCase
}
