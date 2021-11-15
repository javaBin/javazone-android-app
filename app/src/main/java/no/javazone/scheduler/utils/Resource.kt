package no.javazone.scheduler.utils

sealed interface Resource<T> {
    val data: T
}

class SuccessResource<T>(override val data: T) : Resource<T>
class LoadingResource<T>(override val data: T) : Resource<T>
class ErrorResource<T>(val throwable: Throwable, override val data: T) : Resource<T>