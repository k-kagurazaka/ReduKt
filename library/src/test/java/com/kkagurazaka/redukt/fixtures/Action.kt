package com.kkagurazaka.redukt.fixtures

import com.kkagurazaka.redukt.Action

sealed class TestAction : Action {

    sealed class StringAction : TestAction() {

        object Clear : StringAction()

        data class Append(val value: String) : StringAction()
    }

    sealed class IntAction : TestAction() {

        object Clear : IntAction()

        data class Add(val value: Int) : IntAction()
    }

    sealed class LongAction : TestAction() {

        object Increment : LongAction()
    }
}
