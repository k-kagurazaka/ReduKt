package com.kkagurazaka.redukt.sample.presentation.todolist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kkagurazaka.redukt.sample.BR
import com.kkagurazaka.redukt.sample.R
import com.kkagurazaka.redukt.sample.databinding.ActivityTodoListBinding
import com.kkagurazaka.redukt.sample.databinding.ItemTodoBinding
import com.kkagurazaka.redukt.sample.presentation.common.BindingViewHolder
import com.kkagurazaka.redukt.sample.query.Todo
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.properties.Delegates

class TodoListActivity : DaggerAppCompatActivity() {

    @Inject internal lateinit var createActionDispatcher: TodoListActionDispatcher.Factory

    private lateinit var binding: ActivityTodoListBinding
    private lateinit var store: TodoListStore
    private lateinit var actionDispatcher: TodoListActionDispatcher

    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_todo_list)

        store = ViewModelProviders.of(this).get(TodoListStore::class.java)
        actionDispatcher = createActionDispatcher(store)

        initializeViews()
        observeStore()

        actionDispatcher.initialize()
    }

    private fun initializeViews() {
        binding.filterSelector.onFilterChanged = {
            actionDispatcher.changeVisibilityFilter(it)
        }

        adapter = Adapter(actionDispatcher)
        binding.recyclerView.adapter = adapter

        binding.addButton.setOnClickListener {
            val text = binding.inputEditText.text.toString()
            actionDispatcher.addTodo(text)
            binding.inputEditText.setText("")
        }
    }

    private fun observeStore() {
        store.todoList.observe(this, Observer { adapter.items = it ?: emptyList() })
        store.visibilityFilter.observe(this, Observer { binding.filter = it })
    }

    private class Adapter(
            private val actionDispatcher: TodoListActionDispatcher
    ) : RecyclerView.Adapter<BindingViewHolder>() {

        var items: List<Todo> by Delegates.observable(emptyList()) { _, old, new ->
            if (old !== new) {
                TodoDiffCalculator.calculateDiff(old, new).dispatchUpdatesTo(this)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ItemTodoBinding>(inflater, R.layout.item_todo, parent, false)
            return BindingViewHolder(binding)
        }

        override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
            val item = TodoItem(items[position]) { todo ->
                actionDispatcher.toggleTodo(todo.id)
            }
            holder.bind(BR.item, item)
        }

        override fun getItemCount(): Int = items.size
    }

    private object TodoDiffCalculator {

        fun calculateDiff(oldList: List<Todo>, newList: List<Todo>): DiffUtil.DiffResult =
                createCallback(oldList, newList).let(DiffUtil::calculateDiff)

        private fun createCallback(oldList: List<Todo>, newList: List<Todo>): DiffUtil.Callback =
                object : DiffUtil.Callback() {

                    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                            oldList[oldItemPosition].id == newList[newItemPosition].id

                    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                            oldList[oldItemPosition] == newList[newItemPosition]

                    override fun getOldListSize(): Int = oldList.size

                    override fun getNewListSize(): Int = newList.size
                }
    }
}
