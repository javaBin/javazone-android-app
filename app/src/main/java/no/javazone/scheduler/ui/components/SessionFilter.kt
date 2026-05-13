package no.javazone.scheduler.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.javazone.scheduler.R
import no.javazone.scheduler.model.ConferenceDate
import no.javazone.scheduler.model.ConferenceFormat
import no.javazone.scheduler.model.ConferenceLanguage
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.SessionDayFormat
import no.javazone.scheduler.utils.DEFAULT_CONFERENCE_DAYS
import no.javazone.scheduler.utils.FIRST_CONFERENCE_DAY
import java.time.LocalDate

@Composable
fun SessionFilter(
    selectedLanguage: ConferenceLanguage?,
    onLanguageSelected: (ConferenceLanguage?) -> Unit,
    conferenceDays: List<ConferenceDate>,
    selectedDay: LocalDate?,
    onDaySelected: (LocalDate?) -> Unit,
    selectedFormat: ConferenceFormat?,
    onFormatSelected: (ConferenceFormat?) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Language Filter
        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(start = 5.dp, bottom = 10.dp, top = 10.dp)
        ) {
            Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                ConferenceChip(
                    label = "All",
                    selected = selectedLanguage == null,
                    onExecute = { onLanguageSelected(null) }
                )
            }
            ConferenceLanguage.entries.forEach { language ->
                Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                    ConferenceChip(
                        label = stringResource(id = language.label),
                        selected = language == selectedLanguage,
                        onExecute = { onLanguageSelected(language) }
                    )
                }
            }
        }

        // Conference Days Filter
        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(start = 5.dp, bottom = 10.dp)
        ) {
            Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                ConferenceChip(
                    label = "All",
                    selected = selectedDay == null,
                    onExecute = { onDaySelected(null) }
                )
            }
            conferenceDays.sortedBy { it.date }.forEach {
                Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                    ConferenceChip(
                        label = SessionDayFormat.format(it.date),
                        selected = it.date == selectedDay,
                        onExecute = { onDaySelected(it.date) }
                    )
                }
            }
        }

        // Conference Format Filter
        Row(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(start = 5.dp, bottom = 10.dp)
        ) {
            Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                ConferenceChip(
                    label = "All",
                    selected = selectedFormat == null,
                    onExecute = { onFormatSelected(null) }
                )
            }
            ConferenceFormat.entries.forEach { format ->
                Column(modifier = Modifier.padding(start = 4.dp, end = 4.dp)) {
                    ConferenceChip(
                        label = stringResource(id = format.label),
                        selected = format == selectedFormat,
                        onExecute = { onFormatSelected(format) }
                    )
                }
            }
        }

        // Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text(stringResource(R.string.search_hint)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun SessionFilterLightPreview() {
    var i = 0
    SessionFilter(
        selectedLanguage = null,
        onLanguageSelected = {},
        conferenceDays = DEFAULT_CONFERENCE_DAYS.map { ConferenceDate(it, "day ${i++}") },
        selectedDay = FIRST_CONFERENCE_DAY,
        onDaySelected = {},
        selectedFormat = null,
        onFormatSelected = {},
        searchQuery = "",
        onSearchQueryChange = {}
    )
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun SessionFilterDarkPreview() {
    var i = 0
    JavaZoneTheme(useDarkTheme = true) {
        SessionFilter(
            selectedLanguage = null,
            onLanguageSelected = {},
            conferenceDays = DEFAULT_CONFERENCE_DAYS.map { ConferenceDate(it, "day ${i++}") },
            selectedDay = FIRST_CONFERENCE_DAY,
            onDaySelected = {},
            selectedFormat = null,
            onFormatSelected = {},
            searchQuery = "Kotlin",
            onSearchQueryChange = {}
        )
    }
}
