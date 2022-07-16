package no.javazone.scheduler.utils

import android.util.Log
import kotlinx.coroutines.flow.*

inline fun <RESULT_TYPE, REQUEST_TYPE>  networkBoundResource(
    crossinline query: () -> Flow<RESULT_TYPE>,
    crossinline fetch: suspend () -> REQUEST_TYPE,
    crossinline saveFetchResult: suspend (REQUEST_TYPE) -> Unit,
    crossinline shouldFetch: (RESULT_TYPE) -> Boolean = { true }
) = flow {
    val data = query().first()

    val cachedData = if (shouldFetch(data)) {
        emit(LoadingResource(data))

        try {
            saveFetchResult(fetch())
            query().map { SuccessResource(it) }
        } catch (ex: Exception) {
            Log.d(LOG_TAG, "Exception caught", ex)
            query().map { ErrorResource(ex, it) }
        }
    } else {
        query().map { SuccessResource(it) }
    }

    emitAll(cachedData)
}