package com.kkagurazaka.redukt.sample.di

import android.content.Context
import com.kkagurazaka.redukt.sample.TodoApplication
import dagger.Binds
import dagger.Module

@Module
interface AppModule {

    @Binds
    fun bindsContext(app: TodoApplication): Context
}
