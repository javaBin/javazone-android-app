package no.javazone.scheduler.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*import kotlinx.coroutines.launch
import no.javazone.scheduler.model.Conference
import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.model.ConferenceSession
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.repository.ConferenceRepository
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

    val isReady: StateFlow<Boolean> = sessions
        .map {
            it.data.isNotEmpty()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    private var _selectedDay: MutableState<LocalDate?> = mutableStateOf(WORKSHOP_DAY)

    val selectedDay: State<LocalDate?> = _selectedDay

    private var _selectedFormat: MutableState<ConferenceFormat?> = mutableStateOf(null)

    val selectedFormat: State<ConferenceFormat?> = _selectedFormat


    init {
        viewModelScope.launch {
            val conf = conference.first {
                it.data != Conference.NULL_INSTANCE
            }
            conferenceDays = conf.data.days
            _selectedDay.value = getDefaultDate()
        }
    }

    fun getDefaultDate(): LocalDate? {
        val today: LocalDate = LocalDate.now()
        val conferenceDateSet = conferenceDays.map { it.date }.toSet()
        return if (conferenceDateSet.contains(today)) today else null
    }

    fun updateSessionsWithMySchedule(
        sessions: List<ConferenceSession>,
        selectedDay: LocalDate?,
        selectedFormat: ConferenceFormat?,
        mySchedule: List<String>
    ): List<ConferenceSession> =
        sessions
            .filter {
                selectedDay == null || it.time.toLocalDate() == selectedDay
            }
            .mapNotNull { session ->
                val filteredTalks = session.talks
                    .filter { talk -> selectedFormat == null || talk.format == selectedFormat }
                if (filteredTalks.isEmpty()) {
                    null
                } else {
                    session.copy(
                        talks = filteredTalks.map { talk ->
                            if (mySchedule.contains(talk.id)) {
                                talk.copy(scheduled = true)
                            } else {
                                talk
                            }
                        }
                    )
                }
            }
            .sortedBy {
                it.time
            }

    fun selectMySchedule(
        sessions: List<ConferenceSession>,
        mySchedule: List<String>
    ): Map<LocalDate, List<ConferenceTalk>> =
        sessions
            .flatMap { session ->
                session.talks
                    .filter { mySchedule.contains(it.id) }
                    .map { it.copy(scheduled = true) }
            }
            .sortedBy { it.slotTime }
            .groupBy { it.slotTime.toLocalDate() }

    fun addOrRemoveSchedule(talkId: String) {
        viewModelScope.launch {
            repository.addOrRemoveSchedule(talkId)
        }
    }

    fun updateDetailsArg(arg: String, from: String) {
        _detailsArg = arg to from
    }

    fun getDetailsArg(): Pair<String, String> = _detailsArg

    fun updateSelectedDay(select: LocalDate?) {
        _selectedDay.value = select
    }

    fun updateSelectedFormat(format: ConferenceFormat?) {
        _selectedFormat.value = format
        val workshopDay = conferenceDays.find { it.label == "workshop" }?.date
        when (format) {
            ConferenceFormat.WORKSHOP -> {
                _selectedDay.value = workshopDay
            }
            ConferenceFormat.PRESENTATION,
            ConferenceFormat.LIGHTNING_TALK -> {
                if (_selectedDay.value == null || _selectedDay.value == workshopDay) {
                    _selectedDay.value = conferenceDays
                        .filter { it.label != "workshop" }
                        .minByOrNull { it.date }?.date
                }
            }
            else -> {}
        }
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
