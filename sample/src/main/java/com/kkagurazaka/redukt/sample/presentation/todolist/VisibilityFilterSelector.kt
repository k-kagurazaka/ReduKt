package com.kkagurazaka.redukt.sample.presentation.todolist

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.kkagurazaka.redukt.sample.query.VisibilityFilter

class VisibilityFilterSelector @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : Spinner(context, attrs) {

    var visibilityFilter: VisibilityFilter = VisibilityFilter.ALL
        set(value) {
            if (field != value) {
                field = value
                onFilterChanged?.invoke(value)
            }
        }

    var onFilterChanged: ((VisibilityFilter) -> Unit)? = null

    init {
        adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items.map { it.name })
        onItemSelectedListener = object : OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {
                visibilityFilter = VisibilityFilter.ALL
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                visibilityFilter = items[position]
            }

        }
    }

    private companion object {

        val items = listOf(VisibilityFilter.ALL, VisibilityFilter.ACTIVE, VisibilityFilter.COMPLETED)
    }
}
