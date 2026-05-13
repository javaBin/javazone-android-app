package no.javazone.scheduler.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import no.javazone.scheduler.model.ConferenceTalk
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.utils.sampleTalks

@Composable
private fun MetadataBadge(
    icon: ImageVector,
    label: String,
    containerColor: Color
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1
            )
        }
    }
}

@Composable
fun TalkCard(
    talk: ConferenceTalk,
    onToggleSchedule: (String) -> Unit,
    navigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { navigateToDetail(talk.id) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 0.dp)
        ) {
            // Row 1: Room, Length, Type, Language + bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetadataBadge(
                    icon = Icons.Filled.MeetingRoom,
                    label = talk.room.name,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                MetadataBadge(
                    icon = Icons.Filled.Schedule,
                    label = "${talk.length} min",
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                MetadataBadge(
                    icon = Icons.Filled.Category,
                    label = stringResource(id = talk.format.label),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                MetadataBadge(
                    icon = Icons.Filled.Language,
                    label = talk.language,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.weight(1f))
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    MyScheduleButton(
                        isScheduled = talk.scheduled,
                        onClick = { onToggleSchedule(talk.id) },
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            // Row 2: Title
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = talk.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            // Row 3: Speakers
            if (talk.speakers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.RecordVoiceOver,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = talk.speakers.joinToString { it.name },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            // Row 4: Suggested keywords
            if (talk.suggestedKeywords.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    talk.suggestedKeywords.forEach { keyword ->
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(50.dp)
                        ) {
                            Text(
                                text = keyword,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
            // Bottom padding
            Spacer(
                modifier = Modifier.height(
                    if (talk.suggestedKeywords.isNotEmpty()) 8.dp else 14.dp
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun TalkCardLightPreview(@PreviewParameter(SampleTalkProvider::class) talk: ConferenceTalk) {
    TalkCard(
        talk = talk,
        onToggleSchedule = {},
        navigateToDetail = {}
    )
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun TalkCardDarkPreview(@PreviewParameter(SampleTalkProvider::class) talk: ConferenceTalk) {
    JavaZoneTheme(useDarkTheme = true) {
        TalkCard(
            talk = talk,
            onToggleSchedule = {},
            navigateToDetail = {}
        )
    }
}

private class SampleTalkProvider : PreviewParameterProvider<ConferenceTalk> {
    override val values = sampleTalks.asSequence()
}
