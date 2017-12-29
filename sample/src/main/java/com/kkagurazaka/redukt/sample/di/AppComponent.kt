package com.kkagurazaka.redukt.sample.di

import com.kkagurazaka.redukt.sample.TodoApplication
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    InfraModule::class,
    CommandModule::class,
    QueryModule::class,
    ActivityModule::class
])
interface AppComponent : AndroidInjector<TodoApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<TodoApplication>()
}
