package no.javazone.scheduler.ui.partners

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
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
        modifier = Modifier.background(Color.LightGray)
    ) {
        items(partners) { partner ->
            Card(
                modifier = Modifier.padding(4.dp)
            ) {
                AsyncImage(
                    model = partner.logoUrl,
                    imageLoader = imageLoader,
                    error = rememberVectorPainter(Icons.Filled.BrokenImage),
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0f) }),
                    contentDescription = partner.name,
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

@ExperimentalFoundationApi
@Composable
@Preview
fun PartnersContentPreview() {
    val partners = listOf(
        Partner(
            name = "Foo Inc",
            homepageUrl = "https://wwww.vg.no",
            logoUrl = "https://d3o108dy577i1m.cloudfront.net/2019/logos/systek.svg"
        ),
        Partner(
            name = "Bar Inc",
            homepageUrl = "https://www.nettavisen.no",
            logoUrl = "https://d3o108dy577i1m.cloudfront.net/2020/logos/storebrand.png"
        ),
        Partner(
            name = "Foo Inc",
            homepageUrl = "https://wwww.vg.no",
            logoUrl = ""
        ),
        Partner(
            name = "Bar Inc",
            homepageUrl = "https://www.nettavisen.no",
            logoUrl = ""
        ),
        Partner(
            name = "Foo Inc",
            homepageUrl = "https://wwww.vg.no",
            logoUrl = ""
        ),
        Partner(
            name = "Bar Inc",
            homepageUrl = "https://www.nettavisen.no",
            logoUrl = ""
        ),
        Partner(
            name = "Foo Inc",
            homepageUrl = "https://wwww.vg.no",
            logoUrl = ""
        ),
        Partner(
            name = "Bar Inc",
            homepageUrl = "https://www.nettavisen.no",
            logoUrl = ""
        ),
    )

    PartnersContent(partners = partners, imageLoader = LocalContext.current.imageLoader, forwardToWeb = { })
}