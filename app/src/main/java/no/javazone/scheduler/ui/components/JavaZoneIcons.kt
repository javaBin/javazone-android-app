package no.javazone.scheduler.ui.components

import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import no.javazone.scheduler.R

@Composable
fun MyScheduleButton(
    isScheduled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentAlpha: Float = ContentAlpha.high
) {
    val clickLabel = stringResource(
        if (isScheduled) R.string.unschedule else R.string.schedule
    )
    CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
        IconToggleButton(
            checked = isScheduled,
            onCheckedChange = { onClick() },
            modifier = modifier.semantics {
                // Use a custom click label that accessibility services can communicate to the user.
                // We only want to override the label, not the actual action, so for the action we pass null.
                this.onClick(label = clickLabel, action = null)
            }
        ) {
            Icon(
                imageVector = if (isScheduled) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = null // handled by click label of parent
            )
        }
    }
}
