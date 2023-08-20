package no.javazone.scheduler.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.repository.ConferenceRepository
import no.javazone.scheduler.utils.DEFAULT_CONFERENCE_DAYS
import no.javazone.scheduler.utils.LoadingResource
import no.javazone.scheduler.utils.Resource
import no.javazone.scheduler.utils.WORKSHOP_DAY
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

    private var _detailsArg: Pair<String, String> = "" to ""

    val isReady: LiveData<Boolean> = sessions
        .map {
            it.data.isNotEmpty()
        }
        .asLiveData()

    private var _selectedDay: MutableState<LocalDate> = mutableStateOf(WORKSHOP_DAY)

    val selectedDay: State<LocalDate> = _selectedDay


    init {
        viewModelScope.launch {
            val conf = conference.first {
                it.data != Conference.NULL_INSTANCE
            }
            conferenceDays = conf.data.days
            _selectedDay.value = getDefaultDate()
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
                selectedDay == LocalDate.MIN ||
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

    fun updateDetailsArg(arg: String, from: String) {
        _detailsArg = arg to from
    }

    fun getDetailsArg(): Pair<String, String> = _detailsArg

    fun updateSelectedDay(select: LocalDate) {
        _selectedDay.value = select
    }

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
