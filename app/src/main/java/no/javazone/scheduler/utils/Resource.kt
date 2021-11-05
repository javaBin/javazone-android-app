package no.javazone.scheduler.utils

sealed interface Resource<T>

class SuccessResource<T>(val data: T) : Resource<T>
class LoadingResource<T>(val data: T? = null) : Resource<T>
class ErrorResource<T>(val throwable: Throwable, val data: T? = null) : Resource<T>