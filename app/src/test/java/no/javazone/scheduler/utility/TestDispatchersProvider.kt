package no.javazone.scheduler.utility

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import no.javazone.scheduler.utils.DispatchersProvider

@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatchersProvider(
    val scheduler: TestCoroutineScheduler
) : DispatchersProvider {

    private val dispatcher = StandardTestDispatcher(scheduler)

    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val unconfined: CoroutineDispatcher = dispatcher
}