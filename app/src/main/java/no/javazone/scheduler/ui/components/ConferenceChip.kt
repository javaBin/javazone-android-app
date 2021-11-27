package no.javazone.scheduler.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.javazone.scheduler.ui.theme.JavaZoneShapes

@Composable
fun ConferenceChip(
    label: String,
    selected: Boolean = false,
    onExecute: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(end = 8.dp),
        elevation = 8.dp,
        shape = JavaZoneShapes.large,
        color = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier.toggleable(
                value = selected,
                onValueChange = {
                    onExecute()
                }
            )
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
@Preview
fun ConferenceChipPreview() {
    ConferenceChip(
        label = "Monday",
        selected = false,
        onExecute = {}
    )
}