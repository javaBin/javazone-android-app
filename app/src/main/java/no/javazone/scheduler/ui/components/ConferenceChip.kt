package no.javazone.scheduler.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import no.javazone.scheduler.ui.theme.JavaZoneShapes
import no.javazone.scheduler.ui.theme.JavaZoneTheme

@Composable
fun ConferenceChip(
    label: String,
    selected: Boolean = false,
    onExecute: () -> Unit
) {
    Surface(
        elevation = 8.dp,
        shape = JavaZoneShapes.large,
        color = if (selected) MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.outline
    ) {
        Row(
            modifier = Modifier.toggleable(
                value = selected,
                onValueChange = {
                    onExecute()
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            )
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) MaterialTheme.colorScheme.inverseOnSurface else Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
@Preview
fun ConferenceChipLightPreview(@PreviewParameter(SampleChipsProvider::class) param: Pair<String, Boolean>) {
    ConferenceChip(
        label = param.first,
        selected = param.second,
        onExecute = {}
    )
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun ConferenceChipDarkPreview(@PreviewParameter(SampleChipsProvider::class) param: Pair<String, Boolean>) {
    JavaZoneTheme(useDarkTheme = true) {
        ConferenceChip(
            label = param.first,
            selected = param.second,
            onExecute = {}
        )
    }
}

class SampleChipsProvider : PreviewParameterProvider<Pair<String, Boolean>> {
    override val values =
        listOf(
            "Selected" to true,
            "Not" to false
        ).asSequence()

}
