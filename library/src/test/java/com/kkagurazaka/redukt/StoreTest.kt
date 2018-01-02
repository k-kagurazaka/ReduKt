package com.kkagurazaka.redukt

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.kkagurazaka.redukt.fixtures.DoubleIncrementTestStore
import com.kkagurazaka.redukt.fixtures.LongState
import com.kkagurazaka.redukt.fixtures.TestAction
import com.kkagurazaka.redukt.fixtures.TestState
import com.kkagurazaka.redukt.fixtures.TestStore
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.check
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import kotlinx.coroutines.experimental.Job
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class StoreTest {

    class OnInitial {

        @Rule
        @JvmField
        val rule = InstantTaskExecutorRule()

        @Test
        fun getStateReturnsInitialState() = testWithTimeout {
            val store = TestStore()

            val testState = store.getState<TestState>()
            assertThat(testState.string.description).isEqualTo("initial")
            assertThat(testState.int.number).isEqualTo(1)

            val longState = store.getState<LongState>()
            assertThat(longState.number).isEqualTo(1L)
        }

        @Test
        fun getLiveStateReturnsInitialLiveState() = testWithTimeout {
            val store = TestStore()
            val testObserver = mock<Observer<TestState>>()
            val longObserver = mock<Observer<LongState>>()

            store.getLiveState<TestState>().observeForever(testObserver)
            store.getLiveState<LongState>().observeForever(longObserver)

            verify(testObserver).onChanged(check {
                assertThat(it.string.description).isEqualTo("initial")
                assertThat(it.int.number).isEqualTo(1)
            })

            verify(longObserver).onChanged(check {
                assertThat(it.number).isEqualTo(1L)
            })
        }
    }

    class AfterActionsDispatched {

        @Rule
        @JvmField
        val rule = InstantTaskExecutorRule()

        @Test
        fun getStateReturnsNewState() = testWithTimeout {
            val store = TestStore()

            store.dispatchTestActions().forEach { it.join() }

            val testState = store.getState<TestState>()
            assertThat(testState.string.description).isEqualTo("initial+append+append2")
            assertThat(testState.int.number).isEqualTo(2)

            val longState = store.getState<LongState>()
            assertThat(longState.number).isEqualTo(3L)
        }

        @Test
        fun getLiveStateReturnsNewLiveState() = testWithTimeout {
            val store = TestStore()
            val testObserver = mock<Observer<TestState>>()
            val longObserver = mock<Observer<LongState>>()

            store.getLiveState<TestState>().observeForever(testObserver)
            store.getLiveState<LongState>().observeForever(longObserver)

            store.dispatchTestActions().forEach { it.join() }

            argumentCaptor<TestState>().apply {
                verify(testObserver, times(5)).onChanged(capture())
                assertThat(lastValue.string.description).isEqualTo("initial+append+append2")
                assertThat(lastValue.int.number).isEqualTo(2)
            }

            argumentCaptor<LongState>().apply {
                verify(longObserver, times(3)).onChanged(capture())
                assertThat(lastValue.number).isEqualTo(3L)
            }
        }

        @Test
        fun onStateChangedCalled() = testWithTimeout {
            val store = TestStore()
            val onStateChanged = mock<(State, Action, State) -> Unit>()
            store.onStateChanged = onStateChanged

            store.dispatchTestActions().forEach { it.join() }

            verify(onStateChanged, times(6)).invoke(any(), any(), any())
        }

        private fun TestStore.dispatchTestActions(): List<Job> =
                dispatchAll(
                        TestAction.LongAction.Increment,
                        TestAction.IntAction.Clear,
                        TestAction.StringAction.Append("+append"),
                        TestAction.StringAction.Append("+append2"),
                        TestAction.IntAction.Add(2),
                        TestAction.LongAction.Increment
                )
    }

    class OnMultipleReducersBindToSingleState {

        @Rule
        @JvmField
        val rule = InstantTaskExecutorRule()

        @Test
        fun allReducersCalled() = testWithTimeout {
            val store = DoubleIncrementTestStore()

            store.dispatchAll(
                    TestAction.LongAction.Increment,
                    TestAction.LongAction.Increment
            ).forEach {
                it.join()
            }

            val longState = store.getState<LongState>()
            assertThat(longState.number).isEqualTo(5L)
        }
    }
}

private fun Store.dispatchAll(vararg actions: Action): List<Job> {
    val jobs = mutableListOf<Job>()
    for (action in actions) {
        dispatch(action).let(jobs::add)
    }
    return jobs
}
