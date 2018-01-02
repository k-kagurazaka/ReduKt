package com.kkagurazaka.redukt.fixtures

import com.kkagurazaka.redukt.CombinedReducer
import com.kkagurazaka.redukt.Reducer

object TestReducer : CombinedReducer<TestState, TestAction>() {

    init {
        register(StringReducer, TestState::string) { state, subState -> state.copy(string = subState) }
        register(IntReducer, TestState::int) { state, subState -> state.copy(int = subState) }
    }

    override fun initialState(): TestState =
            TestState(
                    string = StringReducer.initialState(),
                    int = IntReducer.initialState()
            )
}

object StringReducer : Reducer<StringState, TestAction.StringAction> {

    override fun initialState(): StringState = StringState("initial")

    override fun reduce(currentState: StringState, action: TestAction.StringAction): StringState =
            when (action) {
                is TestAction.StringAction.Clear -> currentState.copy(description = "")
                is TestAction.StringAction.Append -> currentState.copy(description = currentState.description + action.value)
            }
}

object IntReducer : Reducer<IntState, TestAction.IntAction> {

    override fun initialState(): IntState = IntState(1)

    override fun reduce(currentState: IntState, action: TestAction.IntAction): IntState =
            when (action) {
                is TestAction.IntAction.Clear -> currentState.copy(number = 0)
                is TestAction.IntAction.Add -> currentState.copy(number = currentState.number + action.value)
            }
}

object LongReducer : Reducer<LongState, TestAction.LongAction> {

    override fun initialState(): LongState = LongState(1)

    override fun reduce(currentState: LongState, action: TestAction.LongAction): LongState =
            when (action) {
                is TestAction.LongAction.Increment -> currentState.copy(number = currentState.number + 1)
            }
}
