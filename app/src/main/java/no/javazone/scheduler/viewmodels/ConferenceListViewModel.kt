package no.javazone.scheduler.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.repository.ConferenceRepository
import no.javazone.scheduler.utils.DEFAULT_CONFERENCE_DAYS
import no.javazone.scheduler.utils.LoadingResource
import no.javazone.scheduler.utils.Resource
import no.javazone.scheduler.utils.SuccessResource
import java.time.LocalDate

class ConferenceListViewModel(
    private val repository: ConferenceRepository
) : ViewModel() {

    val conference: StateFlow<Resource<Conference>> = repository.getConference()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LoadingResource(Conference.NULL_INSTANCE)
        )

    val sessions: StateFlow<Resource<List<ConferenceSession>>> = repository.getSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LoadingResource(emptyList())
        )

    var conferenceDays: List<ConferenceDate> = emptyList()
        private set

    val mySchedule: StateFlow<List<String>> = repository.getSchedules()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = emptyList()
        )

    private var _detailsArg: String = ""

    val _isReady: MutableLiveData<Boolean> = MutableLiveData(false)

    val isReady: LiveData<Boolean> = _isReady

    init {
        viewModelScope.launch {
            _isReady.value = false
            val conf = conference.first {
                it is SuccessResource<Conference>
            } as SuccessResource<Conference>
            conferenceDays = conf.data.days
            _isReady.value = true
        }
    }

    fun getDefaultDate(): LocalDate {
        val today: LocalDate = LocalDate.now()
        val first: LocalDate =
            conferenceDays.map { it.date }.minOrNull() ?: DEFAULT_CONFERENCE_DAYS.first()
        return if (today.isBefore(first) || today.isAfter(first)) first else today
    }

    fun updateSessionsWithMySchedule(
        sessions: List<ConferenceSession>,
        selectedDay: LocalDate,
        mySchedule: List<String>
    ): List<ConferenceSession> =
        sessions
            .filter {
                it.time.toLocalDate() == selectedDay
            }
            .map { session ->
                session.copy(
                    talks = session.talks.map { talk ->
                        if (mySchedule.contains(talk.id)) {
                            talk.copy(scheduled = true)
                        } else {
                            talk
                        }
                    }
                )
            }
            .sortedBy {
                it.time
            }

    fun selectMySchedule(
        sessions: List<ConferenceSession>,
        mySchedule: List<String>
    ): Map<LocalDate, List<ConferenceTalk>> =
        sessions
            .map { session ->
                session.copy(
                    talks = session.talks.mapNotNull { talk ->
                        if (mySchedule.contains(talk.id)) {
                            talk.copy(scheduled = true)
                        } else {
                            null
                        }
                    }
                )
            }
            .flatMap {
                it.talks
            }
            .sortedBy {
                it.slotTime
            }
            .groupBy {
                it.slotTime.toLocalDate()
            }

    fun addOrRemoveSchedule(talkId: String) {
        viewModelScope.launch {
            repository.addOrRemoveSchedule(talkId)
        }
    }

    fun updateDetailsArg(arg: String) {
        _detailsArg = arg
    }

    fun getDetailsArg(): String = _detailsArg

    /**
     * Factory for HomeViewModel that takes PostsRepository as a dependency
     */
    companion object {
        fun provideFactory(
            repository: ConferenceRepository,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ConferenceListViewModel(repository) as T
            }
        }
    }
}
