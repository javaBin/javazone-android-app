package no.javazone.scheduler.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.repository.ConferenceSessionRepository
import no.javazone.scheduler.utils.LOG_TAG
import java.time.LocalDate

class ConferenceListViewModel(
    private val repository: ConferenceSessionRepository
) : ViewModel() {

    val sessions: MutableState<List<ConferenceSession>> = mutableStateOf(listOf())
    val mySchedule: MutableState<List<String>> = mutableStateOf(listOf())

    init {
        viewModelScope.launch {
            val dataSessions = repository.getSessions()
            dataSessions.observeForever { conferenceSessions ->
                Log.d(LOG_TAG, "Found ${conferenceSessions.size} sessions")
                sessions.value = conferenceSessions
            }

            val dataSchedule = repository.getMySchedule()
            dataSchedule.observeForever {
                mySchedule.value = it
            }
        }
    }

    fun firstConferenceDay(): LocalDate? =
        sessions.value.groupBy { it.date }.keys.firstOrNull()

    fun addOrRemoveSchedule(talkId: String) {
        viewModelScope.launch {
            repository.addOrRemoveSchedule(talkId)
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