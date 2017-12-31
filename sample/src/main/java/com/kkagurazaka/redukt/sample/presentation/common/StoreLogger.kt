package com.kkagurazaka.redukt.sample.presentation.common

import android.util.Log
import com.kkagurazaka.redukt.Action
import com.kkagurazaka.redukt.BuildConfig
import com.kkagurazaka.redukt.State

object StoreLogger : (State, Action, State) -> Unit {

    private const val TAG = "StoreLog"

    override fun invoke(oldState: State, action: Action, newState: State) {
        if (!BuildConfig.DEBUG) return

        Log.d(TAG, "=============== Log ===============")
        Log.d(TAG, "before state: $oldState")
        Log.d(TAG, "action: $action")
        Log.d(TAG, "after changed: $newState")
        Log.d(TAG, "===================================")
    }
}
