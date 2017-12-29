package com.kkagurazaka.redukt.sample.di

import android.arch.persistence.room.Room
import android.content.Context
import com.kkagurazaka.redukt.sample.infra.TodoCommandDao
import com.kkagurazaka.redukt.sample.infra.TodoDatabase
import com.kkagurazaka.redukt.sample.infra.TodoQueryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InfraModule {

    @Singleton
    @Provides
    fun providesTodoDatabase(context: Context): TodoDatabase =
            Room.databaseBuilder(context, TodoDatabase::class.java, "TodoDatabase").build()

    @Provides
    fun providesTodoCommandDao(database: TodoDatabase): TodoCommandDao = database.todoCommandDao()

    @Provides
    fun providesTodoQueryDao(database: TodoDatabase): TodoQueryDao = database.todoQueryDao()
}
