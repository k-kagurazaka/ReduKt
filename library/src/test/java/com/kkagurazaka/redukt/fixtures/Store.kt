package com.kkagurazaka.redukt.fixtures

import com.kkagurazaka.redukt.Store
import kotlinx.coroutines.experimental.Unconfined

open class TestStore : Store(Unconfined) {

    init {
        register(TestReducer)
        register(LongReducer)
    }
}

class DoubleIncrementTestStore : TestStore() {

    init {
        register(LongReducer)
    }
}
