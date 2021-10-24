package no.javazone.scheduler.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.repository.ConferenceSessionRepository
import java.time.LocalDate

class ConferenceListViewModel(
    private val repository: ConferenceSessionRepository
) : ViewModel() {

    val sessions: MutableState<List<ConferenceSession>> = mutableStateOf(listOf())

    init {
        viewModelScope.launch {
            val result = repository.getSessions()
            result.observeForever {
                sessions.value = it
            }
        }
    }

    fun sessionOfTheDay(day: LocalDate): List<ConferenceSession> {
        return sessions.value
            .filter {
                it.date == day
            }
    }

    /**
     * Factory for HomeViewModel that takes PostsRepository as a dependency
     */
    companion object {
        fun provideFactory(
            repository: ConferenceSessionRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ConferenceListViewModel(repository) as T
            }
        }
    }
}