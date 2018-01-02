package com.kkagurazaka.redukt.fixtures

import com.kkagurazaka.redukt.State

data class TestState(val string: StringState, val int: IntState) : State

data class StringState(val description: String) : State

data class IntState(val number: Int) : State

data class LongState(val number: Long) : State
