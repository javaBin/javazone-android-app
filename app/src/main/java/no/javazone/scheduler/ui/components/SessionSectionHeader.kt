package no.javazone.scheduler.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.javazone.scheduler.ui.theme.JavaZoneTheme

@Composable
fun SessionSectionHeader(text: String, modifier: Modifier = Modifier) {
    Surface(
        tonalElevation = 10.dp,
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .weight(1f)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SessionSectionHeaderLightPreview() {
    SessionSectionHeader(text = "09:00")
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun SessionSectionHeaderDarkPreview() {
    JavaZoneTheme(useDarkTheme = true) {
        SessionSectionHeader(text = "09:00")
    }
}
