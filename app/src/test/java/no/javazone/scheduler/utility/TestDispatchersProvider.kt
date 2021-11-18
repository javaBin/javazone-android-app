package no.javazone.scheduler.utility

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import no.javazone.scheduler.utils.DispatchersProvider

@ExperimentalCoroutinesApi
object TestDispatchersProvider : DispatchersProvider {
    override val default: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
    override val io: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
    override val main: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
    override val unconfined: CoroutineDispatcher
        get() = TestCoroutineDispatcher()
}