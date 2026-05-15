package no.javazone.scheduler.ui.partners

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.imageLoader
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.model.Partner
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.utils.LOG_TAG
import no.javazone.scheduler.utils.samplePartners
import no.javazone.scheduler.viewmodels.PartnersViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PartnersRoute(
    appContainer: AppContainer
) {
    val viewModel: PartnersViewModel = viewModel(
        factory = PartnersViewModel.provideFactory(appContainer.partnersRepository)
    )

    val partners = viewModel.partners.collectAsState().value
    val context = LocalContext.current

    PartnersContent(
        forwardToWeb = { url ->
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        },
        imageLoader = appContainer.imageLoader,
        partners = partners
    )
}

@ExperimentalFoundationApi
@Composable
fun PartnersContent(
    forwardToWeb: (String) -> Unit,
    partners: List<Partner>,
    imageLoader: ImageLoader
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        items(partners) { partner ->
            Card(
                modifier = Modifier.padding(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = partner.logoUrl,
                        imageLoader = imageLoader,
                        error = rememberVectorPainter(Icons.Filled.BrokenImage),
                        contentDescription = partner.name,
                        onLoading = { Log.d(LOG_TAG, "Loading partner image: ${partner.logoUrl}") },
                        onSuccess = { Log.d(LOG_TAG, "Loaded partner image: ${partner.logoUrl}") },
                        onError = { Log.e(LOG_TAG, "Failed to load partner image: ${partner.logoUrl}") },
                        modifier = Modifier
                            .padding(3.dp)
                            .size(74.dp)
                            .clickable(enabled = partner.homepageUrl.isNotEmpty()) {
                                forwardToWeb(partner.homepageUrl)
                            },
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
@Preview(name = "Light Theme", showBackground = true)
fun PartnersContentLightPreview() {
    JavaZoneTheme(useDarkTheme = false) {
        PartnersContent(
            partners = samplePartners,
            imageLoader = LocalContext.current.imageLoader,
            forwardToWeb = { })
    }
}

@ExperimentalFoundationApi
@Composable
@Preview(name = "Dark Theme", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun PartnersContentDarkPreview() {
    JavaZoneTheme(useDarkTheme = true) {
        PartnersContent(
            partners = samplePartners,
            imageLoader = LocalContext.current.imageLoader,
            forwardToWeb = { })
    }
}
