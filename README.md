# ReduKt

Predictable state container for Android &amp; Kotlin apps

## Motivation

There are a lot of Redux implementations for Android.
Why do I reinvent the wheel?
The answers are;

- Android Architecture Components (ViewModel &amp; LiveData) aware
- Without RxJava
- `CombinedReducer` support

## Install

```groovy
repositories {
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.k-kagurazaka:redukt:0.0.2'
}
```

## Basics Usage

First, define `State` data class representing your app state for each screen (Activity).

```kotlin
data class TodoListState(val todoList: List<Todo>): State
```

Next, make `Action` sealed class to change the defined `State`.

```kotlin
seald class TodoListAction : Action {
    data class Add(text: String) : TodoListAction()
    data class ToggleCompleted(id: Int): TodoListAction()
}
```

Now, we are ready for writing a `Reducer` to change the `State` with given `Action`s.

```kotlin

object TodoListReducer : Reducer<TodoListState, TodoListAction> {
    override fun initialState(): TodoListState = TodoListState(todoList = emptyList())
    
    override fun reduce(currentState: TodoListState, action: TodoListAction): TodoListState =
            when (action) {
                is TodoListAction.Add -> currentState.copy(
                    todoList = currentState.todoList.toMutableList().apply { add(Todo(text = action.text)) }
                )
                is TodoListAction.ToggleCompleted -> currentState.copy(
                    todoList = currentState.todoList.map {
                        if (it.id == action.id) it.copy(completed = !it.completed) else it
                    }
                )
            }
}
```

Finally, create `Store` class and connect it to `Activity`.
You can easily instantiate `Store` class by `ViewModelProviders` since `Store` is also `ViewModel` of Android Architecture Components.

```kotlin
class TodoListStore : Store() {
    init {
        register(TodoListReducer)
    }
}

class TodoListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_list)
        val store = ViewModelProviders.of(this).get(TodoListStore::class.java)
        store.getLiveState<TodoListState>().observe(this, Observer { binding.state = it })
        
        binding.addButton.setOnClickListener { onAddButtonClick(binding.inputEditText.text.toString()) }
        ...
    }
    
    private fun onAddButtonClick(text: String) {
        // Dispatch an action to change the app state
        TodoListAction.Add(text).let(store::dispatch)
    }
}
```

## Combined Reducer

When your app sate grows up, you may want to split `Reducer` into small sub `Reducer`s to achieve separation of concerns.
ReduKt makes it easier to do this separation by `CombinedReducer`.

```kotlin
data class TodosState(val todoList: List<Todo>) : State
data class FilteringState(filter: VisibilityFilter) : State
data class TodoListState(val todos: TodosState, val filtering: FilteringState): State

seald class TodoListAction : Action {
    seald class Todos : TodoListAction() {
        data class Add(text: String) : Todos()
        data class ToggleCompleted(id: Int): Todos()
    }
    sealed class Filtering : TodoListAction() {
        data class Change(filter: VisibilityFilter): Filtering()
    }
}

object TodosReducer<TodosState, TodoListAction.Todos> { ... }
object FilteringReducer<FilteringState, TodoListAction.FilteringState> { ... }

class TodoListReducer : CombinedReducer<TodoListState, TodoListAction>() {
    init {
        register(TodosReducer, extract = TodoListState::todos,
                combine = { state, subState -> state.copy(todos = subState) })
        register(FilteringReducer, extract = TodoListState::filtering,
                combine = { state, subState -> state.copy(filtering = subState) })
    }

    override fun initialState(): TodoListState =
            TodoListState(
                    todos = TodosReducer.initialState(),
                    filtering = FilteringReducer.initialState()
            )
}
```

## License

    Copyright 2017 Keita Kagurazaka

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
