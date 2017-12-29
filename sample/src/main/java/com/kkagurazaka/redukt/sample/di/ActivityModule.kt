package com.kkagurazaka.redukt.sample.di

import com.kkagurazaka.redukt.sample.presentation.todolist.TodoListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {

    @ContributesAndroidInjector
    fun contributesTodoListActivity(): TodoListActivity
}
