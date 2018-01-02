package com.kkagurazaka.redukt

import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeout
import java.util.concurrent.TimeUnit

fun testWithTimeout(
        time: Long = 3000,
        unit: TimeUnit = TimeUnit.MILLISECONDS,
        block: suspend CoroutineScope.() -> Unit
): Unit = runBlocking {
    withTimeout(time, unit, block)
}
